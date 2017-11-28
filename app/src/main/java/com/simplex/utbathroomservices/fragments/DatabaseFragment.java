package com.simplex.utbathroomservices.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseFragment extends Fragment {

    private HashMap<String, Bathroom> firebaseBRatings = new HashMap<>();
    private HashMap<String, WaterFountain> firebaseWRatings = new HashMap<>();
    private ArrayList<Bathroom> saveBathroom = new ArrayList<>();
    private ArrayList<WaterFountain> saveFountain= new ArrayList<>();
    private ArrayList<String> saveBuildings= new ArrayList<>();
    private ArrayList<Marker> mapMarkers = new ArrayList<>();
    private HashMap<String, MarkerOptions> newMapmarkers = new HashMap<>();
    private ArrayList<String> oldMapmarkers = new ArrayList<>();

    public DatabaseFragment() {
        // Required empty public constructor
    }

    public static DatabaseFragment newInstance() {
        return new DatabaseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

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

}
