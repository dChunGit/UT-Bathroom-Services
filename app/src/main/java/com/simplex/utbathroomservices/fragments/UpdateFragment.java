package com.simplex.utbathroomservices.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.Building;
import com.simplex.utbathroomservices.cloudfirestore.BuildingDB;
import com.simplex.utbathroomservices.cloudfirestore.DatabaseCallback;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountainDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class UpdateFragment extends Fragment implements DatabaseCallback {

    public interface onUpdateListener {
        void onUpdateBFinish(HashMap<String, Bathroom> firebaseResults, LinkedList<Bathroom> resultsList,
                             LinkedList<MarkerOptions> markers, boolean doAll);
        void onUpdateWFinish(HashMap<String, WaterFountain> firebaseResults, LinkedList<WaterFountain> resultsList,
                             LinkedList<MarkerOptions> markers, boolean doAll);
        void onUpdateBuildingFinish(LinkedList<String> buildings);
    }

    private onUpdateListener mListener;
    private LinkedList<MarkerOptions> markerOptions = new LinkedList<>();
    private String type;
    private boolean doAll = false;

    public UpdateFragment() {
        // Required empty public constructor
    }


    public static UpdateFragment newInstance(String type) {
        UpdateFragment updateFragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putString("Type", type);
        updateFragment.setArguments(args);
        return updateFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            type = getArguments().getString("Type");
        }
        startUpdate();
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onUpdateListener) {
            mListener = (onUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onUpdateListener");
        }
    }

    private void startUpdate() {
        if(type.contains("Update")) {
            doAll = true;
        }

        if(type.contains("Bathroom") || type.equalsIgnoreCase("Update")) {
            BathroomDB bathroomDB = new BathroomDB(this);
            bathroomDB.getAllBathrooms();
        } else if(type.contains("Fountain")) {
            WaterFountainDB waterFountainDB = new WaterFountainDB(this);
            waterFountainDB.getAllWaterFountains();
        } else if(type.contains("Buildings")) {
            BuildingDB buildingDB = new BuildingDB(this);
            buildingDB.getAllBuildings();
            //buildingDB.addAllBuildings(new ArrayList<>(Arrays.asList(buildings)));
        }
    }

    @Override
    public void updateFinishedB(LinkedList<Bathroom> r) {
        new Thread(() -> {
            HashMap<String, Bathroom> results = new HashMap<>();
            for(Bathroom bathroom : r) {
                results.put(bathroom.getBuilding() + " " + bathroom.getFloor(), bathroom);
            }
            setUpMarkers(r);
            mListener.onUpdateBFinish(results, r, markerOptions, doAll);
        }).start();
    }

    @Override
    public void updateFinishedF(LinkedList<WaterFountain> r) {
        new Thread(() -> {
            HashMap<String, WaterFountain> results = new HashMap<>();
            for(WaterFountain waterFountain : r) {
                results.put(waterFountain.getBuilding() + " " + waterFountain.getFloor(), waterFountain);
            }
            setUpMarkers(r);
            mListener.onUpdateWFinish(results, r, markerOptions, doAll);
        }).start();
    }

    @Override
    public void updateBuildings(LinkedList<Building> b) {
        new Thread(() -> {
            LinkedList<String> temp = new LinkedList<>(b.get(0).getBuildings());
            mListener.onUpdateBuildingFinish(temp);
        }).start();
    }

    private <T> void setUpMarkers(LinkedList<T> results) {
        for(T item: results) {
            Location location;
            String title, type;
            if(item instanceof Bathroom) {
                Bathroom bathroom = (Bathroom) item;
                title = bathroom.getBuilding() + " " + bathroom.getFloor();
                System.out.println(title);
                location = bathroom.getLocation();
                type = "Bathroom";

            } else {
                WaterFountain fountain = (WaterFountain) item;
                title = fountain.getBuilding() + " " + fountain.getFloor();
                System.out.println(title);
                location = fountain.getLocation();
                type = "Fountain";
            }

            if(location != null) {
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions options = new MarkerOptions().position(sydney).title(title + " @ " + type);
                synchronized (this) {
                    markerOptions.add(options);
                }
            }

        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
