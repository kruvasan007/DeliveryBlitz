package com.example.myprojectgame.db;

public class SelectOrderData {

    public double k;
    public int costDelivery;
    public int earnFomOrder;
    public int addExp;
    public int currentProgress;
    public long currentTime;

    public SelectOrderData(int progress, long time,int exp, int earn,int cost){
        this.addExp = exp;
        this.costDelivery = cost;
        this.currentTime = time;
        this.earnFomOrder = earn;
        this.currentProgress = progress;
    }
}
