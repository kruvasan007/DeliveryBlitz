package com.example.myprojectgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChooseOrderActivity extends BaseActivity {
    private static TextView textView;
    private RecyclerView recyclerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        recyclerLayout = findViewById(R.id.recycler_layout);
        textView = findViewById(R.id.textView);
        Button buttonClose = findViewById(R.id.back_button);
        Button buttonNext = findViewById(R.id.next_step_button);
        OrdersAdapter adapter = new OrdersAdapter(getData());
        recyclerLayout.setAdapter(adapter);


        buttonClose.setOnClickListener(v -> closeButton());
        buttonNext.setOnClickListener(v -> nextButton());
    }


    public static void OrderChoose(CharSequence text){
        textView.setText(text);
    }

    private void nextButton() {
        try {
            Intent intent = new Intent(ChooseOrderActivity.this, ChooseTransportActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e("E","Error");
        }
    }

    private List<OrderData> getData() {
        List<OrderData> data = new ArrayList<>();
        data.add(new OrderData("Заказ номер 1"));
        data.add(new OrderData("1234"));
        data.add(new OrderData("1235"));
        data.add(new OrderData("1236"));
        return data;
    }

    private void closeButton(){
        try {
            Intent intent = new Intent(ChooseOrderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e("E","Error");
        }
    }
}