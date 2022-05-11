package com.example.myprojectgame.ui.click;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myprojectgame.R;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ClickerActivity extends BaseActivity {
    private AnimationDrawable personAnimation;
    private AnimationDrawable fastpersonAnimation;
    private int clickCounter;
    private ImageView personImage;
    private CustomAnimationDrawable fastAnimation;
    private ProgressBar prograssBar;
    private TextView timer;
    private long firstTime;
    private MyTimer myTimerTask;
    private Timer mTimer;
    private String strDate;
    private static int time;
    private static int damage;

    @SuppressLint({"ClickableViewAccessibility", "ResourceAsColor"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicker);
        onStartTimer();
        prograssBar = (ProgressBar) findViewById(R.id.progressBar);
        prograssBar.setMax(time);
        timer = findViewById(R.id.timer);
        personImage = (ImageView) findViewById(R.id.image_view);
        personAnimation = (AnimationDrawable) personImage.getBackground();


        if (damage == 3) createDialogAccident();
        clickCounter = 0;
        personAnimation.start();
        fastpersonAnimation = new AnimationDrawable();
        for (int i = 0; i < personAnimation.getNumberOfFrames(); i++)
            fastpersonAnimation.addFrame(personAnimation.getFrame(i), 50);
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

        personImage.setOnClickListener(v -> clickAnimation());
    }

    public static void setStart(List<Double> cord, Double k) {
        Random random = new Random();
        damage = random.nextInt(10);
        time = (int) (Math.abs(cord.get(0) * cord.get(0) - gameData.gamerCoord.get(0) * gameData.gamerCoord.get(0) + (cord.get(1) * cord.get(1) - gameData.gamerCoord.get(1) * gameData.gamerCoord.get(1))) / 0.1188355527468957 * k * 60);
    }

    private void startAnim() {
        if (clickCounter == 1) {
            personAnimation.stop();
            personImage.setBackground(fastAnimation);
            fastAnimation.start();
        }
    }

    private void clickAnimation() {
        clickCounter++;
        firstTime -= 1000;
        prograssBar.setProgress(prograssBar.getProgress()+1);
        startAnim();
    }

    private void createDialogAccident() {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
        gameData.health -= 20;
    }

    private void makeToastSize(String t) {
        SpannableStringBuilder biggerText = new SpannableStringBuilder(t);
        biggerText.setSpan(new RelativeSizeSpan(0.7f), 0, t.length(), 0);
        Toast.makeText(this, biggerText, Toast.LENGTH_LONG).show();
    }

    private void onStartTimer() {
        firstTime = System.currentTimeMillis()+time*1000;
        mTimer = new Timer();
        myTimerTask = new MyTimer();
        mTimer.schedule(myTimerTask, 1000, 1000);
    }

    class MyTimer extends TimerTask {
        @Override
        public void run() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "mm:ss", Locale.getDefault());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    long currentTime = firstTime-System.currentTimeMillis()-1000;
                    if(currentTime>=0) {
                        @SuppressLint("DefaultLocale") String strDate = String.format("%d:%d",
                                TimeUnit.MILLISECONDS.toMinutes(currentTime),
                                TimeUnit.MILLISECONDS.toSeconds(currentTime) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(currentTime))
                        );
                    }else strDate = "0:00";
                    timer.setText(strDate);
                    prograssBar.setProgress(prograssBar.getProgress()+1);
                    if(prograssBar.getProgress()>=time){
                        mTimer.cancel();
                        myTimerTask.cancel();
                        makeToastSize("Доставка выполнена успешно");
                        gameData.money += gameData.earn;
                        Intent intent = new Intent(ClickerActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}

