package com.example.myprojectgame.db;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    public long id;

    public double k;
    public int money;
    public int health;
    public int exp;
    public int cost;
    public int earn;
    public String transport;
    public String step;
    public String order;
    public List<Float> gamerCoord;

    public GameData(int money, int health, int exp) {
        this.money = money;
        this.health = health;
        this.k = 0.0;
        this.exp = exp;
        this.transport = "";
        gamerCoord = new ArrayList<Float>();
        gamerCoord.add(54.991225577868676f);
        gamerCoord.add(82.8972068018308f);
        this.step = "0";
        this.order = "";
    }

}
