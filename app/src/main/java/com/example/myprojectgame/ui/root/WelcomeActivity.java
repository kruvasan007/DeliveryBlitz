package com.example.myprojectgame.ui.root;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.myprojectgame.R;
import com.example.myprojectgame.data.IconId;
import com.example.myprojectgame.db.FoodData;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.db.TransportData;
import com.example.myprojectgame.domain.PreferencesManager;
import com.example.myprojectgame.ui.App;

public class WelcomeActivity extends AppCompatActivity {
    private PreferencesManager pm;
    private OrderDao dao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        getSupportActionBar().hide();
        pm = new PreferencesManager(this);

        //if it is first run
        if (pm.getFirstRun()) {
            pm.setFirstRun();
            firstRunDataBase();
            Button chooseLocation = findViewById(R.id.choose_location_button);
            Button autoButton = findViewById(R.id.auto_button);
            chooseLocation.setOnClickListener(v -> {
                pm.setEnableLocationChoose(true);
                requestPermissions();
            });
            autoButton.setOnClickListener(view1 -> {
                startMainActivity();
            });
        }
        else startMainActivity();
    }

    private void startMainActivity() {
        pm.close();
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    //create new database
    private void firstRunDataBase() {
        dao = App.getAppDatabaseInstance().orderDao();
        dao.insertOrder(new OrderData("МАК ДАК", "55.03963005768076, 82.96122777648965", IconId.MCDONALDS.getIcon()));
        dao.insertOrder(new OrderData("ДОДО", "55.0106521380292, 82.93736307619466", IconId.DODO.getIcon()));
        dao.insertOrder(new OrderData("ЧАШКА КОФЕ", "55.04477301431026, 82.92285248440155", IconId.COUPLE.getIcon()));
        dao.insertOrder(new OrderData("ХЛЕБНИЦА", "54.96414597832322, 82.93641956376621", IconId.PINE.getIcon()));

        dao.insertOrder(new OrderData("KФC", "54.989384589520384, 82.9079604150277", IconId.KFC.getIcon()));
        dao.insertOrder(new OrderData("ДУДНИК", "54.985050928898104, 82.89371252068324", IconId.DUDNIK.getIcon()));
        dao.insertOrder(new OrderData("БУРГЕР КИНГ", "55.04162177716465, 82.91282626462706", IconId.BURGER.getIcon()));

        dao.insertOrder(new OrderData("ТОМЯМ БАР", "55.0298768288667, 82.93723510525126", IconId.TOMYAM.getIcon()));
        dao.insertOrder(new OrderData("ПЕКУ ПЕКУ", "55.05111537242559, 82.93920641008613", IconId.PEKU.getIcon()));
        dao.insertOrder(new OrderData("ХОТ ДОГ", "54.991995862500346, 82.85863831589572", IconId.HOTDOG.getIcon()));

        dao.insertFood(new FoodData("КОФЕ", IconId.KOFFEE.getIcon(), 10, 5));
        dao.insertFood(new FoodData("БАТОНЧИК", IconId.STICK.getIcon(), 30, 15));
        dao.insertFood(new FoodData("ЭНЕРГЕТИК", IconId.ENERGETIC.getIcon(), 30, 15));

        dao.insertTransport(new TransportData("МАШИНА", 150, IconId.BUS.getIcon(), 0.4d,"Прекрасный транспорт, комфорт на высоте, и личное пространство имеется. Но прийдется долго стоять в пробках ("));
        dao.insertTransport(new TransportData("ПЕШКОМ", 0, IconId.WALK.getIcon(), 1.0d,""));
        dao.insertTransport(new TransportData("ПРОЕЗДНОЙ", 50, IconId.METRO.getIcon(), 0.6d,"В тесноте, да не в обиде. Зато быстро и в социуме"));
        dao.insertTransport(new TransportData("ВЕЛОСИПЕД", 25 , IconId.BIKE.getIcon(), 0.85d,"Если ты поддерживаешь экологию, велосипед - твой выбор!"));
        dao.insertTransport(new TransportData("СКЕЙТ", 10, IconId.SKATE.getIcon(), 0.95d,"После работы сразу можешь заскочить к ребятам на дворе и покатать"));
    }

    //if user wants location using
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMainActivity();
            }
        }
    }
}
