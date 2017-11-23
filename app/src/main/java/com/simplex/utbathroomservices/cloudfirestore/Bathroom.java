package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zoeng on 11/10/17.
 */

public class Bathroom {


    Location location;
    String building;
    String floor;
    String space;
    String numberStalls;
    Integer wifiQuality; // 1 to 5 scale
    Integer busyness; // 1 to 5 scale
    Integer cleanliness; // 1 to 5 scale
    Integer overallRating; // 1 to 5 scale
    ArrayList<Rating> rating;
    String[] image;

    public Bathroom() {

    }

    public Bathroom(Location location, String building, String floor, String space, String numberStalls, Integer wifiQuality, Integer busyness, Integer cleanliness, Integer overallRating, ArrayList<Rating> rating, String[] image) {
        this.location = location;
        this.building = building;
        this.floor = floor;
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


    public String getSpace() {
        return space;

    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getNumberStalls() {
        return numberStalls;
    }

    public void setNumberStalls(String numberStalls) {
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

    public Location getLocation() {

        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
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
}
