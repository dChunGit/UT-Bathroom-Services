package com.simplex.utbathroomservices.location;

import android.location.Location;

/**
 * Created by dwsch on 10/24/2017.
 */

public interface LocationCallback {
    void updateLocationGUI(Location location);
}
