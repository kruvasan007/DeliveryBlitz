package com.example.myprojectgame.ui.delivery;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;
import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageButton;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings uiSettings;
    private final String API_KEY = "AIzaSyB1zfb3xWhk8yBV14SrgQrNEBQr3Jprew4";
    private OrderDao dao;
    private DirectionsResult direction;
    private int time;
    public static List<Double> currentCoord;
    private LatLng gamer;
    private int earn, exp;
    private ArrayList<List<Float>> coords;

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (OrderData order : dao.selectOrder()) {
            String[] o = order.coordinates.split(",");
            coords = new ArrayList<>();
            BitmapDescriptor iconShop = getIconFromDrawables(getDrawable(order.icon));
            List<Float> or = new ArrayList<Float>();
            or.add(Float.parseFloat(o[0]));
            or.add(Float.parseFloat(o[1]));
            coords.add(or);
            if (Float.parseFloat(String.valueOf(gameData.gamerCoord.get(0))) != or.get(0))
                mMap.addMarker(new MarkerOptions().position(new LatLng(or.get(0), or.get(1))).title(order.name).icon(iconShop).zIndex(order.id));
        }
        settingsMap();

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
        mMap.moveCamera(CameraUpdateFactory.zoomTo(14f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gamer));
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);
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