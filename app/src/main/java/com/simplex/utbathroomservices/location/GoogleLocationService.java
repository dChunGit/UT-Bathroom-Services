package com.simplex.utbathroomservices.location;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * Created by dwsch on 10/23/2017.
 */
public class GoogleLocationService {
    private GoogleServicesCallbacks callbacks = new GoogleServicesCallbacks();
    private Context activity;
    private LocationUpdateListener locationUpdateListener;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    public GoogleLocationService(Context activity) {
        this.locationUpdateListener = (LocationUpdateListener) activity;
        this.activity = activity;
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient() {
        //Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks(callbacks)
                .addOnConnectionFailedListener(callbacks)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        mGoogleApiClient.connect();
    }

    public GoogleApiClient getApiClient() {
        return mGoogleApiClient;
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    private class GoogleServicesCallbacks implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener, LocationListener {

        @Override
        public void onConnected(Bundle bundle) {
            startLocationUpdates();
        }

        @Override
        public void onConnectionSuspended(int i) {
            mGoogleApiClient.connect();
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            if (connectionResult.getErrorCode() == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED) {
                Toast.makeText(activity, "Google play service not updated", Toast.LENGTH_LONG).show();

            }
            locationUpdateListener.cannotReceiveLocationUpdates();
        }

        @Override
        public void onLocationChanged(Location location) {
            if (location.hasAccuracy()) {
                if (location.getAccuracy() < 30) {
                    locationUpdateListener.updateLocation(location);
                }
            }
        }
    }

    private static boolean locationEnabled(Context context) {
        boolean gps_enabled = false;
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return gps_enabled;
    }

    private boolean servicesConnected(Context context) {
        return isPackageInstalled(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE, context);
    }

    private boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void startUpdates() {
        //connect client
        if (servicesConnected(activity)) {
            if (locationEnabled(activity)) {
                locationUpdateListener.canReceiveLocationUpdates();
                startLocationUpdates();
            } else {
                locationUpdateListener.cannotReceiveLocationUpdates();
                Toast.makeText(activity, "Unable to get your location. Please turn on your GPS", Toast.LENGTH_LONG).show();
            }
        } else {
            locationUpdateListener.cannotReceiveLocationUpdates();
            Toast.makeText(activity, "Google Play Services Unavailable", Toast.LENGTH_LONG).show();
        }
    }

    //stop location updates
    public void stopUpdates() {
        stopLocationUpdates();
    }

    //start location updates
    private void startLocationUpdates() {

        if (ContextCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, callbacks);
        }
    }

    public void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, callbacks);
        }
    }


    public void startGoogleApi() {
        mGoogleApiClient.connect();
    }

    public void closeGoogleApi() {
        mGoogleApiClient.disconnect();
    }

}