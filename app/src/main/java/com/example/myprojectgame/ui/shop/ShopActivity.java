package com.example.myprojectgame.ui.shop;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myprojectgame.R;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;

public class ShopActivity extends BaseActivity {

    private static TextView textMoney, textHealth, textEx;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        textMoney = findViewById(R.id.money);
        textHealth = findViewById(R.id.health);
        textEx = findViewById(R.id.exp);
        textMoney.setText(String.valueOf(gameData.money));
        textHealth.setText(String.valueOf(gameData.health));
        textEx.setText(String.valueOf(gameData.exp));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.ItemContainer, new ShopFragment()).commit();

        Button buyButton = findViewById(R.id.buy_button);
        buyButton.setOnClickListener(v -> {
            ShopAdapter.buyItem();
            textMoney.setText(String.valueOf(gameData.money));
            textHealth.setText(String.valueOf(gameData.health));
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> backActivity());
    }

    private void backActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
