package com.example.myprojectgame.ui.shop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.FoodData;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.ui.App;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {

    private List<FoodData> data;
    private ShopAdapter adapter;
    private OrderDao dao;
    private LinearLayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_layout);
        dao = App.getAppDatabaseInstance().orderDao();
        data = new ArrayList<>();
        for (FoodData food: dao.selectFood()) data.add(food);
        adapter = new ShopAdapter(data);
        recyclerView.setAdapter(adapter);
    }
}
