package com.example.myprojectgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChooseOrderActivity extends BaseActivity {
    private static TextView textView;
    private RecyclerView recyclerLayout;
    public static int step, idTransport, idOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new OrderFragment()).commit();
        step = 0;
        recyclerLayout = findViewById(R.id.recycler_layout);
        textView = findViewById(R.id.textView);
        Button buttonClose = findViewById(R.id.back_button);
        Button buttonNext = findViewById(R.id.next_step_button);

        buttonNext.setOnClickListener(v -> {
            if (step == 0) {
                buttonNext.setText("Подтвердить транспорт");
                nextStep();
            } else nextActivity();
        });

        buttonClose.setOnClickListener(v -> closeButton());
    }

    private void nextActivity() {
            Intent intent = new Intent(ChooseOrderActivity.this, DeliveryActivity.class);
            startActivity(intent);
            finish();
    }


    public static void OrderChoose(CharSequence text, ArrayList loc) {
        textView.setText("Выбран заказ: " + text);
        GameData.order = loc;
    }

    public static void OrderTransport(CharSequence text) {
        textView.setText("Выбран транспорт: " + text);
    }

    private void nextStep() {
        textView.setText("");
        step++;
        getSupportFragmentManager().beginTransaction()
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