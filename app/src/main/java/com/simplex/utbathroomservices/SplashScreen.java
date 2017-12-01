package com.simplex.utbathroomservices;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.transition.ChangeBounds;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.simplex.utbathroomservices.activities.MainActivity;
import com.wang.avi.AVLoadingIndicatorView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashScreen extends AppCompatActivity implements OnMapReadyCallback{

    private AVLoadingIndicatorView avLoadingIndicatorView;
    private TextView done;
    private View disappearView;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ColabReg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        avLoadingIndicatorView = findViewById(R.id.loadingIndicator);
        avLoadingIndicatorView.smoothToShow();
        done = findViewById(R.id.done);
        disappearView = findViewById(R.id.tempView);

        setFont();


        new Thread(() -> {
            try {
                final SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.loadingMap, supportMapFragment).commit();
                runOnUiThread(() -> supportMapFragment.getMapAsync(this));

            } catch (Exception e) {
                //log map load exception
            }
        }).start();
    }

    private void setFont() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ColabReg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        final Handler handler = new Handler();
        final Intent main = new Intent(this, MainActivity.class);
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadeinlong);
        animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                done.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        final ViewGroup transitionsContainer = findViewById(R.id.logoview);

        handler.postDelayed(() -> {

            if(avLoadingIndicatorView != null) {
                avLoadingIndicatorView.smoothToHide();
                Transition transition = new ChangeBounds();
                transition.setDuration(500);
                TransitionManager.beginDelayedTransition(transitionsContainer, transition);
                disappearView.setVisibility(View.GONE);

            }

            if(done != null) {
                done.startAnimation(animationFadeIn);
            }

        }, 1000);

        handler.postDelayed(() -> {

            startActivity(main);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        }, 2500);

        //Toast.makeText(this, "Loaded", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
