package com.simplex.utbathroomservices;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.DatabaseCallback;

import java.util.ArrayList;

public class UpdateFragment extends Fragment implements DatabaseCallback{

    public interface onUpdateListener {
        void onUpdateFinish(ArrayList<Bathroom> results);
    }

    private onUpdateListener mListener;

    public UpdateFragment() {
        // Required empty public constructor
    }

    public static UpdateFragment newInstance() {
        return new UpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startUpdate();

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onUpdateListener) {
            mListener = (onUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onUpdateListener");
        }
    }

    private void startUpdate() {
        BathroomDB bathroomDB = new BathroomDB(this);
        bathroomDB.getAllBathrooms();
    }

    @Override
    public void updateFinished(ArrayList<Bathroom> r) {
        mListener.onUpdateFinish(r);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
