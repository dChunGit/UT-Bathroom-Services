package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;

/**
 * Created by zoeng on 11/10/17.
 */

public class WaterFountain {
    Location location;
    String building;
    String floor;
    String temperature;
    boolean isBottleRefillStation;
    String taste;
    Rating rating;
    String [] image;

    public WaterFountain(){

    }

    @Override
    public String toString() {
        return "WaterFountain{" +
                "location=" + location +
                ", building='" + building + '\'' +
                ", floor='" + floor + '\'' +
                ", temperature='" + temperature + '\'' +
                ", isBottleRefillStation=" + isBottleRefillStation +
                ", taste='" + taste + '\'' +
                ", rating=" + rating +
                '}';
    }

    public WaterFountain(Location location, String building, String floor, String temperature, boolean isBottleRefillStation, String taste, Rating rating, String [] image) {
        this.location = location;
        this.building = building;
        this.floor = floor;
        this.temperature = temperature;
        this.isBottleRefillStation = isBottleRefillStation;
        this.taste = taste;
        this.rating = rating;
        this.image = image;
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

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }
}
