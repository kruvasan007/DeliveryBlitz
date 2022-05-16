package com.example.myprojectgame.ui.click;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myprojectgame.R;

public class MyDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment,container,false);
        Button closeButton = view.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> {
            dismiss();
        });
        return view;
    }
}
