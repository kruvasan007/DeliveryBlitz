package com.example.myprojectgame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonStart = findViewById(R.id.select_button);
        buttonStart.setOnClickListener(v -> startButton());
    }

    private void startButton(){
            Intent intent = new Intent(MainActivity.this, ChooseOrderActivity.class);
            startActivity(intent);
            finish();
    }
}