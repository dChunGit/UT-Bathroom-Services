package com.simplex.utbathroomservices.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.Rating;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;

/**
 * Created by dchun on 11/27/17.
 */

public class FavoriteAdapter  <T> extends RecyclerView.Adapter<FavoriteAdapter.ReviewHolder1> {
    private Context context;
    private ArrayList<T> ratings = new ArrayList<>();
    private String type;

    public FavoriteAdapter(Context context, ArrayList<T> ratings, String type) {
        this.context = context;
        this.ratings = ratings;
        this.type=type;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ReviewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View reviewView = inflater.inflate(R.layout.bathroom_favorites, parent, false);
        // Return a new holder instance
        ReviewHolder1 reviewHolder = new ReviewHolder1(reviewView);
        return reviewHolder;

    }

    @Override
    public void onBindViewHolder(FavoriteAdapter.ReviewHolder1 holder, int position) {

        if(type.equals("Bathroom")) {
            Bathroom b = (Bathroom) ratings.get(position);
            holder.building.setText(String.valueOf(b.getBuilding()));
            holder.room.setText(String.valueOf(b.getFloor()));
            holder.stars.setRating(b.getOverallRating());

        } else if(type.equals("Fountain")) {
            WaterFountain wf= (WaterFountain) ratings.get(position);
            holder.building.setText(String.valueOf(wf.getBuilding()));
            holder.room.setText(String.valueOf(wf.getFloor()));
            holder.stars.setRating(wf.getOverallRating());

        }
    }

    @Override
    public int getItemCount() {
        return ratings.size();
    }

    public class ReviewHolder1 extends RecyclerView.ViewHolder {
        TextView building, room;
        SimpleRatingBar stars;

        public ReviewHolder1(View itemView) {
            super(itemView);
            building= itemView.findViewById(R.id.buildingname1);
            room= itemView.findViewById(R.id.roomname1);
            stars= itemView.findViewById(R.id.stars1);



        }


    }
}
