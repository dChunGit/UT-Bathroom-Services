package com.simplex.utbathroomservices.interfaces;

import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.Building;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by dchun on 11/22/17.
 */
public interface DatabaseCallback {
    void updateFinishedB(ArrayList<Bathroom> r);
    void updateFinishedF(ArrayList<WaterFountain> r);
    void updateBuildings(ArrayList<Building> b);
    void addFinished(boolean success);
}