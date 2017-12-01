package com.simplex.utbathroomservices.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.simplex.utbathroomservices.cloudfirestore.Rating;

/**
 * Created by dchun on 11/30/17.
 */

public class Rating_Item {
    @PrimaryKey
    private String location;

    @Column
    private Rating rating;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

}
