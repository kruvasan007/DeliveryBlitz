package com.example.myprojectgame.data;

import com.example.myprojectgame.R;

public enum IconId {

    MCDONALDS (R.drawable.mcdonalds),
    KFC (R.drawable.kfc),
    TOMYAM (R.drawable.tomyam),
    KOFFEE (R.drawable.koffe),
    BUS (R.drawable.bus),
    METRO (R.drawable.train),
    WALK (R.drawable.walking),
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
