package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by zoeng on 11/10/17.
 */

public class BathroomDB {
    FirebaseFirestore mFireStore=FirebaseFirestore.getInstance();

    public void addBathroomToDB(Location location, String building, String floor, String space, Integer numberStalls, Integer wifiQuality, Integer busyness, Rating rating){
        Bathroom b= new Bathroom( location,  building,  floor,  space,  numberStalls,  wifiQuality,  busyness,  rating);
        mFireStore.collection("bathroom").add(b);

    }

public ArrayList<Bathroom> getAllBathrooms(){
        final ArrayList<Bathroom> results= new ArrayList<Bathroom>();
        mFireStore.collection("bathroom")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            Log.d("Success", document.getId() + " => " + document.getData());
                            Bathroom bathroom = document.toObject(Bathroom.class);
                            results.add(bathroom);

                        }
                    } else {
                        Log.d("Error", "Error getting documents: ", task.getException());
                    }
                }
            });
        return results;
}

    public ArrayList<Bathroom> getBathroomByBuilding(String building){
        final ArrayList<Bathroom> results= new ArrayList<Bathroom>();
        mFireStore.collection("bathroom")
                .whereEqualTo("building", building)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                Bathroom bathroom = document.toObject(Bathroom.class);
                                results.add(bathroom);

                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return results;
    }
    public ArrayList<Bathroom> getBathroomByBuildingAndFloor(String building, String floor){
        final ArrayList<Bathroom> results= new ArrayList<Bathroom>();
        mFireStore.collection("bathroom")
                .whereEqualTo("building", building)
                .whereEqualTo("floor", floor)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                Bathroom bathroom = document.toObject(Bathroom.class);
                                results.add(bathroom);

                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return results;
    }
}
