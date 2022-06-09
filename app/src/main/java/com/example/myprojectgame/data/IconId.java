package com.example.myprojectgame.data;

import com.example.myprojectgame.R;

public enum IconId {

    MCDONALDS (R.drawable.mcdonalds),
    KFC (R.drawable.kfc),
    TOMYAM (R.drawable.tomyam),
    DODO (R.drawable.ic_dodo),
    PEKU (R.drawable.ic_pekupeku),
    BURGER (R.drawable.ic_burgerking),
    DUDNIK (R.drawable.ic_dudnik),
    HOTDOG (R.drawable.ic_hotdog),
    COUPLE (R.drawable.ic_couple),
    PINE (R.drawable.ic_pine),
    KOFFEE (R.drawable.koffe),

    BUS (R.drawable.bus),
    METRO (R.drawable.train),
    WALK (R.drawable.walking),
    BIKE (R.drawable.bike),
    SKATE (R.drawable.stake),

    STICK (R.drawable.stick),
    ENERGETIC (R.drawable.ic_energetic);

    private int icon;

    IconId(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}
