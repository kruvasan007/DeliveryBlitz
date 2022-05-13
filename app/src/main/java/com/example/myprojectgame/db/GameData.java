package com.example.myprojectgame.db;

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
    public List<Double> gamerCoord;
    public int state;
    public long time;

    public GameData(int money, int health, int exp, List<Double> coord) {
        this.money = money;
        this.health = health;
        this.k = 0.0;
        this.exp = exp;
        this.transport = "";
        gamerCoord = coord;
        this.step = "0";
        this.state = 0;
        this.order = "";
        this.time = 0;
    }

}
