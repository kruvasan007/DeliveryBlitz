package com.example.myprojectgame;

import java.util.ArrayList;

public class GameData {
    public int currentMoney, currentHealth, currentExp;
    public String transport;
    public static ArrayList<Float> order; //FIX FIX FIX FIX FIX FIX FIX FIX


    public GameData(int money, int health, int exp){
        this.currentExp = exp;
        this.currentHealth = health;
        this.currentMoney = money;
    }
}
