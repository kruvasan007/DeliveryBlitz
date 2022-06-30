package com.example.myprojectgame.ui.delivery;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;
import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class DeliveryActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private OrderDao dao;
    private int time;
    public static List<Double> currentCoord;
    private LatLng gamer;
    private int earn, exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery);

        ImageButton backButton = findViewById(R.id.linear_layout).findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> backActivity());

        dao = App.getAppDatabaseInstance().orderDao();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        if (!checkInternetConnection()){
            makeToastSize();
        }
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void backActivity() {
        gameData.health += 10;
        Intent intent = new Intent(DeliveryActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkInternetConnection(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json_1));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        //add new points from db
        for (OrderData order : dao.selectOrder()) {
            if (!gameData.doneOrder.contains(order.name)) {
                String[] o = order.coordinates.split(",");
                ArrayList<List<Float>> cords = new ArrayList<>();
                BitmapDescriptor iconShop = getIconFromDrawables(getDrawable(order.icon));
                List<Float> or = new ArrayList<Float>();
                or.add(Float.parseFloat(o[0]));
                or.add(Float.parseFloat(o[1]));
                cords.add(or);
                if (Float.parseFloat(String.valueOf(gameData.gamerCoord.get(0))) != or.get(0))
                    mMap.addMarker(new MarkerOptions().position(new LatLng(or.get(0), or.get(1))).title(order.name).icon(iconShop).zIndex(order.id));
            }
        }
        settingsMap();

        //if user choose point
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

    //calculations of stats
    private int calculateExp(float number) {
        int result = (int) (Math.log(gameData.exp + 2) * number / 300);
        if (result != 0){
            return result;
        } else return 1;
    }

    private int calculateEarn(float number) {
        int result_money = (int) (number / ((4 / 3.6) * 300) + ((1 + gameData.exp) / 5));
        if(result_money != 0) {
            return result_money;
        } else return 1;
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
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setMapToolbarEnabled(false);
        uiSettings.setCompassEnabled(false);
    }

    private void makeToastSize() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (LinearLayout) findViewById(R.id.toast_layout));
        TextView head = layout.findViewById(R.id.head);
        TextView description = layout.findViewById(R.id.descript);
        head.setText("Ошибка");
        head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_location_without_button), null, null, null);
        description.setText("Ошибка подключения к интернету");
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 10, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private BitmapDescriptor getIconFromDrawables(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().popBackStack();
    }
}