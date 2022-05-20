package com.example.myprojectgame.ui.choose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.TransportData;
import com.example.myprojectgame.ui.App;

import java.util.ArrayList;
import java.util.List;

public class СhooseTransportFragment extends Fragment {
    private OrderDao dao;
    private ChooseTransportAdapter adapter;
    private List<TransportData> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_layout);
        data = new ArrayList<>();
        getTransportData();
        adapter = new ChooseTransportAdapter(data);
        recyclerView.setAdapter(adapter);
    }

    private void getTransportData() {
        dao = App.getAppDatabaseInstance().orderDao();
        data = dao.selectTransport();
    }
}
