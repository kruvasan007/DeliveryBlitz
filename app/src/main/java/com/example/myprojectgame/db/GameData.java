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
    public int progress;

    public GameData(int money, int health, int exp, List<Double> coord, int prog, long t, int s) {
        this.money = money;
        this.health = health;
        this.k = 0.0;
        this.exp = exp;
        this.transport = "";
        gamerCoord = coord;
        this.step = "0";
        this.progress = prog;
        this.state = s;
        this.order = "";
        this.time = t;
    }

}
