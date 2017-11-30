package com.simplex.utbathroomservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.simplex.utbathroomservices.adapters.ReviewAdapter;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;

import java.util.ArrayList;

public class Reviews extends AppCompatActivity {
    private RecyclerView recyclerView3, recyclerView4;
    private ArrayList<Bathroom> sentBRatings = new ArrayList<>();
    private ArrayList<Bathroom> sentWRatings = new ArrayList<>();

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
        recyclerView3 = findViewById(R.id.reviewRecycler3);
        recyclerView4 = findViewById(R.id.reviewRecycler4);
        ReviewAdapter reviewBAdapter = new ReviewAdapter(this, sentBRatings.get(0).getRating(), "Bathroom");
        ReviewAdapter reviewFAdapter = new ReviewAdapter(this, sentWRatings.get(0).getRating(), "Fountain");
        recyclerView3.setAdapter(reviewBAdapter);
        recyclerView3.setLayoutManager(new StaggeredGridLayoutManager(2, 0));
        recyclerView4.setAdapter(reviewFAdapter);
        recyclerView4.setLayoutManager(new StaggeredGridLayoutManager(2, 0));
    }
}
