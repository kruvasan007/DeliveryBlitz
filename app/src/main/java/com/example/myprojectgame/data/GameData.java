package com.example.myprojectgame.data;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    public int money;
    public int health;
    public int exp;
    public List<Double> gamerCoord;
    public List<String> enableTransport;
    public List<String> doneOrder;

    public GameData() {
        this(null, null, null, null,null,null);
    }

    public GameData(Integer money, Integer health, Integer exp, List<Double> coord, List<String> transp, List<String> dO) {
        if (money == null) this.money = 100; else this.money = money;
        if (health == null) this.health = 100; else this.health = health;
        if (exp == null) this.exp = 0; else this.exp = exp;
        if (coord == null) {
            ArrayList<Double> arrayList = new ArrayList<>();
            arrayList.add(54.991225577868676);
            arrayList.add(82.8972068018308);
            this.gamerCoord = arrayList;
        } else {
            this.gamerCoord = coord;
        }
        if(transp == null){
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("ПЕШКОМ");
            this.enableTransport = arrayList;
        } else{
            this.enableTransport = transp;
        }
        if(dO == null){
            this.doneOrder = new ArrayList<String>();
        } else{
            this.doneOrder = dO;
        }
    }
}
