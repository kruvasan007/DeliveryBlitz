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

    public OrderData(String name, String coordinates, int icon) {
        this.name = name;
        this.icon = icon;
        this.coordinates = coordinates;
    }

    public String getCoordinates() { return this.coordinates;}
}
