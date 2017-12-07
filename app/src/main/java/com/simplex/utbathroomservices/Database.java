package com.simplex.utbathroomservices;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.simplex.utbathroomservices.dbflow.Favorite_Item;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.config.ConfigurationBuilder;

import java.util.ArrayList;
import java.util.HashMap;

@ReportsCrashes(mailTo = "simplexhelper@gmail.com",
        mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.exception,
        resDialogText = R.string.crash_dialog_text,
        resDialogTitle = R.string.crash_dialog_title,
        resDialogOkToast = R.string.crash_dialog_ok_toast,
        resDialogTheme = R.style.AppTheme_Dialog,
        logcatArguments = { "-t", "100", "-v" })

//saves database state, right now only for orientation change but could use to back up data as well
public class Database extends Application {

    private HashMap<String, Bathroom> firebaseBRatings = new HashMap<>();
    private HashMap<String, WaterFountain> firebaseWRatings = new HashMap<>();
    private ArrayList<Bathroom> saveBathroom = new ArrayList<>();
    private ArrayList<WaterFountain> saveFountain= new ArrayList<>();
    private ArrayList<String> saveBuildings= new ArrayList<>();
    private ArrayList<Marker> mapMarkers = new ArrayList<>();
    private HashMap<String, MarkerOptions> newMapmarkers = new HashMap<>();
    private ArrayList<String> oldMapmarkers = new ArrayList<>();
    private HashMap<String, String> favorites = new HashMap<>();
    private HashMap<String, Favorite_Item> favoriteItems = new HashMap<>();
    private HashMap<String, MarkerOptions> restoreMapMarkers = new HashMap<>();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        ACRA.init(this);
    }

    public HashMap<String, MarkerOptions> getRestoreMapMarkers() {
        return restoreMapMarkers;
    }

    public void setRestoreMapMarkers(HashMap<String, MarkerOptions> restoreMapMarkers) {
        this.restoreMapMarkers = restoreMapMarkers;
    }

    public HashMap<String, Bathroom> getFirebaseBRatings() {
        return firebaseBRatings;
    }

    public void setFirebaseBRatings(HashMap<String, Bathroom> firebaseBRatings) {
        this.firebaseBRatings = firebaseBRatings;
    }

    public HashMap<String, WaterFountain> getFirebaseWRatings() {
        return firebaseWRatings;
    }

    public void setFirebaseWRatings(HashMap<String, WaterFountain> firebaseWRatings) {
        this.firebaseWRatings = firebaseWRatings;
    }

    public ArrayList<Bathroom> getSaveBathroom() {
        return saveBathroom;
    }

    public void setSaveBathroom(ArrayList<Bathroom> saveBathroom) {
        this.saveBathroom = saveBathroom;
    }

    public ArrayList<WaterFountain> getSaveFountain() {
        return saveFountain;
    }

    public void setSaveFountain(ArrayList<WaterFountain> saveFountain) {
        this.saveFountain = saveFountain;
    }

    public ArrayList<String> getSaveBuildings() {
        return saveBuildings;
    }

    public void setSaveBuildings(ArrayList<String> saveBuildings) {
        this.saveBuildings = saveBuildings;
    }

    public ArrayList<Marker> getMapMarkers() {
        return mapMarkers;
    }

    public void setMapMarkers(ArrayList<Marker> mapMarkers) {
        this.mapMarkers = mapMarkers;
    }

    public HashMap<String, MarkerOptions> getNewMapmarkers() {
        return newMapmarkers;
    }

    public void setNewMapmarkers(HashMap<String, MarkerOptions> newMapmarkers) {
        System.out.println("Database: " + newMapmarkers);
        this.newMapmarkers = new HashMap<>(newMapmarkers);
    }

    public ArrayList<String> getOldMapmarkers() {
        return oldMapmarkers;
    }

    public void setOldMapmarkers(ArrayList<String> oldMapmarkers) {
        this.oldMapmarkers = oldMapmarkers;
    }

    public HashMap<String, String> getFavorites() {
        return favorites;
    }

    public void setFavorites(HashMap<String, String> favorites) {
        this.favorites = favorites;
    }

    public HashMap<String, Favorite_Item> getFavoriteItems() {
        return favoriteItems;
    }

    public void setFavoriteItems(HashMap<String, Favorite_Item> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }

}
