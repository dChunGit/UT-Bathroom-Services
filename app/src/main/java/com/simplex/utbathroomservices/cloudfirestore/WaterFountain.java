package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zoeng on 11/10/17.
 */

public class WaterFountain implements Parcelable {

    Location location;
    String building;
    String floor;
    Integer reviews;
    String temperature;
    boolean isBottleRefillStation;
    String taste;
    Integer overallRating; // 1 to 5 scale
    ArrayList<Rating> rating;
    ArrayList<String> image;

    @Override
    public String toString() {
        return "WaterFountain{" +
                "location=" + location +
                ", building='" + building + '\'' +
                ", floor='" + floor + '\'' +
                ", reviews='" + reviews + '\'' +
                ", temperature='" + temperature + '\'' +
                ", isBottleRefillStation=" + isBottleRefillStation +
                ", taste='" + taste + '\'' +
                ", overallRating=" + overallRating +
                '}';
    }

    public WaterFountain() {

    }

    public WaterFountain(Location location, String building, String floor, Integer reviews, String temperature, boolean isBottleRefillStation, String taste, Integer overallRating, ArrayList<Rating> rating, ArrayList<String> image) {
        this.location = location;
        this.building = building;
        this.floor = floor;
        this.reviews = reviews;
        this.temperature = temperature;
        this.isBottleRefillStation = isBottleRefillStation;
        this.taste = taste;
        this.overallRating = overallRating;
        this.rating = rating;
        this.image = image;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    public Location getLocation() {

        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public boolean isBottleRefillStation() {
        return isBottleRefillStation;
    }

    public void setBottleRefillStation(boolean bottleRefillStation) {
        isBottleRefillStation = bottleRefillStation;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public ArrayList<Rating> getRating() {
        return rating;
    }

    public void setRating(ArrayList<Rating> rating) {
        this.rating = rating;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected WaterFountain(Parcel in) {
        location = (Location) in.readValue(Location.class.getClassLoader());
        building = in.readString();
        floor = in.readString();
        temperature = in.readString();
        isBottleRefillStation = in.readByte() != 0x00;
        taste = in.readString();
        overallRating = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            rating = new ArrayList<>();
            in.readList(rating, Rating.class.getClassLoader());
        } else {
            rating = null;
        }
        if (in.readByte() == 0x01) {
            image = new ArrayList<>();
            in.readList(image, String.class.getClassLoader());
        } else {
            image = null;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(location);
        dest.writeString(building);
        dest.writeString(floor);
        dest.writeString(temperature);
        dest.writeByte((byte) (isBottleRefillStation ? 0x01 : 0x00));
        dest.writeString(taste);
        if (overallRating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(overallRating);
        }
        if (rating == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(rating);
        }
        if (image == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(image);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WaterFountain> CREATOR = new Parcelable.Creator<WaterFountain>() {
        @Override
        public WaterFountain createFromParcel(Parcel in) {
            return new WaterFountain(in);
        }

        @Override
        public WaterFountain[] newArray(int size) {
            return new WaterFountain[size];
        }
    };
}
