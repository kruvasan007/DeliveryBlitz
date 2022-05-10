package com.example.myprojectgame.ui.select;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.Order;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.DeliveryActivity;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;

public class ChooseOrderActivity extends BaseActivity {
    private static TextView textView;
    private RecyclerView recyclerLayout;

    private OrderDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        dao = App.getAppDatabaseInstance().orderDao();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new OrderFragment()).commit();
        recyclerLayout = findViewById(R.id.recycler_layout);
        textView = findViewById(R.id.textView);
        Button buttonClose = findViewById(R.id.back_button);
        Button buttonNext = findViewById(R.id.next_step_button);

        buttonNext.setOnClickListener(v -> {
            if (gameData.step.equals("0")) {
                if (!gameData.order.equals("")) {
                    buttonNext.setText("Подтвердить транспорт");
                    gameData.step = "1";
                    nextStep();
                } else Toast.makeText(this, "Пожалуйста,выберите заказ", Toast.LENGTH_SHORT).show();

            } else {
                gameData.money -= gameData.cost;
                nextActivity();
            }
        });

        buttonClose.setOnClickListener(v -> closeButton());
    }

    private void nextActivity() {
        Intent intent = new Intent(ChooseOrderActivity.this, DeliveryActivity.class);
        startActivity(intent);
        finish();
    }


    public static void OrderChoose(Order data) {
        textView.setText("Вы заработаете: " + data.costs);
        gameData.order = data.name;
        gameData.earn = data.costs;
    }

    public static void OrderTransport(Order data) {
        textView.setText("Стоимость проезда: " + data.costs);
        gameData.transport = data.name;
        gameData.cost = data.costs;
        gameData.k = data.k;
    }

    private void nextStep() {
        textView.setText("");
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .replace(R.id.MainContainer, new OrderFragment()).commit();
    }

    private void closeButton() {
        try {
            Intent intent = new Intent(ChooseOrderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e("E", "Error");
        }
    }

}