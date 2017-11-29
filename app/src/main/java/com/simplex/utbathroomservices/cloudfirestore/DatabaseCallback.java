package com.simplex.utbathroomservices.cloudfirestore;

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