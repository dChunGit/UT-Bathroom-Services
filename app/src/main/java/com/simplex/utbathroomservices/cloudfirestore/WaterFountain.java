package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;

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
    Integer overallRating; // 1 to 5 scale
    ArrayList<Rating> rating;
    String [] image;

    @Override
    public String toString() {
        return "WaterFountain{" +
                "location=" + location +
                ", building='" + building + '\'' +
                ", floor='" + floor + '\'' +
                ", temperature='" + temperature + '\'' +
                ", isBottleRefillStation=" + isBottleRefillStation +
                ", taste='" + taste + '\'' +
                ", overallRating=" + overallRating +
                '}';
    }

    public WaterFountain(){

    }

    public WaterFountain(Location location, String building, String floor, String temperature, boolean isBottleRefillStation, String taste, Integer overallRating, ArrayList<Rating> rating, String[] image) {
        this.location = location;
        this.building = building;
        this.floor = floor;
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

    public String[] getImage() {
        return image;
    }

    public void setImage(String[] image) {
        this.image = image;
    }
}
