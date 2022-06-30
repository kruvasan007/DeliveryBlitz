package com.example.myprojectgame.ui.root;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;
import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myprojectgame.domain.PreferencesManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferencesManager pm = new PreferencesManager(this);
        pm.setSelectOrderData(selectOrderData);
        pm.setGameData(gameData);
        pm.close();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        PreferencesManager pm = new PreferencesManager(this);
        gameData = pm.getGameData();
        selectOrderData = pm.getSelectableOrderData();
        pm.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
