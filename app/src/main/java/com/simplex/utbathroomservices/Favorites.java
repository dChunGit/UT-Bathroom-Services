package com.simplex.utbathroomservices;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;
import com.simplex.utbathroomservices.adapters.FavoriteAdapter;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    ArrayList<Bathroom> sentBRatings;
    ArrayList<WaterFountain> sentWRatings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
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
        setUpAdapter();
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.addbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_favorites));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener((view) -> onBackPressed());

    }

    private void setUpAdapter(){
        RecyclerView recyclerView1 = findViewById(R.id.reviewRecycler1);
        FavoriteAdapter favoriteAdapter1 = new FavoriteAdapter(this, sentBRatings, "Bathroom");
        recyclerView1.setAdapter(favoriteAdapter1);
        recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(1, 0));

        RecyclerView recyclerView2 = findViewById(R.id.reviewRecycler2);
        FavoriteAdapter favoriteAdapter2 = new FavoriteAdapter(this, sentWRatings, "Fountain");
        recyclerView2.setAdapter(favoriteAdapter2);
        recyclerView2.setLayoutManager(new StaggeredGridLayoutManager(1, 0));

        doBounceAnimation(recyclerView1);
        doBounceAnimation(recyclerView2);
    }

    private void doBounceAnimation(View targetView) {
        if(targetView != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", 0, 40, 0);
            animator.setInterpolator(new EasingInterpolator(Ease.ELASTIC_IN_OUT));
            animator.setStartDelay(500);
            animator.setDuration(2000);
            animator.start();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
