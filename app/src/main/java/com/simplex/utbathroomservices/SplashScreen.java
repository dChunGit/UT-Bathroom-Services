package com.simplex.utbathroomservices;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wang.avi.AVLoadingIndicatorView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class SplashScreen extends AppCompatActivity implements OnMapReadyCallback{

    private AVLoadingIndicatorView avLoadingIndicatorView;
    private TextView done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        avLoadingIndicatorView = findViewById(R.id.loadingIndicator);
        avLoadingIndicatorView.smoothToShow();
        done = findViewById(R.id.done);

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
        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
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

        handler.postDelayed(() -> {

            if(avLoadingIndicatorView != null) {
                avLoadingIndicatorView.smoothToHide();
            }

            if(done != null) {
                done.startAnimation(animationFadeIn);
            }

        }, 1500);

        handler.postDelayed(() -> {

            startActivity(main);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        }, 2000);

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
