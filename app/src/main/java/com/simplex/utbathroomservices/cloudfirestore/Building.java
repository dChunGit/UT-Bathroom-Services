package com.simplex.utbathroomservices.cloudfirestore;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by dchun on 11/26/17.
 */

public class Building implements Parcelable{

    ArrayList<String> buildings;

    public Building() {

    }

    public Building(ArrayList<String> buildings) {
        this.buildings = buildings;
    }

    @Override
    public String toString() {
        return "Buildings{" + "Buildings'" + buildings + '\'' + "}";
    }

    public ArrayList<String> getBuildings() {
        return buildings;
    }

    public void setBuildings(ArrayList<String> buildings) {
        this.buildings = buildings;
    }

    protected Building(Parcel in) {
        if (in.readByte() == 0x01) {
            buildings = new ArrayList<String>();
            in.readList(buildings, String.class.getClassLoader());
        } else {
            buildings = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (buildings == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(buildings);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Building> CREATOR = new Parcelable.Creator<Building>() {
        @Override
        public Building createFromParcel(Parcel in) {
            return new Building(in);
        }

        @Override
        public Building[] newArray(int size) {
            return new Building[size];
        }
    };

}
