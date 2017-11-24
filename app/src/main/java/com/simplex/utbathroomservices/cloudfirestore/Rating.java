package com.simplex.utbathroomservices.cloudfirestore;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zoeng on 11/10/17.
 */

public class Rating implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(review);
    }

    private Rating(Parcel in) {
        review = in.readString();
    }

    public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel parcel) {
            return new Rating(parcel);
        }

        @Override
        public Rating[] newArray(int i) {
            return new Rating[i];
        }
    };
}
