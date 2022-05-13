package com.example.myprojectgame.ui.root;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("money", gameData.money);
        myEdit.putInt("health", gameData.health);
        myEdit.putInt("exp", gameData.exp);
        myEdit.putInt("state",gameData.state);
        myEdit.putLong("time",gameData.time);
        myEdit.putString("coord",gameData.gamerCoord.get(0).toString()+" "+gameData.gamerCoord.get(1).toString());
        myEdit.putString("lastActivity", getClass().getName());
        myEdit.apply();
    }
}
