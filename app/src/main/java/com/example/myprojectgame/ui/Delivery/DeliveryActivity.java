package com.example.myprojectgame.ui.Delivery;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;
import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;
import com.example.myprojectgame.ui.select.ChooseOrderActivity;
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

public class DeliveryActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings uiSettings;
    private ArrayList<List<Float>> coords;
    private OrderDao dao;
    private int time;
    public static List<Double> currentCoord;
    private LatLng gamer;

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
                Intent intent = new Intent(DeliveryActivity.this, ChooseOrderActivity.class);
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

        createGamerMarker();

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
                time = (int) (results[0] / (65 * (4 / 3.6)) + ((1+ gameData.exp) / 10));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, new DeliveryFragment(marker.getZIndex(), time)).commit();
                selectOrderData.addExp = dao.getById((int) marker.getZIndex()).get(0).exp;
                selectOrderData.earnFomOrder = dao.getById((int) marker.getZIndex()).get(0).cost;
                selectOrderData.currentTime = time;
            }
            return false;
        });
    }

    private void settingsMap() {
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gamer));
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);
    }

    private void createGamerMarker() {
        gamer = new LatLng(gameData.gamerCoord.get(0), gameData.gamerCoord.get(1));
        mMap.addMarker(new MarkerOptions().draggable(false).position(gamer).title("ВЫ НАХОДИТЕСЬ ЗДЕСЬ").icon(getIconFromDrawables(getDrawable(R.drawable.walking))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gamer));

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