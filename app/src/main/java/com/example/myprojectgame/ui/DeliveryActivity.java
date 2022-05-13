package com.example.myprojectgame.ui;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.ui.click.ClickerActivity;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private UiSettings uiSettings;
    private ArrayList<List<Float>> coords;
    private List<OrderData> RelativeOrder;
    private LatLng shop;
    private OrderDao dao;
    private Marker prevMarker;
    private List<Double> currentCoord;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);
        time = 0;
        Button nextButton = findViewById(R.id.next_step_button);
        TextView earn = findViewById(R.id.earn);
        TextView wasted = findViewById(R.id.wasted);
        wasted.setText("-" + gameData.cost);
        earn.setText("+" + gameData.earn);
        nextButton.setOnClickListener(v -> nextActivity());
        dao = App.getAppDatabaseInstance().orderDao();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void nextActivity() {
        if (time == 0) makeToastSize("Выберите ресторан");
        else {
            gameData.gamerCoord = currentCoord;
            ClickerActivity.setStart(currentCoord, gameData.k, time);
            Intent intent = new Intent(DeliveryActivity.this, ClickerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        coords = new ArrayList<List<Float>>();
        System.out.println(gameData.order);
        RelativeOrder = dao.getByName(gameData.order);
        BitmapDescriptor iconShop = getIconFromDrawables(getDrawable(Integer.parseInt(RelativeOrder.get(0).icon)));
        LatLng gamer = new LatLng(gameData.gamerCoord.get(0), gameData.gamerCoord.get(1));
        mMap.addMarker(new MarkerOptions().draggable(false).position(gamer).title("ВЫ НАХОДИТЕСЬ ЗДЕСЬ").icon(getIconFromDrawables(getDrawable(R.drawable.bus))));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gamer));
        for (OrderData order : RelativeOrder) {
            String[] o = order.coordinates.split(",");
            List<Float> or = new ArrayList<Float>();
            or.add(Float.parseFloat(o[0]));
            or.add(Float.parseFloat(o[1]));
            coords.add(or);
            if (Float.parseFloat(String.valueOf(gameData.gamerCoord.get(0))) != or.get(0)) mMap.addMarker(new MarkerOptions().position(new LatLng(or.get(0), or.get(1))).title(order.name).icon(iconShop));
        }
        mMap.moveCamera(CameraUpdateFactory.zoomTo(13f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(gamer));
        uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);

        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) marker -> {
            if (!gamer.equals(marker.getPosition())) {
                float[] results = new float[1];
                currentCoord = new ArrayList<Double>();
                currentCoord.add(marker.getPosition().latitude);
                currentCoord.add(marker.getPosition().longitude);
                Location.distanceBetween(gamer.latitude, gamer.longitude,
                                marker.getPosition().latitude, marker.getPosition().longitude, results);
                time = (int) (results[0] / (4 / 3.6) * gameData.k) ;
                marker.setTitle(time / 60 + " мин");
            }
            return false;
        });
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