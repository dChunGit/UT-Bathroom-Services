package com.simplex.utbathroomservices.cloudfirestore;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zoeng on 11/10/17.
 */

public class Rating implements Parcelable {
    String review;
    String space;
    Integer numberStalls;
    Integer wifiQuality; // 1 to 5 scale
    Integer busyness; // 1 to 5 scale
    Integer cleanliness; // 1 to 5 scale
    Integer overallRating; // 1 to 5 scale
    String temperature;
    boolean isBottleRefillStation;
    String taste;

    @Override
    public String toString() {
        return "Rating{" +
                "review='" + review + '\'' +
                ", space='" + space + '\'' +
                ", numberStalls='" + numberStalls + '\'' +
                ", wifiQuality='" + wifiQuality + '\'' +
                ", busyness='" + busyness + '\'' +
                ", cleanliness='" + cleanliness + '\'' +
                ", overallRating='" + overallRating + '\'' +
                ", temperature='" + temperature + '\'' +
                ", isBottleRefillStation='" + isBottleRefillStation + '\'' +
                ", taste='" + taste + '\'' +
                '}';
    }

    public Rating(){

    }
    public Rating(String review, String space, Integer numberStalls, Integer wifiQuality, Integer busyness,
                  Integer cleanliness, Integer overallRating, String temperature, boolean isBottleRefillStation,
                  String taste) {
        this.review = review;
        this.space = space;
        this.numberStalls = numberStalls;
        this.wifiQuality = wifiQuality;
        this.busyness = busyness;
        this.cleanliness = cleanliness;
        this.overallRating = overallRating;
        this.temperature = temperature;
        this.isBottleRefillStation = isBottleRefillStation;
        this.taste = taste;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
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

    protected Rating(Parcel in) {
        review = in.readString();
        space = in.readString();
        numberStalls = in.readByte() == 0x00 ? null : in.readInt();
        wifiQuality = in.readByte() == 0x00 ? null : in.readInt();
        busyness = in.readByte() == 0x00 ? null : in.readInt();
        cleanliness = in.readByte() == 0x00 ? null : in.readInt();
        overallRating = in.readByte() == 0x00 ? null : in.readInt();
        temperature = in.readString();
        isBottleRefillStation = in.readByte() != 0x00;
        taste = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(review);
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
        dest.writeString(temperature);
        dest.writeByte((byte) (isBottleRefillStation ? 0x01 : 0x00));
        dest.writeString(taste);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Rating> CREATOR = new Parcelable.Creator<Rating>() {
        @Override
        public Rating createFromParcel(Parcel in) {
            return new Rating(in);
        }

        @Override
        public Rating[] newArray(int size) {
            return new Rating[size];
        }
    };
}