package com.example.myprojectgame.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TransportData {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public int costs;
    public String icon;
    public Double k;

    public TransportData(String name, int costs, String icon, Double k) {
        this.name = name;
        this.icon = icon;
        this.costs = costs;
        this.k = k;
    }

    public Order fromTransportToOrder(){
        return new Order(name,"none",icon, costs,k);
    }

}
