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
import com.example.myprojectgame.db.FoodData;

import java.util.LinkedList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    public static List<FoodData> data;
    protected static ConstraintLayout lastFood;
    protected static LinkedList<ConstraintLayout> cards = new LinkedList<>();
    protected int isExpandable = -1;

    public ShopAdapter(List<FoodData> data) {
        cards.clear();
        lastFood = null;
        ShopAdapter.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static Integer buyItem() {
        if (lastFood != null) {
            FoodData dataf = data.get(cards.indexOf(lastFood));
            if (gameData.money - dataf.cost >= 0) {
                gameData.health += dataf.health;
                gameData.money -= dataf.cost;
                lastFood.setBackground(lastFood.getContext().getDrawable(R.drawable.food_style));
                lastFood = null;
                return 0;
            } else return 1;
        } else return 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = (ConstraintLayout) itemView;
            card.setOnClickListener(v -> {
                card.setBackground(card.getContext().getDrawable(R.drawable.selectable_food));
                if (lastFood != null && lastFood != card) {
                    lastFood.setBackground(card.getContext().getDrawable(R.drawable.food_style));
                }
                lastFood = card;
            });
        }

        public void bind(FoodData item) {
            TextView name = card.findViewById(R.id.name);
            TextView cost = card.findViewById(R.id.cost);
            TextView health = card.findViewById(R.id.health);
            ImageView icon = card.findViewById(R.id.icon);
            name.setText(item.name);
            cards.add(card);
            card.setClipToOutline(true);
            health.setText("+" + item.health + " hp");
            cost.setText("-" + item.cost + " руб.");
            Drawable myDrawable = AppCompatResources.getDrawable(card.getContext(), item.icon);
            icon.setImageDrawable(myDrawable);
        }
    }
}
