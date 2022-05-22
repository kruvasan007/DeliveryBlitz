package com.example.myprojectgame.data;

import java.util.ArrayList;
import java.util.List;

public class SelectOrderData {

    public double k;
    public int costDelivery;
    public int earnFomOrder;
    public int addExp;
    public List<Double> selectCurrentCoord;
    public String name;
    public int currentProgress;
    public long currentTime;
    public int state;
    public long lastTime;


    public SelectOrderData() {
        this(null, null, null, null,null,null,null,null,null);
    }

    public SelectOrderData(Integer progress, Long time,Integer exp, Integer earn,Integer cost,Integer state, List<Double> coord,String name,Long lastTime){
        if (progress == null) this.currentProgress = 0; else this.currentProgress = progress;
        if (time == null) this.currentTime = 100; else this.currentTime = time;
        if (cost == null) this.costDelivery = 0; else this.costDelivery = cost;
        if (exp == null) this.addExp = 0; else this.addExp = exp;
        if (earn == null) this.earnFomOrder = 0; else this.earnFomOrder = earn;
        if (state == null) this.state = 0; else this.state = state;
        if (name == null) this.name = ""; else this.name = name;
        if (lastTime == null) this.lastTime = 0; else this.lastTime = lastTime;
        if (selectCurrentCoord == null) { ArrayList<Double> arrayList = new ArrayList<>();
        } else {
            this.selectCurrentCoord = coord;
        }
    }

}
