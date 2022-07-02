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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.myprojectgame.R;
import com.example.myprojectgame.data.GameData;
import com.example.myprojectgame.data.SelectOrderData;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.domain.PreferencesManager;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.click.ClickerActivity;
import com.example.myprojectgame.ui.MyDialogFragment;
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

    private final Random random = new Random();
    private Button buttonStart;

    private ImageButton buttonUpdate;
    private ImageButton buttonLocation;
    private TextView updateText;
    private FusedLocationProviderClient client;
    private Location lastLocation;
    public static SelectOrderData selectOrderData;


    private TextView textHealth;
    private TextView textPoint;
    private Timer mTimer;
    private MyHealthTimer myHealthTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        healthBar = findViewById(R.id.progressBar);
        pointsBar = findViewById(R.id.progressBarPoints);
        buttonStart = findViewById(R.id.select_button);
        buttonLocation = findViewById(R.id.location_button);

        buttonUpdate = findViewById(R.id.update_button);
        updateText = findViewById(R.id.update_text);
        buttonStart.setOnClickListener(v -> startButton());

        ImageButton buttonShop = findViewById(R.id.shop_button);
        buttonShop.setOnClickListener(v -> shopButton());

        TextView textMoney = findViewById(R.id.money);
        textHealth = findViewById(R.id.health);
        TextView textEx = findViewById(R.id.exp);
        textPoint = findViewById(R.id.countPoints);


        client = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        dao = App.getAppDatabaseInstance().orderDao();

        //getPreferences
        pm = new PreferencesManager(this);
        createGameData();
        createSelectOrderData();
        getIntentionLastActivity();
        requestDay();
        chekDayAchievements();

        //request enabled location
        if (!pm.getEnableLocationChoose() && checkPermissions())
            buttonLocation.setVisibility(View.VISIBLE);
        buttonLocation.setOnClickListener(view -> getLastLocation());


        //update progressBar
        if (pointsBar.getProgress() < 10 && !pm.getDayAchievements()) {
            textPoint.setText(gameData.doneOrder.size() + "/10");
            pointsBar.setProgress(gameData.doneOrder.size());
            buttonUpdate.setVisibility(View.INVISIBLE);
            updateText.setVisibility(View.INVISIBLE);
        }
        else{
            textPoint.setText("10/10");
            pointsBar.setProgress(10);
        }

        if(gameData.doneOrder.size() == 10) {
            buttonUpdate.setVisibility(View.VISIBLE);
            updateText.setVisibility(View.VISIBLE);
        }

        //request update orders
        buttonUpdate.setOnClickListener(view -> {
            if (gameData.money >= 100) {
                if (locationEnabled()) {
                    gameData.money -= 100;
                    textMoney.setText(gameData.money+"");
                    gameData.doneOrder = new ArrayList<>();
                    getLastLocation();
                    buttonUpdate.setVisibility(View.INVISIBLE);
                    updateText.setVisibility(View.INVISIBLE);
                }else makeToastSize("Ваша геолокация временно недоступна", 1);
            }
            else makeToastSize("У вас недостаточно средств",3);
        });

        textMoney.setText(String.valueOf(gameData.money));
        textHealth.setText(String.valueOf(gameData.health));
        textEx.setText(String.valueOf(gameData.exp));
    }

    private void dialogAchievements() {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.setValue("Задание на сегодня выполнено!","Поздравляем!",R.layout.achivment_dialog);
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    private void chekDayAchievements(){
        if (gameData.doneOrder.size() == 10 && !pm.getDayAchievements()) {
            gameData.money += 100;
            dialogAchievements();
            buttonUpdate.setVisibility(View.VISIBLE);
            updateText.setVisibility(View.VISIBLE);
            pm.setDayAchievements(true);
        }
    }

    //button actions
    private void startButton() {
        if (gameData.health >= 10) {
            startDeliveryWithAnimation();
        } else makeToastSize("У вас недостаточно жизней", 0);
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
                makeToastSize("Доставка выполнена успешно", 2);
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

    private void makeToastSize(String message, int type) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (LinearLayout) findViewById(R.id.toast_layout));

        TextView head = layout.findViewById(R.id.head);
        TextView description = layout.findViewById(R.id.descript);
        switch (type) {
            case 0:
                head.setText("Здоровье");
                head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.heart), null, null, null);
                break;
            case 1:
                head.setText("Локация");
                head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_location_without_button), null, null, null);
                break;
            case 2:
                head.setText("Вознаграждение");
                head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.money), null, null, null);
                break;
            case 3:
                head.setText("Ошибка");
                head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.money), null, null, null);
                break;
        }
        description.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 10, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        Integer lastDayLogIn = pm.getLastLogIn();
        if (lastDayLogIn != day) {
            lastDayLogIn = day;
            pm.setLastLogIn(lastDayLogIn);
            gameData.doneOrder = new ArrayList<String>();
            pointsBar.setProgress(gameData.doneOrder.size());
            textPoint.setText(gameData.doneOrder.size() + "/10");
            pm.setEnableLocationChoose(false);
            pm.setDayAchievements(false);
            getLastLocation();
        }
    }

    private void dailyUpdatePoint() {
        double maxN = Math.sqrt((float) gameData.exp + 1) / 350 + 0.01;
        double minN = -maxN;
        for (OrderData order : dao.selectOrder()) {
            List<Double> or = new ArrayList<Double>();
            double randomLatitude = (minN + random.nextFloat() * (maxN - minN));
            double randomLongitude = (minN + random.nextFloat() * (maxN - minN));
            if (Math.abs(randomLatitude) < maxN / 2)
                randomLatitude += maxN / 2;
            if (Math.abs(randomLongitude) < maxN / 2)
                randomLongitude += maxN / 2;
            or.add(lastLocation.getLatitude() + randomLatitude);
            or.add(lastLocation.getLongitude() + randomLongitude);
            dao.updateCordinates(order.id, or.get(0) + ", " + or.get(1));
        }
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
                        buttonLocation.setVisibility(View.GONE);
                        lastLocation = task.getResult();
                        pm.setEnableLocationChoose(true);
                        gameData.gamerCoord.set(0, lastLocation.getLatitude());
                        gameData.gamerCoord.set(1, lastLocation.getLongitude());
                        dailyUpdatePoint();
                        makeToastSize("Заказы успешно обновлены", 1);
                    }
                });
            } else {
                makeToastSize("Ваша геолокация временно недоступна", 1);
            }
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
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    private final LocationCallback locationCallback = new LocationCallback() {
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
        runOnUiThread(() -> {
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