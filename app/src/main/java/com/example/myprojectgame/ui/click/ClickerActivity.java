package com.example.myprojectgame.ui.click;

import static com.example.myprojectgame.ui.root.MainActivity.gameData;
import static com.example.myprojectgame.ui.root.MainActivity.selectOrderData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myprojectgame.R;
import com.example.myprojectgame.data.SelectOrderData;
import com.example.myprojectgame.domain.PreferencesManager;
import com.example.myprojectgame.ui.MyDialogFragment;
import com.example.myprojectgame.ui.root.BaseActivity;
import com.example.myprojectgame.ui.root.MainActivity;

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
    private TextView timer, descr;
    private long firstTime;
    private MyTimer myTimerTask;
    private Timer mTimer;
    private static int damage;
    private View clicker_view;

    @SuppressLint({"ClickableViewAccessibility", "ResourceAsColor"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clicker);

        //start order
        selectOrderData.state = 1;

        prograssBar = findViewById(R.id.progressBar);
        descr = findViewById(R.id.descr);
        timer = findViewById(R.id.timer);
        personImage = findViewById(R.id.image_view);
        personAnimation = (AnimationDrawable) personImage.getBackground();

        personAnimation.start();

        //create fastversion animation
        fastpersonAnimation = new AnimationDrawable();
        for (int i = 0; i < personAnimation.getNumberOfFrames(); i++)
            fastpersonAnimation.addFrame(personAnimation.getFrame(i), 50);
        fastpersonAnimation.setOneShot(true);

        setDamage();
        onStartTimer();

        //random accident
        if (damage == 3 && gameData.exp > 50 && gameData.health > 30) createDialogAccident();

        fastAnimation = new CustomAnimationDrawable(fastpersonAnimation) {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationFinish() {
                clickCounter--;
                //if the number of clicks end
                if (clickCounter == 0) {
                    this.stop();
                    personImage.setBackground(personAnimation);
                    personAnimation.start();
                } else {
                    this.start();
                }
            }
        };
        clicker_view = findViewById(R.id.clicker_listener);
        clicker_view.setOnClickListener(v -> clickAnimation());
    }

    private void setDamage() {
        Random random = new Random();
        damage = random.nextInt(10);
    }

    private void startAnim() {
        if (clickCounter == 1) {
            descr.setVisibility(View.INVISIBLE);
            personAnimation.stop();
            personImage.setBackground(fastAnimation);
            fastAnimation.start();
        }
    }

    private void clickAnimation() {
        clickCounter++;
        firstTime -= 1000;
        prograssBar.setProgress(prograssBar.getProgress() + 1);
        startAnim();
    }

    private void createDialogAccident() {
        MyDialogFragment dialogFragment = new MyDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
        gameData.health -= 20;
    }

    private void makeToastSize(String message, int type) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast,
                (LinearLayout) findViewById(R.id.toast_layout));

        TextView head = layout.findViewById(R.id.head);
        TextView description = layout.findViewById(R.id.descript);
        switch (type) {
            case 0:
                head.setText("Здоровье");
                head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.heart), null, null, null);
                break;
            case 1:
                head.setText("Локация");
                head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.ic_location_without_button), null, null, null);
                break;
            case 2:
                head.setText("Вознаграждение");
                head.setCompoundDrawablesWithIntrinsicBounds(getDrawable(R.drawable.money), null, null, null);
                break;
        }
        description.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //set timer
    private void onStartTimer() {
        clickCounter = 0;
        prograssBar.setMax((int) selectOrderData.currentTime - 2 + selectOrderData.currentProgress);
        prograssBar.setProgress(selectOrderData.currentProgress);
        firstTime = System.currentTimeMillis() + selectOrderData.currentTime * 1000;
        mTimer = new Timer();
        myTimerTask = new MyTimer();
        mTimer.schedule(myTimerTask, 1000, 1000);
    }

    class MyTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectOrderData.currentTime = firstTime - System.currentTimeMillis() - 1000;
                    @SuppressLint("DefaultLocale") String strDate = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(selectOrderData.currentTime),
                            TimeUnit.MILLISECONDS.toSeconds(selectOrderData.currentTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(selectOrderData.currentTime))
                    );
                    selectOrderData.currentTime /= 1000;
                    prograssBar.setProgress(prograssBar.getProgress() + 1);
                    selectOrderData.currentProgress = prograssBar.getProgress();
                    if (selectOrderData.currentTime <= 0) {
                        makeToastSize("Доставка выполнена успешно", 2);
                        endTime();
                    } else timer.setText(strDate);

                }
            });
        }
    }

    private void endTime() {
        gameData.money += selectOrderData.earnFomOrder;
        gameData.exp += selectOrderData.addExp;
        gameData.money -= selectOrderData.costDelivery;
        selectOrderData = new SelectOrderData(null, null, null, null, null, null, null, null, null);
        Intent intent = new Intent(ClickerActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //checking for the end of time
    @Override
    protected void onPause() {
        selectOrderData.lastTime = System.currentTimeMillis();
        myTimerTask.cancel();
        mTimer.cancel();
        mTimer.purge();
        PreferencesManager pm = new PreferencesManager(this);
        pm.setSelectOrderData(selectOrderData);
        pm.setGameData(gameData);
        pm.close();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (System.currentTimeMillis() - selectOrderData.lastTime >= selectOrderData.currentTime * 1000) {
            makeToastSize("Доставка выполнена успешно",2);
            endTime();
        } else {
            selectOrderData.currentTime -= (System.currentTimeMillis() - selectOrderData.lastTime) / 1000;
            onStartTimer();
        }
    }
}

