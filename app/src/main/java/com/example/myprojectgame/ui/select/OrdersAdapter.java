package com.example.myprojectgame.ui.select;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    public static List<Order> data;
    protected static Button lastBut;
    protected static LinkedList<Button> buttons = new LinkedList<>();
    public static Random random = new Random();
    public static List<Integer> color = new ArrayList<Integer>();


    public OrdersAdapter(List<Order> data) {
        buttons.clear();
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_button, parent, false);
        color.add(parent.getContext().getResources().getColor(R.color.gren));
        color.add(parent.getContext().getResources().getColor(R.color.red_white));
        color.add(parent.getContext().getResources().getColor(R.color.blue));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = (Button) itemView;
            button.setOnClickListener(v -> {
                button.setActivated(true);
                if (lastBut != null){
                    lastBut.setActivated(false);
                }
                lastBut = button;

                if (gameData.step == "0"){
                    ChooseOrderActivity.OrderChoose(data.get(buttons.indexOf(button)));
                }
                if (gameData.step == "1") {
                    ChooseOrderActivity.OrderTransport(data.get(buttons.indexOf(button)));
                }

            });
        }
        public void bind(Order item){
            button.setText(item.name);
            buttons.add(button);
            button.setBackgroundColor(color.get(random.nextInt(color.size())));
            Drawable myDrawable = button.getContext().getDrawable(Integer.parseInt(item.icon));
            button.setCompoundDrawablesWithIntrinsicBounds(null,myDrawable,null,null);
        }
    }
}
