package com.example.myprojectgame.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrderData {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String coordinates;
    public int icon;
    public int cost;
    public int exp;

    public OrderData(String name, String coordinates, int icon, int cost, int exp) {
        this.name = name;
        this.icon = icon;
        this.coordinates = coordinates;
        this.cost = cost;
        this.exp = exp;
    }

    public Order fromOrderstoOrder(){
        return new Order(name,coordinates,icon,cost,exp);
    }
    public String getCoordinates() { return this.coordinates;}
}
