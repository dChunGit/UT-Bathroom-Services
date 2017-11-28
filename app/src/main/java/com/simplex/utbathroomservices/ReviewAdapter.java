package com.simplex.utbathroomservices;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.simplex.utbathroomservices.cloudfirestore.Rating;

import java.util.ArrayList;

/**
 * Created by dchun on 11/27/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private Context context;
    private ArrayList<Rating> reviews = new ArrayList<>();

    public ReviewAdapter(Context context, ArrayList<Rating> reviews) {
        this.context = context;
        this.reviews = reviews;
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
        View reviewView = inflater.inflate(R.layout.review_item, parent, false);

        // Return a new holder instance
        ReviewHolder reviewHolder = new ReviewHolder(reviewView);
        return reviewHolder;

    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        TextView textView = holder.getTextView();
        textView.setText(reviews.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView review;

        public ReviewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            review = itemView.findViewById(R.id.reviewView);
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


        public TextView getTextView() {
            return review;
        }
    }
}
