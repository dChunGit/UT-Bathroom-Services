package com.simplex.utbathroomservices.cloudfirestore;

import java.util.ArrayList;

/**
 * Created by dchun on 11/22/17.
 */
public interface DatabaseCallback {
    void updateFinished(ArrayList<Bathroom> r);
}