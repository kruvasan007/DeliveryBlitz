package com.example.myprojectgame.ui.shop;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectgame.R;
import com.example.myprojectgame.db.TransportData;

import java.util.LinkedList;
import java.util.List;

public class BuffsAdapter extends RecyclerView.Adapter<BuffsAdapter.ViewHolder> {
    public static List<TransportData> data;
    protected static ConstraintLayout lastTransport;
    protected static LinkedList<ConstraintLayout> cards = new LinkedList<>();
    protected int isExpandable = -1;

    public BuffsAdapter(List<TransportData> data) {
        cards.clear();
        lastTransport = null;
        BuffsAdapter.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_buffs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(data.get(position));
        final boolean isExpanded = position== isExpandable;
        holder.extendLayout.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(v -> {
            lastTransport = holder.card;
            isExpandable = isExpanded ? -1:position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static Integer buyTransport() {
        if (lastTransport != null) {
            TransportData dataf = data.get(cards.indexOf(lastTransport));
            if (gameData.money - dataf.costs * 15 >= 0) {
                gameData.money -= dataf.costs*15;
                gameData.enableTransport.add(dataf.name);
                lastTransport.setVisibility(View.GONE);
                lastTransport.setMaxHeight(0);
                lastTransport.setBackground(lastTransport.getContext().getDrawable(R.drawable.food_style));
                lastTransport = null;
                return 0;
            } else return 1;
        } else return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout card;
        private TextView extendLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = (ConstraintLayout) itemView;
        }

        public void bind(TransportData item) {
            TextView name = card.findViewById(R.id.name);
            TextView cost = card.findViewById(R.id.cost);
            ImageView icon = card.findViewById(R.id.icon);
            extendLayout = card.findViewById(R.id.detail);
            extendLayout.setVisibility(View.GONE);
            extendLayout.setText(item.description);
            name.setText(item.name);
            cards.add(card);
            card.setClipToOutline(true);
            cost.setText("-" + item.costs * 15 + " руб.");
            Drawable myDrawable = AppCompatResources.getDrawable(card.getContext(), item.icon);
            icon.setImageDrawable(myDrawable);
        }
    }
}
