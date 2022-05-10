package com.example.myprojectgame.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrderData {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String coordinates;
    public String icon;
    public int cost;

    public OrderData(String name, String coordinates, String icon, int cost) {
        this.name = name;
        this.icon = icon;
        this.coordinates = coordinates;
        this.cost = cost;
    }

    public Order fromOrderstoOrder(){
        return new Order(name,coordinates,icon,cost);
    }
    public String getCoordinates() { return this.coordinates;}
}
