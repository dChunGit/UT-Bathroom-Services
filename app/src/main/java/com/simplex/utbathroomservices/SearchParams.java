package com.simplex.utbathroomservices;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dchun on 12/2/17.
 */

public class SearchParams implements Parcelable {
    private String searchType;
    private String type;

    private String building;
    private String floor;

    //bathroom stuff
    private int space;
    private int stalls;
    private int wifi;
    private int busyness;
    private int cleanliness;
    private int rating;

    //water fountain stuff
    private boolean isFillable;
    private int taste;
    private int temperature;

    public SearchParams(String searchType, String type, String building, String floor, int space, int stalls, int wifi, int busyness,
                        int cleanliness, int rating, boolean isFillable, int taste, int temperature) {
        this.searchType = searchType;
        this.type = type;
        this.building = building;
        this.floor = floor;
        this.space = space;
        this.stalls = stalls;
        this.wifi = wifi;
        this.busyness = busyness;
        this.cleanliness = cleanliness;
        this.rating = rating;
        this.isFillable = isFillable;
        this.taste = taste;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Bathroom{" +
                "searchtype='" + searchType+ '\'' +
                ", type='" + type + '\'' +
                ", building='" + building + '\'' +
                ", floor='" + floor + '\'' +
                ", space='" + space + '\'' +
                ", numberStalls=" + stalls +
                ", wifiQuality=" + wifi +
                ", busyness=" + busyness +
                ", cleanliness=" + cleanliness +
                ", overallRating=" + rating +
                ", isFillable='" + isFillable+ '\'' +
                ", taste='" + taste+ '\'' +
                ", temperature='" + temperature+ '\'' +
                '}';
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public int getStalls() {
        return stalls;
    }

    public void setStalls(int stalls) {
        this.stalls = stalls;
    }

    public int getWifi() {
        return wifi;
    }

    public void setWifi(int wifi) {
        this.wifi = wifi;
    }

    public int getBusyness() {
        return busyness;
    }

    public void setBusyness(int busyness) {
        this.busyness = busyness;
    }

    public int getCleanliness() {
        return cleanliness;
    }

    public void setCleanliness(int cleanliness) {
        this.cleanliness = cleanliness;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean getIsFillable() {
        return isFillable;
    }

    public void setIsFillable(boolean isFillable) {
        this.isFillable = isFillable;
    }

    public int getTaste() {
        return taste;
    }

    public void setTaste(int taste) {
        this.taste = taste;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }


    protected SearchParams(Parcel in) {
        type = in.readString();
        building = in.readString();
        floor = in.readString();
        space = in.readInt();
        stalls = in.readInt();
        wifi = in.readInt();
        busyness = in.readInt();
        cleanliness = in.readInt();
        rating = in.readInt();
        isFillable = in.readByte() != 0x00;
        taste = in.readInt();
        temperature = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(building);
        dest.writeString(floor);
        dest.writeInt(space);
        dest.writeInt(stalls);
        dest.writeInt(wifi);
        dest.writeInt(busyness);
        dest.writeInt(cleanliness);
        dest.writeInt(rating);
        dest.writeByte((byte) (isFillable ? 0x01 : 0x00));
        dest.writeInt(taste);
        dest.writeInt(temperature);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SearchParams> CREATOR = new Parcelable.Creator<SearchParams>() {
        @Override
        public SearchParams createFromParcel(Parcel in) {
            return new SearchParams(in);
        }

        @Override
        public SearchParams[] newArray(int size) {
            return new SearchParams[size];
        }
    };
}