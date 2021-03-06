package com.example.myprojectgame.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDao {

    @Query("SELECT * FROM OrderData")
    List<OrderData> selectOrder();

    @Query("SELECT * FROM TransportData")
    List<TransportData> selectTransport();

    @Query("SELECT * FROM FoodData")
    List<FoodData> selectFood();

    @Query("SELECT * FROM OrderData WHERE name = :selectName")
    List<OrderData> getByName(String selectName);

    @Query("SELECT * FROM FoodData WHERE name = :selectName")
    FoodData getByNameFood(String selectName);

    @Query("UPDATE Orderdata SET coordinates = :cords WHERE id = :tid")
    void updateCordinates(long tid, String cords);

    @Insert
    long insertOrder(OrderData orderData);

    @Insert
    long insertTransport(TransportData transportData);

    @Insert
    long insertFood(FoodData foodData);

    @Update
    void update(OrderData orderData);

    @Delete
    void delete(OrderData orderData);
}
