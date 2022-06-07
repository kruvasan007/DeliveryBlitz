package com.example.myprojectgame.domain;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myprojectgame.data.GameData;
import com.example.myprojectgame.data.SelectOrderData;
import com.google.gson.Gson;

import java.io.Closeable;

public class PreferencesManager implements Closeable {
    private final static String PREFS_NAME = "PREFS";
    private final static String PREF_GAME_DATA = "gameData";
    private final static String PREF_SELECT_ORDER_DATA = "orderData";

    private SharedPreferences prefs;

    public PreferencesManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public Boolean getFirstRun() {
        return prefs.getBoolean("firstrun", true);
    }

    public void setFirstRun() {
        prefs.edit().putBoolean("firstrun", false).apply();
    }

    public Boolean getEnableLocationChoose() {
        return prefs.getBoolean("location", false);
    }

    public void setEnableLocationChoose(boolean it) {
        prefs.edit().putBoolean("location", it).apply();
    }

    public Integer getLastLogIn(){ return  prefs.getInt("lastDay", -3); }

    public void setLastLogIn(int it) { prefs.edit().putInt("lastDay", it).apply(); }

    public GameData getGameData() {
        return new Gson().fromJson(prefs.getString(PREF_GAME_DATA, "{}"), GameData.class);
    }

    public void setGameData(GameData gameData) {
        prefs.edit().putString(PREF_GAME_DATA, new Gson().toJson(gameData)).apply();
    }

    public SelectOrderData getSelectableOrderData() {
        return new Gson().fromJson(prefs.getString(PREF_SELECT_ORDER_DATA, "{}"), SelectOrderData.class);
    }

    public void setSelectOrderData(SelectOrderData selectOrderData) {
        prefs.edit().putString(PREF_SELECT_ORDER_DATA, new Gson().toJson(selectOrderData)).apply();
    }

    @Override
    public void close() {
        prefs = null;
    }
}
