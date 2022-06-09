package com.example.myprojectgame.ui.root;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import com.example.myprojectgame.R;
import com.example.myprojectgame.data.GameData;
import com.example.myprojectgame.data.SelectOrderData;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.domain.PreferencesManager;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.click.ClickerActivity;
import com.example.myprojectgame.ui.delivery.DeliveryActivity;
import com.example.myprojectgame.ui.shop.ShopActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private OrderDao dao;

    public static GameData gameData;
    private ProgressBar healthBar, pointsBar;
    private long regenerateTime;
    private PreferencesManager pm;

    private Random random = new Random();
    private Button buttonStart;

    private double randomLatitude, randomLongitude;
    private Calendar calendar;
    private Integer lastDayLogIn;
    private double minN;
    private int day;
    private double maxN;

    private FusedLocationProviderClient client;
    private Location lastLocation;
    public static SelectOrderData selectOrderData;


    private TextView textMoney, textHealth, textEx, textPoint;
    private Timer mTimer;
    private MyHealthTimer myHealthTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        healthBar = findViewById(R.id.progressBar);
        pointsBar = findViewById(R.id.progressBarPoints);
        buttonStart = findViewById(R.id.select_button);
        buttonStart.setOnClickListener(v -> startButton());

        ImageButton buttonShop = findViewById(R.id.shop_button);
        buttonShop.setOnClickListener(v -> shopButton());

        textMoney = findViewById(R.id.money);
        textHealth = findViewById(R.id.health);
        textEx = findViewById(R.id.exp);
        textPoint = findViewById(R.id.countPoints);


        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        dao = App.getAppDatabaseInstance().orderDao();

        //getPreferences
        pm = new PreferencesManager(this);
        createGameData();
        createSelectOrderData();
        getIntentionLastActivity();
        requestDay();

        textMoney.setText(String.valueOf(gameData.money));
        textHealth.setText(String.valueOf(gameData.health));
        textEx.setText(String.valueOf(gameData.exp));
        textPoint.setText(gameData.doneOrder.size() + "/10");
        pointsBar.setProgress(gameData.doneOrder.size());
    }

    //button actions
    private void startButton() {
        if (gameData.health >= 10) {
            startDeliveryWithAnimation();
        } else Toast.makeText(this, "У вас недостаточно жизней", Toast.LENGTH_SHORT).show();
    }

    private void shopButton() {
        Intent intent = new Intent(MainActivity.this, ShopActivity.class);
        startActivity(intent);
        finish();
    }

    //intention to current order
    private void getIntentionLastActivity() {
        if (selectOrderData.state == 1) {
            if (System.currentTimeMillis() - selectOrderData.lastTime >= selectOrderData.currentTime * 1000) {
                if (gameData.doneOrder.size() == 10) {
                    gameData.money += 100;
                    Toast.makeText(this, "Это был последний заказ! Молодец!", Toast
                            .LENGTH_SHORT).show();
                } else Toast.makeText(this, "Доставка выполнена успешно", Toast
                        .LENGTH_SHORT).show();
                gameData.money += selectOrderData.earnFomOrder;
                gameData.exp += selectOrderData.addExp;
                selectOrderData = new SelectOrderData(null, null, null, null, null, null, null, null, null);
            } else {
                selectOrderData.currentTime -= (System.currentTimeMillis() - selectOrderData.lastTime) / 1000;
                startActivity(new Intent(this, ClickerActivity.class));
                finish();
            }
        }
    }

    //create data from pm
    private void createGameData() {
        gameData = pm.getGameData();
        startHealthRegeneration();
    }

    private void createSelectOrderData() {
        selectOrderData = pm.getSelectableOrderData();
    }

    //request last login day for update
    private void requestDay() {
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
        lastDayLogIn = pm.getLastLogIn();
        if (lastDayLogIn != day) {
            gameData.doneOrder = new ArrayList<>();
            getLastLocation();
        }
    }

    private void dailyUpdatePoint() {
        lastDayLogIn = day;
        pm.setLastLogIn(lastDayLogIn);
        maxN = Math.sqrt((float) gameData.exp + 1) / 350 + 0.01;
        minN = -maxN;
        for (OrderData order : dao.selectOrder()) {
            List<Double> or = new ArrayList<Double>();
            randomLatitude = (minN + random.nextFloat() * (maxN - minN));
            randomLongitude = (minN + random.nextFloat() * (maxN - minN));
            if (Math.abs(randomLatitude) < maxN / 2)
                randomLatitude += maxN / 2;
            if (Math.abs(randomLongitude) < maxN / 2)
                randomLongitude += maxN / 2;
            or.add(lastLocation.getLatitude() + randomLatitude);
            or.add(lastLocation.getLongitude() + randomLongitude);
            dao.updateCordinates(order.id, or.get(0) + ", " + or.get(1));
        }
        pm.close();
    }

    //request location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (locationEnabled()) {
                client.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        lastLocation = task.getResult();
                        gameData.gamerCoord.set(0, lastLocation.getLatitude());
                        gameData.gamerCoord.set(1, lastLocation.getLongitude());
                        dailyUpdatePoint();
                    }
                });
            } else
                Toast.makeText(this, "Ваша геолокация временно недоступна", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5);
        request.setInterval(5);
        request.setFastestInterval(0);
        request.setNumUpdates(1);
        client = LocationServices.getFusedLocationProviderClient(this);
        client.requestLocationUpdates(request, locationCallback, Looper.myLooper());
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            lastLocation = locationResult.getLastLocation();
            gameData.gamerCoord.set(0, lastLocation.getLatitude());
            gameData.gamerCoord.set(1, lastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean locationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //animation HEALTH textview
    private void startDeliveryWithAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        animation.reset();
        textHealth.clearAnimation();
        textHealth.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                buttonStart.setEnabled(false);
                gameData.health -= 10;
                textHealth.setText(String.valueOf(gameData.health));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                buttonStart.setEnabled(true);
                Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    //timer HEALTH REGENERATION
    private void startHealthRegeneration() {
        System.out.println(gameData);
        if (gameData.health < 100) {
            healthBar.setVisibility(View.VISIBLE);
            regenerateTime = System.currentTimeMillis() + 1000 * 120;
            healthBar.setProgress(0);
            healthBar.setMax(1000 * 120);
            mTimer = new Timer();
            myHealthTask = new MyHealthTimer();
            mTimer.schedule(myHealthTask, 1000, 1000);
        }
    }

    class MyHealthTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    healthBar.setProgress((int) (1000 * 120 - (regenerateTime - System.currentTimeMillis())));
                    if (healthBar.getProgress() >= healthBar.getMax()) {
                        gameData.health += 10;
                        textHealth.setText(String.valueOf(gameData.health));
                        myHealthTask.cancel();
                        mTimer.cancel();
                        mTimer.purge();
                        if (gameData.health < 100) {
                            startHealthRegeneration();
                        } else {
                            healthBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myHealthTask != null) {
            myHealthTask.cancel();
            mTimer.cancel();
            mTimer.purge();
        }
    }
}