package com.example.myprojectgame.ui;

import android.app.Application;

import androidx.room.Room;

import com.example.myprojectgame.db.AppDatabase;

public class App extends Application {
    private static App instance;
    private AppDatabase instanceDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AppDatabase getAppDatabaseInstance() {
        instance.instanceDatabase = Room.databaseBuilder(
                    instance.getApplicationContext(),
                    AppDatabase.class,
                    "mydatabase.db"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build();
        return instance.instanceDatabase;
    }
}
