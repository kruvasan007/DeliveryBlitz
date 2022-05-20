package com.example.myprojectgame.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {OrderData.class, TransportData.class, FoodData.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract OrderDao orderDao();
}