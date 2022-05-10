package com.example.myprojectgame.db;

public class Order {
    public long id;

    public String name;
    public String coordinates;
    public String icon;
    public Double k;
    public int costs;

    public Order(String name, String coordinates, String icon, int costs) {
        this.name = name;
        this.icon = icon;
        this.coordinates = coordinates;
        this.costs = costs;
    }
    public Order(String name, String coordinates, String icon, int costs, Double k) {
        this(name,coordinates,icon,costs);
        this.k = k;
    }
}
