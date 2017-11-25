package com.simplex.utbathroomservices.cloudfirestore;

import java.util.LinkedList;

/**
 * Created by dchun on 11/22/17.
 */
public interface DatabaseCallback {
    void updateFinished(LinkedList<Bathroom> r);
}