package com.example.myprojectgame;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_layout);
        OrdersAdapter adapter = new OrdersAdapter(getData());
        recyclerView.setAdapter(adapter);
    }

    private List<OrderData> getData() {
        List<OrderData> data = new ArrayList<>();
        if ( ChooseOrderActivity.step == 0) {
            data.add(new OrderData("1", "" + R.drawable.mcdonalds));
            data.add(new OrderData("2", "" + R.drawable.mcdonalds));
            data.add(new OrderData("3", "" + R.drawable.mcdonalds));
            data.add(new OrderData("4", "" + R.drawable.mcdonalds));

            data.add(new OrderData("5", "" + R.drawable.mcdonalds));
            data.add(new OrderData("6", "" + R.drawable.mcdonalds));
            data.add(new OrderData("7", "" + R.drawable.mcdonalds));
            data.add(new OrderData("8", "" + R.drawable.mcdonalds));
        }
        else {
            data.add(new OrderData("1", "" + R.drawable.bus));
            data.add(new OrderData("2", "" + R.drawable.bus));
            data.add(new OrderData("3", "" + R.drawable.bus));
            data.add(new OrderData("4", "" + R.drawable.bus));
        }
        return data;
    }
}
