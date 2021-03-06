package com.example.myprojectgame.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FoodData {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public int icon;
    public int cost;
    public int health;

    public FoodData(String name, int icon, int cost, int health) {
        this.name = name;
        this.icon = icon;
        this.cost = cost;
        this.health = health;
    }
}
