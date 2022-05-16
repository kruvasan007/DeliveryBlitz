package com.example.myprojectgame.ui.select;

import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.Order;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    public static List<Order> data;
    protected static ConstraintLayout lastBut;
    protected static LinkedList<ConstraintLayout> cards = new LinkedList<>();
    public static List<Integer> color = new ArrayList<Integer>();


    public OrdersAdapter(List<Order> data) {
        cards.clear();
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
        private final ConstraintLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = (ConstraintLayout) itemView;
            card.setOnClickListener(v -> {
                card.setBackground(card.getContext().getDrawable(R.drawable.not_activated_constraint_style));
                if (lastBut != null && lastBut != card) {
                    lastBut.setBackground(card.getContext().getDrawable(R.drawable.constraint_style));
                }
                lastBut = card;
                ChooseOrderActivity.OrderTransport(data.get(cards.indexOf(card)));
            });
        }

        public void bind(Order item) {
            TextView name = card.findViewById(R.id.name);
            TextView earn = card.findViewById(R.id.earn);
            TextView time = card.findViewById(R.id.time);
            ImageView icon = card.findViewById(R.id.icon);
            name.setText(item.name);
            cards.add(card);
            card.setClipToOutline(true);
            earn.setText(item.costs + " руб.");
            long orderTime = (long) (selectOrderData.currentTime/(2 - item.k));
            @SuppressLint("DefaultLocale") String stringTime = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(orderTime),
                    TimeUnit.SECONDS.toSeconds(orderTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(orderTime))
            );
            time.setText(stringTime + " мин.");
            Drawable myDrawable = card.getContext().getDrawable(Integer.parseInt(item.icon));
            icon.setImageDrawable(myDrawable);
            card.setBackground(card.getContext().getDrawable(R.drawable.constraint_style));
        }
    }
}
