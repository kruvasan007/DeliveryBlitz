package com.example.myprojectgame.ui.select;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;
import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.Order;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.Delivery.DeliveryActivity;
import com.example.myprojectgame.ui.click.ClickerActivity;
import com.example.myprojectgame.ui.root.BaseActivity;

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
                if (selectOrderData.k != 0) {
                    if ( gameData.money - selectOrderData.costDelivery >= 0){
                        gameData.health -= 10;
                        selectOrderData.currentTime = (long) (selectOrderData.currentTime/(2-selectOrderData.k));
                        nextActivity();
                    }
                    else makeToastSize("У вас недостаточно средств");
                } else makeToastSize("Пожалуйста,выберите транспорт");
            });

        buttonClose.setOnClickListener(v -> closeButton());
    }
    public static void OrderTransport(Order data) {
        selectOrderData.costDelivery = data.costs;
        selectOrderData.k = data.k;
    }

    private void closeButton() {
        Intent intent = new Intent(ChooseOrderActivity.this, DeliveryActivity.class);
        startActivity(intent);
        finish();
    }

    private void nextActivity() {
        Intent intent = new Intent(ChooseOrderActivity.this, ClickerActivity.class);
        startActivity(intent);
        finish();
    }

    private void makeToastSize(String t) {
        String text = t;
        SpannableStringBuilder biggerText = new SpannableStringBuilder(text);
        biggerText.setSpan(new RelativeSizeSpan(0.7f), 0, text.length(), 0);
        Toast.makeText(this, biggerText, Toast.LENGTH_LONG).show();
    }
}