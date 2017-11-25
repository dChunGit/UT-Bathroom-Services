package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zoeng on 11/10/17.
 */

public class Bathroom implements Parcelable{

    Location location;
    String building;
    String floor;
    Integer reviews; //Number of reviews
    String space;
    Integer numberStalls;
    Integer wifiQuality; // 1 to 5 scale
    Integer busyness; // 1 to 5 scale
    Integer cleanliness; // 1 to 5 scale
    Integer overallRating; // 1 to 5 scale
    ArrayList<Rating> rating;
    ArrayList<String> image;

    public Bathroom() {

    }

    public Bathroom(Location location, String building, String floor, Integer reviews, String space, Integer numberStalls,
                    Integer wifiQuality, Integer busyness, Integer cleanliness, Integer overallRating,
                    ArrayList<Rating> rating, ArrayList<String> image) {
        this.location = location;
        this.building = building;
        this.floor = floor;
        this.reviews = reviews;
        this.space = space;
        this.numberStalls = numberStalls;
        this.wifiQuality = wifiQuality;
        this.busyness = busyness;
        this.cleanliness = cleanliness;
        this.overallRating = overallRating;
        this.rating = rating;
        this.image = image;
    }

    @Override
    public String toString() {
        return "Bathroom{" +
                "location=" + location +
                ", building='" + building + '\'' +
                ", floor='" + floor + '\'' +
                ", reviews='" + reviews + "\'" +
                ", space='" + space + '\'' +
                ", numberStalls=" + numberStalls +
                ", wifiQuality=" + wifiQuality +
                ", busyness=" + busyness +
                ", cleanliness=" + cleanliness +
                ", overallRating=" + overallRating +
                '}';
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

    public String getSpace() { return space; }

    public void setSpace(String space) {
        this.space = space;
    }

    public Integer getNumberStalls() {
        return numberStalls;
    }

    public void setNumberStalls(Integer numberStalls) {
        this.numberStalls = numberStalls;
    }

    public Integer getWifiQuality() {
        return wifiQuality;
    }

    public void setWifiQuality(Integer wifiQuality) {
        this.wifiQuality = wifiQuality;
    }

    public Integer getBusyness() {
        return busyness;
    }

    public void setBusyness(Integer busyness) {
        this.busyness = busyness;
    }

    public ArrayList<Rating> getRating() {
        return rating;
    }

    public void setRating(ArrayList<Rating> rating) {
        this.rating = rating;
    }

    public Location getLocation() { return location; }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public Integer getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(Integer cleanliness) {
        this.cleanliness = cleanliness;
    }

    public Integer getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(Integer overallRating) {
        this.overallRating = overallRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(location, i);
        parcel.writeString(building);
        parcel.writeString(floor);
        parcel.writeValue(reviews);
        parcel.writeString(space);
        parcel.writeValue(numberStalls);
        parcel.writeValue(wifiQuality);
        parcel.writeValue(busyness);
        parcel.writeValue(cleanliness);
        parcel.writeValue(overallRating);
        if(rating != null) {
            parcel.writeTypedArray(rating.toArray(new Rating[rating.size()]), i);
        } else {
            parcel.writeTypedArray(new Rating[0], i);
        }
        if(image != null) {
            parcel.writeStringArray(image.toArray(new String[image.size()]));
        } else {
            parcel.writeStringArray(new String[0]);
        }
    }

    private Bathroom(Parcel in) {
        location = in.readParcelable(Location.class.getClassLoader());
        building = in.readString();
        floor = in.readString();
        reviews = (Integer) in.readValue(Integer.class.getClassLoader());
        space = in.readString();
        numberStalls = (Integer) in.readValue(Integer.class.getClassLoader());
        wifiQuality = (Integer) in.readValue(Integer.class.getClassLoader());
        busyness = (Integer) in.readValue(Integer.class.getClassLoader());
        cleanliness = (Integer) in.readValue(Integer.class.getClassLoader());
        overallRating = (Integer) in.readValue(Integer.class.getClassLoader());
        Rating[] tempRating = in.createTypedArray(Rating.CREATOR);
        if(tempRating.length == 0) {
            rating = null;
        } else {
            rating = new ArrayList<>(Arrays.asList(tempRating));
        }
        String[] tempImage = in.createStringArray();
        if(tempImage.length == 0) {
            image = null;
        } else {
            image = new ArrayList<>(Arrays.asList(tempImage));
        }
    }

    public static final Parcelable.Creator<Bathroom> CREATOR = new Parcelable.Creator<Bathroom>() {

        @Override
        public Bathroom createFromParcel(Parcel parcel) {
            return new Bathroom(parcel);
        }

        @Override
        public Bathroom[] newArray(int i) {
            return new Bathroom[i];
        }
    };
}
