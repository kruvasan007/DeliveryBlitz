package com.example.myprojectgame.ui.root;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myprojectgame.R;
import com.example.myprojectgame.data.GameData;
import com.example.myprojectgame.data.IconId;
import com.example.myprojectgame.data.SelectOrderData;
import com.example.myprojectgame.db.FoodData;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.db.TransportData;
import com.example.myprojectgame.domain.PreferencesManager;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.delivery.DeliveryActivity;
import com.example.myprojectgame.ui.click.ClickerActivity;
import com.example.myprojectgame.ui.shop.ShopActivity;

public class MainActivity extends BaseActivity {
    private OrderDao dao;

    private TextView textMoney, textHealth, textEx;
    public static GameData gameData;
    private PreferencesManager pm;
    public static SelectOrderData selectOrderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pm = new PreferencesManager(this);
        createGameData();
        createSelectOrderData();
        getIntentionLastActivity();
        pm.close();

        setContentView(R.layout.activity_main);

        Button buttonStart = findViewById(R.id.select_button);
        buttonStart.setOnClickListener(v -> startButton());

        ImageButton buttonShop = findViewById(R.id.shop_button);
        buttonShop.setOnClickListener(v -> shopButton());

        textMoney = findViewById(R.id.money);
        textHealth = findViewById(R.id.health);
        textEx = findViewById(R.id.exp);
        textMoney.setText(String.valueOf(gameData.money));
        textHealth.setText(String.valueOf(gameData.health));
        textEx.setText(String.valueOf(gameData.exp));
    }

    private void shopButton() {
        Intent intent = new Intent(MainActivity.this, ShopActivity.class);
        startActivity(intent);
        finish();
    }

    private void createGameData() {
        if (pm.getFirstRun()) firstRunDataBase();
        gameData = pm.getGameData();
    }

    private void createSelectOrderData() {
        selectOrderData = pm.getSelectableOrderData();
    }

    private void healthAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation);
        animation.reset();
        textHealth.setText("" + gameData.health);
        textHealth.clearAnimation();
        textHealth.startAnimation(animation);
    }

    private void getIntentionLastActivity() {
        if (selectOrderData.state == 1) {
            startActivity(new Intent(this, ClickerActivity.class));
            finish();
        }
    }

    private void startButton() {
        if (gameData.health > 10) {
            Intent intent = new Intent(MainActivity.this, DeliveryActivity.class);
            startActivity(intent);
            finish();
        } else Toast.makeText(this, "У вас недостаточно жизней", Toast.LENGTH_SHORT).show();
    }

    private void firstRunDataBase() {
        dao = App.getAppDatabaseInstance().orderDao();
        dao.insertOrder(new OrderData("МАК ДАК", "55.03963005768076, 82.96122777648965", IconId.MCDONALDS.getIcon()));
        dao.insertOrder(new OrderData("МАК ДАК", "55.0106521380292, 82.93736307619466", IconId.MCDONALDS.getIcon()));
        dao.insertOrder(new OrderData("МАК ДАК", "55.04477301431026, 82.92285248440155", IconId.MCDONALDS.getIcon()));
        dao.insertOrder(new OrderData("МАК ДАК", "54.96414597832322, 82.93641956376621", IconId.MCDONALDS.getIcon()));
        dao.insertOrder(new OrderData("МАК ДАК", "55.04429278979794, 82.95093933736995", IconId.MCDONALDS.getIcon()));

        dao.insertOrder(new OrderData("KФC", "54.989384589520384, 82.9079604150277", IconId.KFC.getIcon()));
        dao.insertOrder(new OrderData("KФC", "54.985050928898104, 82.89371252068324", IconId.KFC.getIcon()));
        dao.insertOrder(new OrderData("KФC", "55.04162177716465, 82.91282626462706", IconId.KFC.getIcon()));

        dao.insertOrder(new OrderData("ТОМЯМ БАР", "55.0298768288667, 82.93723510525126", IconId.TOMYAM.getIcon()));
        dao.insertOrder(new OrderData("ТОМЯМ БАР", "55.05111537242559, 82.93920641008613", IconId.TOMYAM.getIcon()));
        dao.insertOrder(new OrderData("ТОМЯМ БАР", "54.991995862500346, 82.85863831589572", IconId.TOMYAM.getIcon()));

        dao.insertFood(new FoodData("КОФЕ", IconId.KOFFEE.getIcon(), 10, 5));
        dao.insertFood(new FoodData("БАТОНЧИК", IconId.STICK.getIcon(), 30, 15));
        dao.insertTransport(new TransportData("АВТОБУС", 50, IconId.BUS.getIcon(), 0.7d));
        dao.insertTransport(new TransportData("ПЕШКОМ", 0, IconId.WALK.getIcon(), 1.0d));
        dao.insertTransport(new TransportData("МЕТРО", 100, IconId.METRO.getIcon(), 0.4d));
        pm.setFirstRun();
    }

}