package com.example.myprojectgame.db;

import java.util.List;

public class GameData {
    public int money;
    public int health;
    public int exp;
    public List<Double> gamerCoord;

    public GameData(int money, int health, int exp, List<Double> coord) {
        this.money = money;
        this.health = health;
        this.exp = exp;
        gamerCoord = coord;
    }

}
