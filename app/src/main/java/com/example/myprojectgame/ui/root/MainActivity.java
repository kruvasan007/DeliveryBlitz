package com.example.myprojectgame.ui.root;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.GameData;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.OrderData;
import com.example.myprojectgame.db.TransportData;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.select.ChooseOrderActivity;

public class MainActivity extends BaseActivity {
    private OrderDao dao;
    private boolean firstStart = false;

    private static TextView textMoney;
    public static GameData gameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int money = sh.getInt("money", 100);
        int health = sh.getInt("health", 100);
        int exp = sh.getInt("exp", 0);
        gameData = new GameData(money,health,exp);
        setContentView(R.layout.activity_main);
        if (firstStart) {
            dao = App.getAppDatabaseInstance().orderDao();
            dao.insertOrder(new OrderData("MCDONALDS","55.02926001110971, 82.93647034818923",""+R.drawable.mcdonalds,25));
            dao.insertOrder(new OrderData("MCDONALDS","55.03963005768076, 82.96122777648965",""+R.drawable.mcdonalds,10));
            dao.insertOrder(new OrderData("MCDONALDS","55.04477301431026, 82.92285248440155",""+R.drawable.mcdonalds,40));
            dao.insertOrder(new OrderData("MCDONALDS","55.0106521380292, 82.93736307619466",""+R.drawable.mcdonalds,99));
            dao.insertOrder(new OrderData("KFC","54.989384589520384, 82.9079604150277",""+R.drawable.drumsticks,50));
            dao.insertOrder(new OrderData("KFC","54.985050928898104, 82.89371252068324",""+R.drawable.drumsticks,36));
            dao.insertOrder(new OrderData("KFC","55.04162177716465, 82.91282626462706",""+R.drawable.drumsticks,40));
            dao.insertTransport(new TransportData("BUS",50,""+R.drawable.bus,0.7d));
            dao.insertTransport(new TransportData("WALKING",0,""+R.drawable.walking,1.0d));
            dao.insertTransport(new TransportData("METRO",100,""+R.drawable.train,0.4d));
        }
        Button buttonStart = findViewById(R.id.select_button);
        buttonStart.setOnClickListener(v -> startButton());

        textMoney = findViewById(R.id.money);
        textMoney.setText(String.valueOf(gameData.money));
    }

    private void startButton(){
            Intent intent = new Intent(MainActivity.this, ChooseOrderActivity.class);
            startActivity(intent);
            finish();
    }
}