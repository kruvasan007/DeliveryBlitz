package com.example.myprojectgame.ui.shop;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.example.myprojectgame.R;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;

public class ShopActivity extends BaseActivity {

    private static TextView textMoney, textHealth, textEx;
    private int currentState = 0;

    @SuppressLint("UseCompatLoadingForDrawables")
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
            if(currentState == 0)
                ShopAdapter.buyItem();
            else
                BuffsAdapter.buyTransport();
            textMoney.setText(String.valueOf(gameData.money));
            textHealth.setText(String.valueOf(gameData.health));
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> backActivity());

        LinearLayout linearLayout = findViewById(R.id.linear_button);
        ImageButton foodButton = linearLayout.findViewById(R.id.food_button);
        ImageButton transportButton = linearLayout.findViewById(R.id.transport_button);

        foodButton.setOnClickListener(view -> {
            foodButton.setBackground(getDrawable(R.drawable.selectable_button));
            transportButton.setBackground(getDrawable(R.drawable.food_style));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ItemContainer, new ShopFragment()).commit();
        });

        transportButton.setOnClickListener(view -> {
            foodButton.setBackground(getDrawable(R.drawable.food_style));
            transportButton.setBackground(getDrawable(R.drawable.selectable_button));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.ItemContainer, new BuffsFragment()).commit();
        });
    }

    private void backActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
