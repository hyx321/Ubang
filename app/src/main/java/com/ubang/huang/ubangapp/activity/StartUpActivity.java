package com.ubang.huang.ubangapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.ubang.huang.ubangapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StartUpActivity extends AppCompatActivity {
    private static final String TAG = "View Animation";

    @BindView(R.id.start_view)
    View start_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_activity);
        ButterKnife.bind(this);
        init();
    }
    private void init(){
        doAnimation(getAlphaAnimation());
    }

    private void doAnimation(Animation animation) {
        Animation oldAnimation = start_view.getAnimation();
        if (oldAnimation != null) {
            if (oldAnimation.hasStarted() || (!oldAnimation.hasEnded())) {
                oldAnimation.cancel();
                start_view.clearAnimation();
            }
        }    animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Pair<View, String> pTitle = null;
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(StartUpActivity.this,null);
                Intent intent = new Intent(StartUpActivity.this, LoginActivity.class);
                StartUpActivity.this.startActivity(intent, activityOptionsCompat.toBundle());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        start_view.startAnimation(animation);
    }

    private Animation getAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setRepeatCount(0);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setFillBefore(false);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        return alphaAnimation;
    }
}
