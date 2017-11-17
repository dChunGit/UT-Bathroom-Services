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

    public WaterFountain(){

    }
    public WaterFountain(Location location, String building, String floor, String temperature, boolean isBottleRefillStation, String taste, Rating rating) {
        this.location = location;
        this.building = building;
        this.floor = floor;
        this.temperature = temperature;
        this.isBottleRefillStation = isBottleRefillStation;
        this.taste = taste;
        this.rating = rating;
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


}
