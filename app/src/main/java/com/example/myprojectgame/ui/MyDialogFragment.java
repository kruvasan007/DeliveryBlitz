package com.example.myprojectgame.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myprojectgame.R;

public class MyDialogFragment extends DialogFragment {
    private  String message, head;
    private Integer deafault = R.layout.dialog_fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(deafault,container,false);
        if (deafault == R.layout.achivment_dialog){
            TextView textIcon = view.findViewById(R.id.money);
            TextView textDescr = view.findViewById(R.id.message);
            textIcon.setText("+100");
            textDescr.setText(message);
        }
        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            dismiss();
        });
        return view;
    }

    public void setValue(String msg, String hd,Integer def){
        this.message = msg;
        this.head = hd;
        this.deafault = def;
    }
}
