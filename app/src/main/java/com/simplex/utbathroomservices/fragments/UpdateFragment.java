package com.simplex.utbathroomservices.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.Building;
import com.simplex.utbathroomservices.cloudfirestore.BuildingDB;
import com.simplex.utbathroomservices.interfaces.DatabaseCallback;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountainDB;
import com.simplex.utbathroomservices.interfaces.onUpdateListener;

import java.util.ArrayList;
import java.util.HashMap;

//gets data from firebase database, later should make it periodic
public class UpdateFragment extends Fragment implements DatabaseCallback {

    private onUpdateListener mListener;
    private ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
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
    public void updateFinishedB(ArrayList<Bathroom> r) {
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
    public void updateFinishedF(ArrayList<WaterFountain> r) {
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
    public void updateBuildings(ArrayList<Building> b) {
        new Thread(() -> {
            ArrayList<String> temp = new ArrayList<>(b.get(0).getBuildings());
            mListener.onUpdateBuildingFinish(temp);
        }).start();
    }

    @Override
    public void addFinished(boolean success) {
        //ignore
    }

    private <T> void setUpMarkers(ArrayList<T> results) {
        for(T item: results) {
            float hue;
            double longitude, latitude;
            String title, type;
            if(item instanceof Bathroom) {
                Bathroom bathroom = (Bathroom) item;
                title = bathroom.getBuilding() + " " + bathroom.getFloor();
                System.out.println(title);
                longitude = bathroom.getLongitude();
                latitude = bathroom.getLatitude();
                type = "Bathroom";
                hue = BitmapDescriptorFactory.HUE_ROSE;

            } else {
                WaterFountain fountain = (WaterFountain) item;
                title = fountain.getBuilding() + " " + fountain.getFloor();
                System.out.println(title);
                longitude = fountain.getLongitude();
                latitude = fountain.getLatitude();
                type = "Fountain";
                hue = BitmapDescriptorFactory.HUE_CYAN;
            }

            LatLng location = new LatLng(latitude, longitude);
            MarkerOptions options = new MarkerOptions().position(location).title(title + " @ " + type).
                    icon(BitmapDescriptorFactory.defaultMarker(hue));
            synchronized (this) {
                markerOptions.add(options);
            }

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
