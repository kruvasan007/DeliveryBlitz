package com.example.myprojectgame.ui.click;

import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;

public abstract class CustomAnimationDrawable extends AnimationDrawable {
    Handler animationHandler;

    public CustomAnimationDrawable(AnimationDrawable aniDrawable) {
        for (int i = 0; i < aniDrawable.getNumberOfFrames(); i++) {
            this.addFrame(aniDrawable.getFrame(i), aniDrawable.getDuration(i));
        }
    }

    @Override
    public void start() {
        super.start();
        animationHandler = new Handler();
        animationHandler.post(this::onAnimationStart);
        animationHandler.postDelayed(this::onAnimationFinish, getTotalDuration());
    }

    public int getTotalDuration() {
        int iDuration = 0;
        for (int i = 0; i < this.getNumberOfFrames(); i++) {
            iDuration += this.getDuration(i);
        }
        return iDuration;
    }

    public abstract void onAnimationFinish();

    public abstract void onAnimationStart();
}
