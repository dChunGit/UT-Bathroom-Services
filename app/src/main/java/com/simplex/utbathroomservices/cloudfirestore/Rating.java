package com.simplex.utbathroomservices.cloudfirestore;

/**
 * Created by zoeng on 11/10/17.
 */

public class Rating {
    String review;

    @Override
    public String toString() {
        return "Rating{" +
                "review='" + review + '\'' +
                '}';
    }

    public Rating(){

    }
    public Rating(String review) {
        this.review = review;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
