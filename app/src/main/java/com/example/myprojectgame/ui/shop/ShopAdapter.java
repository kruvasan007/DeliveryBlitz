package com.example.myprojectgame.ui.shop;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    public ShopAdapter(List<FoodData> data) {
        cards.clear();
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_food, parent, false);
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

    public static void buyItem() {
        if (lastFood != null) {
            TextView health = lastFood.findViewById(R.id.health);
            TextView cost = lastFood.findViewById(R.id.cost);
            if (gameData.money - Integer.parseInt(((String) cost.getText()).split(" ")[0]) >= 0) {
                gameData.health += Integer.parseInt(((String) health.getText()).split(" ")[0]);
                gameData.money -= Integer.parseInt(((String) cost.getText()).split(" ")[0]);
                lastFood.setBackground(lastFood.getContext().getDrawable(R.drawable.food_style));
                lastFood = null;
            }
            else Toast.makeText(cards.get(0).getContext(),"У вас недостаточно средств",Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(cards.get(0).getContext(),"Выберите товар",Toast.LENGTH_SHORT).show();
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

        public void bind(FoodData item){
            TextView name = card.findViewById(R.id.name);
            TextView cost = card.findViewById(R.id.cost);
            TextView health = card.findViewById(R.id.health);
            ImageView icon = card.findViewById(R.id.icon);
            name.setText(item.name);
            cards.add(card);
            card.setClipToOutline(true);
            health.setText(item.health+" hp");
            cost.setText(item.cost+" руб.");
            Drawable myDrawable = card.getContext().getDrawable(Integer.parseInt(item.icon));
            icon.setImageDrawable(myDrawable);
        }
    }
}
