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
import com.example.myprojectgame.data.SelectOrderData;
import com.example.myprojectgame.db.FoodData;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.db.TransportData;
import com.example.myprojectgame.domain.PreferencesManager;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.Delivery.DeliveryActivity;
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
        textHealth.setText(""+gameData.health);
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
        dao.insertOrder(new OrderData("МАК ДАК", "55.02926001110971, 82.93647034818923", R.drawable.mcdonalds, 25, 10));
        dao.insertOrder(new OrderData("МАК ДАК", "55.03963005768076, 82.96122777648965", R.drawable.mcdonalds, 10, 5));
        dao.insertOrder(new OrderData("МАК ДАК", "55.04477301431026, 82.92285248440155", R.drawable.mcdonalds, 40, 30));
        dao.insertOrder(new OrderData("МАК ДАК", "55.0106521380292, 82.93736307619466", R.drawable.mcdonalds, 99, 110));
        dao.insertOrder(new OrderData("KФC", "54.989384589520384, 82.9079604150277", R.drawable.kfc, 50, 45));
        dao.insertOrder(new OrderData("KФC", "54.985050928898104, 82.89371252068324", R.drawable.kfc, 36, 30));
        dao.insertOrder(new OrderData("KФC", "55.04162177716465, 82.91282626462706", R.drawable.kfc, 40, 5));
        dao.insertFood(new FoodData("КОФЕ", R.drawable.koffe, 10, 5));
        dao.insertFood(new FoodData("БАТОНЧИК", R.drawable.stick, 30, 15));
        dao.insertTransport(new TransportData("АВТОБУС", 50, R.drawable.bus, 0.7d));
        dao.insertTransport(new TransportData("ПЕШКОМ", 0, R.drawable.walking, 1.0d));
        dao.insertTransport(new TransportData("МЕТРО", 100, R.drawable.train, 0.4d));
        pm.setFirstRun();
    }

}