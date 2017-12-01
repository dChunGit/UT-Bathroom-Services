package com.simplex.utbathroomservices.activities;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.adapters.ReviewAdapter;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

import java.util.ArrayList;

public class Reviews extends AppCompatActivity {
    private RecyclerView recyclerView3, recyclerView4;
    private ArrayList<Bathroom> sentBRatings = new ArrayList<>();
    private ArrayList<WaterFountain> sentWRatings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        try{
            sentBRatings = getIntent().getParcelableArrayListExtra("BRatings");
            System.out.println(sentBRatings);
        } catch (Exception e) {
            System.out.println("BRatings malformed");
        }

        try{
            sentWRatings = getIntent().getParcelableArrayListExtra("WRatings");
            System.out.println(sentWRatings);
        } catch (Exception e) {
            System.out.println("WRatings malformed");
        }

        setUpUI();
        setUpAdapters();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.reviewbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_reviews));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((view) -> onBackPressed());

    }

    private void setUpAdapters() {
        recyclerView3 = findViewById(R.id.reviewRecycler3);
        recyclerView4 = findViewById(R.id.reviewRecycler4);
        ReviewAdapter reviewBAdapter = new ReviewAdapter(this, sentBRatings.get(0).getRating(), "Bathroom");
        ReviewAdapter reviewFAdapter = new ReviewAdapter(this, sentWRatings.get(0).getRating(), "Fountain");
        recyclerView3.setAdapter(reviewBAdapter);
        recyclerView3.setLayoutManager(new StaggeredGridLayoutManager(1, 0));
        recyclerView4.setAdapter(reviewFAdapter);
        recyclerView4.setLayoutManager(new StaggeredGridLayoutManager(1, 0));

        doBounceAnimation(recyclerView3);
        doBounceAnimation(recyclerView4);
    }

    private void doBounceAnimation(View targetView) {
        if(targetView != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", 0, 40, 0);
            animator.setInterpolator(new EasingInterpolator(Ease.ELASTIC_IN_OUT));
            animator.setStartDelay(500);
            animator.setDuration(1500);
            animator.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

}
