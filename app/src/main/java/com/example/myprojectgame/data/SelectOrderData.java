package com.example.myprojectgame.data;

public class SelectOrderData {

    public double k;
    public int costDelivery;
    public int earnFomOrder;
    public int addExp;
    public int currentProgress;
    public long currentTime;
    public int state;


    public SelectOrderData() {
        this(null, null, null, null,null,null);
    }

    public SelectOrderData(Integer progress, Long time,Integer exp, Integer earn,Integer cost,Integer state){
        if (progress == null) this.currentProgress = 0; else this.currentProgress = progress;
        if (time == null) this.currentTime = 100; else this.currentTime = time;
        if (cost == null) this.costDelivery = 0; else this.costDelivery = cost;
        if (exp == null) this.addExp = 0; else this.addExp = exp;
        if (earn == null) this.earnFomOrder = 0; else this.earnFomOrder = earn;
        if (state == null) this.state = 0; else this.state = state;

    }

}
