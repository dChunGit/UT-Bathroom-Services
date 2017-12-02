package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by zoeng on 11/10/17.
 */

public class Bathroom implements Parcelable {

    double longitude;
    double latitude;
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

    public Bathroom(double longitude, double latitude, String building, String floor, Integer reviews, String space, Integer numberStalls,
                    Integer wifiQuality, Integer busyness, Integer cleanliness, Integer overallRating,
                    ArrayList<Rating> rating, ArrayList<String> image) {
        this.longitude = longitude;
        this.latitude = latitude;
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
                "longitude=" + longitude +
                ", latitude=" + latitude+
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
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

    protected Bathroom(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
        building = in.readString();
        floor = in.readString();
        reviews = in.readByte() == 0x00 ? null : in.readInt();
        space = in.readString();
        numberStalls = in.readByte() == 0x00 ? null : in.readInt();
        wifiQuality = in.readByte() == 0x00 ? null : in.readInt();
        busyness = in.readByte() == 0x00 ? null : in.readInt();
        cleanliness = in.readByte() == 0x00 ? null : in.readInt();
        overallRating = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            rating = new ArrayList<Rating>();
            in.readList(rating, Rating.class.getClassLoader());
        } else {
            rating = null;
        }
        if (in.readByte() == 0x01) {
            image = new ArrayList<String>();
            in.readList(image, String.class.getClassLoader());
        } else {
            image = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(building);
        dest.writeString(floor);
        if (reviews == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(reviews);
        }
        dest.writeString(space);
        if (numberStalls == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(numberStalls);
        }
        if (wifiQuality == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(wifiQuality);
        }
        if (busyness == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(busyness);
        }
        if (cleanliness == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(cleanliness);
        }
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
    public static final Parcelable.Creator<Bathroom> CREATOR = new Parcelable.Creator<Bathroom>() {
        @Override
        public Bathroom createFromParcel(Parcel in) {
            return new Bathroom(in);
        }

        @Override
        public Bathroom[] newArray(int size) {
            return new Bathroom[size];
        }
    };
}