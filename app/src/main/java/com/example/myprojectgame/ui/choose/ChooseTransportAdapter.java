package com.example.myprojectgame.ui.choose;

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
import com.example.myprojectgame.db.TransportData;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChooseTransportAdapter extends RecyclerView.Adapter<ChooseTransportAdapter.ViewHolder> {
    public static List<TransportData> data;
    protected static ConstraintLayout lastBut;
    protected static LinkedList<ConstraintLayout> cards = new LinkedList<>();
    public static List<Integer> color = new ArrayList<Integer>();


    public ChooseTransportAdapter(List<TransportData> data) {
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
                ChooseTransportActivity.OrderTransport(data.get(cards.indexOf(card)));
            });
        }

        public void bind(TransportData item) {
            TextView name = card.findViewById(R.id.name);
            TextView earn = card.findViewById(R.id.earn);
            TextView time = card.findViewById(R.id.time);
            ImageView icon = card.findViewById(R.id.icon);
            name.setText(item.name);
            cards.add(card);
            card.setClipToOutline(true);
            if(item.payment) {
                earn.setText(item.costs + " ??????.");
            }
            else earn.setText("??????????????????");
            long orderTime = (long) (selectOrderData.currentTime/(2 - item.k));
            String stringTime = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(orderTime),
                    TimeUnit.SECONDS.toSeconds(orderTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(orderTime))
            );
            time.setText(stringTime + " ??????.");
            Drawable myDrawable = card.getContext().getDrawable(item.icon);
            icon.setImageDrawable(myDrawable);
            card.setBackground(card.getContext().getDrawable(R.drawable.constraint_style));
        }
    }
}
