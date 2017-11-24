package com.simplex.utbathroomservices.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.DatabaseCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class UpdateFragment extends Fragment implements DatabaseCallback{

    public interface onUpdateListener {
        void onUpdateFinish(HashMap<String, Bathroom> results, LinkedList<Bathroom> resultsList, LinkedList<MarkerOptions> markers);
    }

    private onUpdateListener mListener;
    private HashMap<String, Bathroom> processedResults = new HashMap<>();
    private LinkedList<MarkerOptions> markerOptions = new LinkedList<>();

    public UpdateFragment() {
        // Required empty public constructor
    }

    public static UpdateFragment newInstance() {
        return new UpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        BathroomDB bathroomDB = new BathroomDB(this);
        bathroomDB.getAllBathrooms();
    }

    @Override
    public void updateFinished(LinkedList<Bathroom> r) {
        new Thread(() -> {
            for(Bathroom b : r) {
                processedResults.put(b.getBuilding() + " " + b.getFloor(), b);
            }
            setUpMarkers();
            mListener.onUpdateFinish(processedResults, r, markerOptions);
        }).start();
    }

    private void setUpMarkers() {
        for(String title : processedResults.keySet()) {

            Bathroom bathroom = processedResults.get(title);
            System.out.println(title);
            Location location = bathroom.getLocation();
            if(location != null) {
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions options = new MarkerOptions().position(sydney).title(title);
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
