package com.example.myprojectgame.ui.delivery;

import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.select.ChooseOrderActivity;

import java.util.concurrent.TimeUnit;

public class DeliveryFragment extends Fragment {
    private OrderDao dao;

    public DeliveryFragment() {
    }

    public void nextActivity() {
        startActivity(new Intent(requireActivity(), ChooseOrderActivity.class));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_order, container, false);
        dao = App.getAppDatabaseInstance().orderDao();
        TextView earn = view.findViewById(R.id.earn);
        TextView exp = view.findViewById(R.id.exp);
        TextView name = view.findViewById(R.id.name);
        TextView time = view.findViewById(R.id.time);

        Button nextButton = view.findViewById(R.id.next_step_button);
        nextButton.setOnClickListener(v -> nextActivity());

        ImageView icon = view.findViewById(R.id.icon);
        @SuppressLint("DefaultLocale") String stringTime = String.format("%02d:%02d",
                TimeUnit.SECONDS.toMinutes(selectOrderData.currentTime),
                TimeUnit.SECONDS.toSeconds(selectOrderData.currentTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(selectOrderData.currentTime))
        );
        time.setText(stringTime+" мин.");
        name.setText(selectOrderData.name);
        exp.setText(selectOrderData.addExp+" xp");
        earn.setText(selectOrderData.earnFomOrder+" руб.");
        Drawable myDrawable = AppCompatResources.getDrawable(view.getContext(),dao.getByName(selectOrderData.name).get(0).icon);
        icon.setImageDrawable(myDrawable);
        return view;
    }

}
