package com.example.myprojectgame;

import android.graphics.drawable.Drawable;

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

    public String getName() {
        return treeMap.get("Name");
    }
    public String getId(){ return treeMap.get("Icon");}
}
