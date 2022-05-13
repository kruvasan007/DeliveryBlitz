package com.example.myprojectgame.db;

public class Order {
    public long id;

    public String name;
    public String coordinates;
    public String icon;
    public Double k;
    public int costs;
    public int exp;

    public Order(String name, String coordinates, String icon, int costs,int exp) {
        this.name = name;
        this.icon = icon;
        this.coordinates = coordinates;
        this.costs = costs;
        this.exp = exp;
    }
    public Order(String name, String coordinates, String icon, int costs, int exp, Double k) {
        this(name,coordinates,icon,costs,exp);
        this.k = k;
    }
}
