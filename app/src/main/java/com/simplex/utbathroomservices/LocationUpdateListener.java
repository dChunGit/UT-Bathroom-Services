package com.simplex.utbathroomservices;

import android.location.Location;

/**
 * Created by dwsch on 10/23/2017.
 */

public interface LocationUpdateListener {
    void canReceiveLocationUpdates();
    void cannotReceiveLocationUpdates();
    void updateLocation(Location location);
    void updateLocationName(String localityName, Location location);

}
