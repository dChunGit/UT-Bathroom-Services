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
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.adapters.MyReviewAdapter;
import com.simplex.utbathroomservices.adapters.ReviewAdapter;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.Rating;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.simplex.utbathroomservices.dbflow.Favorite_Item;
import com.simplex.utbathroomservices.dbflow.Rating_Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Reviews extends AppCompatActivity {
    private RecyclerView recyclerView3, recyclerView4;
    private ArrayList<Rating> myBathrooms = new ArrayList<>();
    private ArrayList<Rating> myFountains = new ArrayList<>();
    private HashMap<String, Bathroom> firebaseBRatings = new HashMap<>();
    private HashMap<String, WaterFountain> firebaseWRatings = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Database database = (Database) getApplication();

        new Thread(() -> {
            System.out.println("Called");
            firebaseBRatings = database.getFirebaseBRatings();
            firebaseWRatings = database.getFirebaseWRatings();

            System.out.println(firebaseBRatings);
            System.out.println(firebaseWRatings);

            List<Rating_Item> uuidList = SQLite.select()
                    .from(Rating_Item.class)
                    .queryList();

            for(Rating_Item rating_item : uuidList) {

                String key = rating_item.getLocation();
                String type = rating_item.getType();
                System.out.println(key + " " + type);

                if(type.equalsIgnoreCase("Bathroom")) {
                    if (firebaseBRatings.containsKey(key)) {
                        System.out.println("Bathroom contains");
                        ArrayList<Rating> rating = firebaseBRatings.get(key).getRating();
                        for (Rating rating1 : rating) {
                            long msb = rating_item.getUuid().getMostSignificantBits();
                            long lsb = rating_item.getUuid().getLeastSignificantBits();
                            if(rating1.getUuidMSB() == msb && rating1.getUuidLSB() == lsb) {
                                myBathrooms.add(rating1);
                            }
                        }

                    }
                }else if(type.equalsIgnoreCase("Fountain")) {
                    if(firebaseWRatings.containsKey(key)) {
                        System.out.println("Fountain contains");
                        ArrayList<Rating> rating = firebaseWRatings.get(key).getRating();
                        for (Rating rating1 : rating) {
                            long msb = rating_item.getUuid().getMostSignificantBits();
                            long lsb = rating_item.getUuid().getLeastSignificantBits();
                            if(rating1.getUuidMSB() == msb && rating1.getUuidLSB() == lsb) {
                                myFountains.add(rating1);
                            }
                        }
                    }
                }
            }

            System.out.println("UUIDS: " + uuidList);
            runOnUiThread(() -> setUpAdapters());


        }).start();

        setUpUI();
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
        if(myBathrooms.size() == 0) {
            recyclerView3.setVisibility(View.GONE);
        } else {
            MyReviewAdapter reviewBAdapter = new MyReviewAdapter(this, myBathrooms, "Bathroom");
            recyclerView3.setAdapter(reviewBAdapter);
            recyclerView3.setLayoutManager(new StaggeredGridLayoutManager(1, 0));
            doBounceAnimation(recyclerView3);
        }

        if(myFountains.size() == 0) {
            recyclerView4.setVisibility(View.GONE);
        } else {
            MyReviewAdapter reviewFAdapter = new MyReviewAdapter(this, myFountains, "Fountain");
            recyclerView4.setAdapter(reviewFAdapter);
            recyclerView4.setLayoutManager(new StaggeredGridLayoutManager(1, 0));
            doBounceAnimation(recyclerView4);
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
