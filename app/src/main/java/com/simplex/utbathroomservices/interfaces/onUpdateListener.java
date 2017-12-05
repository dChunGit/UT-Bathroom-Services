package com.simplex.utbathroomservices.interfaces;

import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dchun on 12/3/17.
 */

public interface onUpdateListener {
    void onUpdateBFinish(HashMap<String, Bathroom> firebaseResults, ArrayList<Bathroom> resultsList,
                         ArrayList<MarkerOptions> markers, boolean doAll);
    void onUpdateWFinish(HashMap<String, WaterFountain> firebaseResults, ArrayList<WaterFountain> resultsList,
                         ArrayList<MarkerOptions> markers, boolean doAll);
    void onUpdateBuildingFinish(ArrayList<String> buildings);
}
