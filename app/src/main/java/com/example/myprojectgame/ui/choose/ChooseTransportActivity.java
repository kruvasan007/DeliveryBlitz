package com.example.myprojectgame.ui.choose;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;
import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.OrderDao;
import com.example.myprojectgame.db.TransportData;
import com.example.myprojectgame.ui.App;
import com.example.myprojectgame.ui.delivery.DeliveryActivity;
import com.example.myprojectgame.ui.click.ClickerActivity;
import com.example.myprojectgame.ui.root.BaseActivity;

public class ChooseTransportActivity extends BaseActivity {
    private OrderDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        dao = App.getAppDatabaseInstance().orderDao();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.MainContainer, new ChooseTransportFragment()).commit();

        RecyclerView recyclerLayout = findViewById(R.id.recycler_layout);
        TextView textView = findViewById(R.id.textView);
        Button buttonClose = findViewById(R.id.back_button);
        Button buttonNext = findViewById(R.id.next_step_button);

        buttonNext.setOnClickListener(v -> {
                if (selectOrderData.k != 0) {
                    if ( gameData.money - selectOrderData.costDelivery >= 0){
                        gameData.gamerCoord = selectOrderData.selectCurrentCoord;
                        if(gameData.doneOrder.size() < 10)
                            gameData.doneOrder.add(selectOrderData.name);
                        selectOrderData.currentTime = (long) (selectOrderData.currentTime/(2-selectOrderData.k));
                        nextActivity();
                    }
                    else makeToastSize("У вас недостаточно средств");
                } else makeToastSize("Пожалуйста,выберите транспорт");
            });

        buttonClose.setOnClickListener(v -> closeButton());
    }
    public static void OrderTransport(TransportData data) {
        if (data.payment) {
            selectOrderData.costDelivery = data.costs;
        } else selectOrderData.costDelivery = 0;
        selectOrderData.k = data.k;
    }

    private void closeButton() {
        Intent intent = new Intent(ChooseTransportActivity.this, DeliveryActivity.class);
        startActivity(intent);
        finish();
    }

    private void nextActivity() {
        Intent intent = new Intent(ChooseTransportActivity.this, ClickerActivity.class);
        startActivity(intent);
        finish();
    }

    private void makeToastSize(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (LinearLayout) findViewById(R.id.toast_layout));
        TextView head = layout.findViewById(R.id.head);
        TextView description = layout.findViewById(R.id.descript);
        head.setText("Ошибка");
        head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.heart), null, null, null);
        description.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}