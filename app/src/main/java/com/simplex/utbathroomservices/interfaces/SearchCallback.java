package com.simplex.utbathroomservices.interfaces;

import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

import java.util.ArrayList;

/**
 * Created by dchun on 12/3/17.
 */

public interface SearchCallback {
    void onCancelledSearch();
    void onPostExecuteSearch(ArrayList<Bathroom> filteredBathrooms,
                             ArrayList<WaterFountain> filteredFountains, ArrayList<MarkerOptions> markerOptions);
}