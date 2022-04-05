package com.example.myprojectgame;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    public static List<OrderData> data;
    protected static Button lastBut;
    protected static LinkedList<Button> buttons = new LinkedList<>();

    public OrdersAdapter(List<OrderData> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_button, parent, false);
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
                if (ChooseOrderActivity.step == 0){
                    ChooseOrderActivity.idOrder = button.getId();
                    ChooseOrderActivity.OrderChoose(button.getText(), data.get(Integer.parseInt((String) button.getText())).getLocation());
                }
                if (ChooseOrderActivity.step == 1) {
                    ChooseOrderActivity.idTransport = button.getId();
                    ChooseOrderActivity.OrderTransport(button.getText());
                }
            });
        }

        public void bind(OrderData item) {
            button.setText(item.getName());
            buttons.add(button);
            Drawable myDrawable = button.getContext().getDrawable(Integer.parseInt(item.getId()));
            button.setCompoundDrawablesWithIntrinsicBounds(null,myDrawable,null,null);
            //TODO: init item
        }
    }
}
