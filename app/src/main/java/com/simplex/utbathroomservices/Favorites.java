package com.simplex.utbathroomservices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.simplex.utbathroomservices.adapters.FavoriteAdapter;
import com.simplex.utbathroomservices.adapters.ReviewAdapter;
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
    }

    protected void setUpAdapter(){
        RecyclerView recyclerView1 = findViewById(R.id.reviewRecycler1);
        FavoriteAdapter favoriteAdapter1 = new FavoriteAdapter(this, sentBRatings, "Bathroom");
        recyclerView1.setAdapter(favoriteAdapter1);
        recyclerView1.setLayoutManager(new StaggeredGridLayoutManager(2, 0));

        RecyclerView recyclerView2 = findViewById(R.id.reviewRecycler2);
        FavoriteAdapter favoriteAdapter2 = new FavoriteAdapter(this, sentWRatings, "Bathroom");
        recyclerView2.setAdapter(favoriteAdapter2);
        recyclerView2.setLayoutManager(new StaggeredGridLayoutManager(2, 0));
    }
}
