package com.example.myprojectgame;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class OrderData {
    private final TreeMap<String,String> treeMap = new TreeMap<>();

    public OrderData(String name, String  id) {
        this.treeMap.put("Name",name);
        this.treeMap.put("Icon",id);
    }
    public OrderData(String name, String id, String location){
        this(name,id);
        this.treeMap.put("Location", location);
    }

    public ArrayList getLocation(){
        ArrayList<Float> locations = new ArrayList<Float>();
        String[] stringLocations = treeMap.get("Location").split(",");
        locations.add(Float.parseFloat(stringLocations[0]));
        locations.add(Float.parseFloat(stringLocations[1]));
        return locations;
    }
    public String getName() {
        return treeMap.get("Name");
    }
    public String getId(){ return treeMap.get("Icon");}
}
