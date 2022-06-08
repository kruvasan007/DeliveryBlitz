package com.example.myprojectgame.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public
class TransportData {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public int costs;
    public int icon;
    public Double k;
    public String description;

    public TransportData(String name, int costs, int icon, Double k, String description) {
        this.name = name;
        this.icon = icon;
        this.costs = costs;
        this.k = k;
        this.description = description;
    }
}
