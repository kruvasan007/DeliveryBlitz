package com.example.myprojectgame.ui.delivery;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;
import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationProvider;
import android.location.LocationRequest;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;
import com.example.myprojectgame.ui.choose.ChooseTransportActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DeliveryActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings uiSettings;
    private OrderDao dao;
    private int time;
    public static List<Double> currentCoord;
    private LatLng gamer;
    private Random random = new Random();
    private int earn, exp;
    private double maxN, minN;
    private List<OrderData> data;
    private OrderData randomOrder;
    private int boundOrder;
    private double randomLatitude, randomLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        ImageButton backButton = findViewById(R.id.linear_layout).findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> backActivity());

        dao = App.getAppDatabaseInstance().orderDao();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    private void backActivity() {
        Intent intent = new Intent(DeliveryActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    protected void nextActivity() {
        if (selectOrderData.currentTime <= 0) makeToastSize("Выберите ресторан");
        else {
            try {
                selectOrderData.currentTime = time;
                Intent intent = new Intent(DeliveryActivity.this, ChooseTransportActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                makeToastSize("Выберите ресторан");
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        settingsMap();
        data = dao.selectOrder();

        maxN = Math.sqrt((float) gameData.exp + 1) / 1000 + 0.01;
        minN = -maxN;
        if (gameData.exp > 1000){
            boundOrder = 11;
        } else boundOrder = gameData.exp/100 + 1;

        for (int i = 0; i <= boundOrder; i++)
            setNewOrderMarker();

        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) marker -> {
            if (!gamer.equals(marker.getPosition())) {
                float[] results = new float[1];
                currentCoord = new ArrayList<Double>();
                currentCoord.add(marker.getPosition().latitude);
                currentCoord.add(marker.getPosition().longitude);
                Location.distanceBetween(gamer.latitude, gamer.longitude,
                        marker.getPosition().latitude, marker.getPosition().longitude, results);

                time = calculateTime(results[0]);
                earn = calculateEarn(results[0]);
                exp = calculateExp(results[0]);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new DeliveryFragment()).commit();

                selectOrderData.addExp = exp;
                selectOrderData.selectCurrentCoord = currentCoord;
                selectOrderData.earnFomOrder = earn;
                selectOrderData.currentTime = time;
                selectOrderData.name = marker.getTitle();
            }
            return false;
        });
    }

    private void setNewOrderMarker() {
        randomOrder = data.get(random.nextInt(data.size()));
        BitmapDescriptor iconShop = getIconFromDrawables(getDrawable(randomOrder.icon));
        List<Double> or = new ArrayList<Double>();
        randomLatitude = (minN + random.nextFloat() * (maxN - minN));
        randomLongitude = (minN + random.nextFloat() * (maxN - minN));
        if (Math.abs(randomLatitude) < maxN / 2){
            randomLatitude += maxN / 2;
        }
        if (Math.abs(randomLongitude) < maxN / 2){
            randomLongitude += maxN / 2;
        }
        or.add(gamer.latitude + randomLatitude);
        or.add(gamer.longitude + randomLongitude);
        if (Float.parseFloat(String.valueOf(gameData.gamerCoord.get(0))) != or.get(0))
            mMap.addMarker(new MarkerOptions().position(new LatLng(or.get(0), or.get(1))).title(randomOrder.name).icon(iconShop).zIndex(randomOrder.id));
    }

    private int calculateExp(float number) {
        return (int) (Math.log(gameData.exp + 2) * number / 300);
    }

    private int calculateEarn(float number) {
        return (int) (number / ((4 / 3.6) * 300) + ((1 + gameData.exp) / 5));
    }

    private int calculateTime(float number) {
        if ((int) number != 0) {
            return (int) (Math.sqrt((float) (gameData.exp * gameData.exp / 25) + 0.2 * gameData.exp + 10) * (number / 500));
        } else return 1;
    }

    private void settingsMap() {
        gamer = new LatLng(gameData.gamerCoord.get(0), gameData.gamerCoord.get(1));
        mMap.addMarker(new MarkerOptions().draggable(false).position(gamer).title("ВЫ НАХОДИТЕСЬ ЗДЕСЬ").icon(getIconFromDrawables(getDrawable(R.drawable.walking))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gamer));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gamer));
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);
    }

    private void makeToastSize(String t) {
        String text = t;
        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(0.7f), 0, text.length(), 0);
        Toast.makeText(this, biggerText, Toast.LENGTH_LONG).show();
    }

    private BitmapDescriptor getIconFromDrawables(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}