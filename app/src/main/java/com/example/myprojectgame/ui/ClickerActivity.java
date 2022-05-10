package com.example.myprojectgame.ui;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myprojectgame.R;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;

import java.util.List;

public class ClickerActivity extends BaseActivity {
    private AnimationDrawable personAnimation;
    private AnimationDrawable fastpersonAnimation;
    private int clickCounter;
    private ImageView personImage;
    private CustomAnimationDrawable fastAnimation;
    private ProgressBar prograssBar;
    private int progress;

    private static int time;

    @SuppressLint({"ClickableViewAccessibility", "ResourceAsColor"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicker);
        prograssBar = (ProgressBar) findViewById(R.id.progressBar);
        prograssBar.setMax(time);

        personImage = (ImageView) findViewById(R.id.image_view);
        personAnimation = (AnimationDrawable) personImage.getBackground();

        clickCounter = 0;
        personAnimation.start();
        fastpersonAnimation = new AnimationDrawable();
        for (int i = 0; i < personAnimation.getNumberOfFrames(); i++) {
            fastpersonAnimation.addFrame(personAnimation.getFrame(i), 50);
        }
        fastpersonAnimation.setOneShot(true);

        fastAnimation = new CustomAnimationDrawable(fastpersonAnimation) {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationFinish() {
                clickCounter--;
                if (clickCounter == 0) {
                    this.stop();
                    personImage.setBackground(personAnimation);
                    personAnimation.start();
                } else {
                    this.start();
                }
            }
        };

        personImage.setOnClickListener(v -> {
            progress = (int) (progress+2*(2-gameData.k));
            if(progress > time){
                gameData.money += gameData.earn;
                Toast.makeText(this, "Доставка осуществлена", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ClickerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            if (progress > 0) postProgress(progress);
            else postProgress(1);

            clickCounter++;
            startAnim();
        });
    }

    private void postProgress(int progress) {
        String strProgress = String.valueOf(progress) + " %";
        prograssBar.setProgress(progress);
    }

    public void startAnim(){
        if(clickCounter == 1){
            personAnimation.stop();
            personImage.setBackground(fastAnimation);
            fastAnimation.start();
        }
    }

    public static void setStart(List<Double> cord, Double k){
        time = (int) ((int) Math.abs(cord.get(0) * cord.get(0) - gameData.gamerCoord.get(0) * gameData.gamerCoord.get(0) + ( cord.get(1)*cord.get(1) -gameData.gamerCoord.get(1)*gameData.gamerCoord.get(1)))/0.1188355527468957*k);
    }
}
