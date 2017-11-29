package com.simplex.utbathroomservices.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.location.LocationCallback;
import com.simplex.utbathroomservices.location.LocationService;

import static android.content.Context.BIND_AUTO_CREATE;

public class ServiceFragment extends Fragment implements LocationCallback {

    private UpdateLocationListener mListener;
    boolean mBounded;
    LocationService mServer;

    @Override
    public void updateLocationGUI(Location location) {
        if(mListener != null) {
            mListener.onLocationUpdate(location);
        }
    }

    public GoogleApiClient getCurrentLocation() {
        return mServer.getCurrentLocation();
    }

    public interface UpdateLocationListener {
        void onLocationUpdate(Location location);
    }

    public ServiceFragment() {
        // Required empty public constructor
    }

    public static ServiceFragment newInstance() {
        return new ServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent mIntent = new Intent(getActivity(), LocationService.class);
        getActivity().getApplicationContext().bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getApplicationContext().unbindService(mConnection);
        mServer = null;
        mBounded = false;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateLocationListener) {
            mListener = (UpdateLocationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateLocationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("BINDER", "SERVICE UNBOUND");
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("BINDER", "SERVICE BOUND");
            mBounded = true;
            LocationService.LocalBinder mLocalBinder = (LocationService.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();
            mServer.setCallbacks(ServiceFragment.this);
        }
    };
}
