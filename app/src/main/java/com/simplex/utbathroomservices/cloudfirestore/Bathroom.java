package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;

/**
 * Created by zoeng on 11/10/17.
 */

public class Bathroom {
    Location location;
    String building;
    String floor;
    String space;
    Integer numberStalls;
    Integer wifiQuality; // 1 to 5 scale
    Integer busyness; // 1 to 5 scale
    Rating rating;
    String[] image;

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
                ", rating=" + rating +
                '}';
    }

    public Bathroom() {

    }

    public Bathroom(Location location, String building, String floor, String space, Integer numberStalls, Integer wifiQuality, Integer busyness, Rating rating, String[] image) {
        this.location = location;
        this.building = building;
        this.floor = floor;
        this.space = space;
        this.numberStalls = numberStalls;
        this.wifiQuality = wifiQuality;
        this.busyness = busyness;
        this.rating = rating;
        this.image = image;
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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
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
}
