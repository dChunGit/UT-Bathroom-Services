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
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.simplex.utbathroomservices.Database;
import com.simplex.utbathroomservices.dbflow.Favorite_Item;
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.adapters.FavoriteAdapter;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Favorites extends AppCompatActivity {

    private ArrayList<Bathroom> Bfavorites = new ArrayList<>();
    private ArrayList<WaterFountain> Wfavorites = new ArrayList<>();
    private HashMap<String, Bathroom> firebaseBRatings = new HashMap<>();
    private HashMap<String, WaterFountain> firebaseWRatings = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Database database = (Database) getApplication();

        //put into hashmaps for faster access later on
        Callable<Boolean> callable = () -> {
            firebaseBRatings = database.getFirebaseBRatings();
            firebaseWRatings = database.getFirebaseWRatings();

            List<Favorite_Item> temp = SQLite.select()
                    .from(Favorite_Item.class)
                    .queryList();

            System.out.println("Favorites: " + temp);

            for(Favorite_Item item : temp) {
                String key = item.getFavorite().split("@")[0].trim();
                if(item.getType().equals("Bathroom")) {
                    Bfavorites.add(firebaseBRatings.get(key));
                } else if(item.getType().equals("Fountain")) {
                    Wfavorites.add(firebaseWRatings.get(key));
                }
            }
            return true;

        };
        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<Boolean> completed = executor.submit(callable);
        try {
            if(completed.get()) {
                System.out.println("Setting Up Adapter");
                setUpAdapter();
            }
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }

        setUpUI();


    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.addbar6);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_favorites));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener((view) -> onBackPressed());

        //set font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ColabReg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    private void setUpAdapter(){
        RecyclerView recyclerView1 = findViewById(R.id.reviewRecycler1);
        if(Bfavorites.size() == 0) {
            recyclerView1.setVisibility(View.GONE);
        } else {
            FavoriteAdapter favoriteAdapter1 = new FavoriteAdapter(this, Bfavorites, "Bathroom");
            recyclerView1.setAdapter(favoriteAdapter1);
            recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(1, 0));
            doBounceAnimation(recyclerView1);
        }

        RecyclerView recyclerView2 = findViewById(R.id.reviewRecycler2);
        if(Wfavorites.size() == 0) {
            recyclerView2.setVisibility(View.GONE);
        } else {
            FavoriteAdapter favoriteAdapter2 = new FavoriteAdapter(this, Wfavorites, "Fountain");
            recyclerView2.setAdapter(favoriteAdapter2);
            recyclerView2.setLayoutManager(new StaggeredGridLayoutManager(1, 0));
            doBounceAnimation(recyclerView2);
        }
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
