package com.simplex.utbathroomservices.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by dwsch on 10/24/2017.
 */

//service to call location updates
public class LocationService extends Service implements LocationUpdateListener{
    private GoogleLocationService googleLocationService;
    private LocationCallback locationCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        //start the handler for getting locations
        //create component
        updateLocation(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

    public void setCallbacks(LocationCallback callbacks) {
        locationCallback = callbacks;
    }

    //get current location of user
    private void updateLocation(Context context) {
        googleLocationService = new GoogleLocationService(context);
        googleLocationService.startUpdates();
    }

    public GoogleApiClient getCurrentLocation() {
        if(googleLocationService != null) {
            return googleLocationService.getApiClient();
        } else return null;
    }

    @Override
    public void canReceiveLocationUpdates() {
    }

    @Override
    public void cannotReceiveLocationUpdates() {
    }

    @Override
    public void updateLocation(Location location) {
        if (location != null) {
            if(locationCallback != null) {
                locationCallback.updateLocationGUI(location);
            }
        }
    }

    IBinder mBinder = new LocalBinder();


    public class LocalBinder extends Binder {
        public LocationService getServerInstance() {
            return LocationService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //stop location updates on stopping the service
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (googleLocationService != null) {
            googleLocationService.stopLocationUpdates();
        }
    }
}