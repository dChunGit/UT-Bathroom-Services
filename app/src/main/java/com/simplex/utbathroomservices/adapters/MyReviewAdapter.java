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
import com.simplex.utbathroomservices.cloudfirestore.Rating;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;

/**
 * Created by dchun on 11/27/17.
 */

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ReviewHolder> {
    private Context context;
    private ArrayList<Rating> reviews = new ArrayList<>();
    private String type;

    public MyReviewAdapter(Context context, ArrayList<Rating> reviews, String type) {
        this.context = context;
        this.reviews = reviews;
        this.type = type;
    }

    private Context getContext() {
        return context;
    }

    private String getRating(int position) {
        return reviews.get(position).getReview();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View reviewView = inflater.inflate(R.layout.myreview_item, parent, false);
        LinearLayout bathroom = reviewView.findViewById(R.id.bathroomreviewMini);
        LinearLayout fountain = reviewView.findViewById(R.id.fountainreviewMini);

        if(type.equals("Bathroom")) {
            bathroom.setVisibility(View.VISIBLE);
            fountain.setVisibility(View.GONE);
        } else if(type.equals("Fountain")) {
            bathroom.setVisibility(View.GONE);
            fountain.setVisibility(View.VISIBLE);
        }

        // Return a new holder instance
        ReviewHolder reviewHolder = new ReviewHolder(reviewView);
        return reviewHolder;

    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.review.setText(reviews.get(position).getReview());
        holder.overallBar.setRating(reviews.get(position).getOverallRating());
        holder.mybuilding.setText(reviews.get(position).getBuilding());
        holder.myfloor.setText(reviews.get(position).getFloor());

        if(type.equals("Bathroom")) {

            holder.stalls.setText(String.valueOf(reviews.get(position).getNumberStalls()));
            holder.spaceBar.setRating(getSpaceVal(reviews.get(position).getSpace()));
            holder.activityBar.setRating(reviews.get(position).getBusyness());
            holder.wifiBar.setRating(reviews.get(position).getWifiQuality());
            holder.cleanBar.setRating(reviews.get(position).getCleanliness());

        } else if(type.equals("Fountain")) {

            holder.bottleRefill.setText(reviews.get(position).isBottleRefillStation() ? "Yes":"No");
            holder.tasteBar.setRating(getTasteVal(reviews.get(position).getTaste()));
            holder.tempBar.setRating(getTempVal(reviews.get(position).getTemperature()));

        }
    }

    private int getSpaceVal(String space) {
        switch(space) {
            case "XSmall": return 1;
            case "Small": return 2;
            case "Medium": return 3;
            case "Large": return 4;
            case "XLarge": return 5;
            default: return 0;
        }
    }

    private int getTasteVal(String type) {
        System.out.println("taste: " + type);
        switch(type) {
            case "Wow": return 5;
            case "Pretty Good": return 4;
            case "Meh": return 3;
            case "Not Great": return 2;
            case "Disgusting": return 1;
            default: return 0;
        }
    }

    private int getTempVal(String type) {
        System.out.println("temp: " + type);
        switch(type) {
            case "cold": return 5;
            case "cool": return 4;
            case "lukewarm": return 3;
            case "warm": return 2;
            case "hot": return 1;
            default: return 0;
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView review, stalls, bottleRefill, mybuilding, myfloor;
        ScaleRatingBar spaceBar, activityBar, wifiBar, cleanBar, tempBar, tasteBar;
        SimpleRatingBar overallBar;

        public ReviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            review = itemView.findViewById(R.id.reviewView);
            overallBar = itemView.findViewById(R.id.starsMini);
            mybuilding = itemView.findViewById(R.id.buildingname_My);
            myfloor = itemView.findViewById(R.id.roomname_My);

            if(type.equals("Bathroom")) {
                stalls = itemView.findViewById(R.id.numstallsMini);
                spaceBar = itemView.findViewById(R.id.spaceBarMini);
                activityBar = itemView.findViewById(R.id.activityBarMini);
                wifiBar = itemView.findViewById(R.id.wifiBarMini);
                cleanBar = itemView.findViewById(R.id.cleanBarMini);
            } else if(type.equals("Fountain")) {
                bottleRefill = itemView.findViewById(R.id.isbottlerefillMini);
                tempBar = itemView.findViewById(R.id.tempBarMini);
                tasteBar = itemView.findViewById(R.id.tasteBarMini);
            }
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked Position = " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
            new MaterialDialog.Builder(view.getContext())
                    .title("Read More")
                    .content(getRating(getAdapterPosition()))
                    .positiveText("Close")
                    .show();

        }
    }
}
