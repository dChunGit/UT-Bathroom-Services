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

public class WaterFountainDB {
    FirebaseFirestore mFireStore=FirebaseFirestore.getInstance();

    public void addWaterFountainToDB(Location location, String building, String floor, String temperature, boolean isBottleRefillStation, String taste, Rating rating){
        WaterFountain wf= new WaterFountain( location,  building,  floor, temperature,isBottleRefillStation,taste,   rating);
        mFireStore.collection("waterfountain").add(wf);

    }

    public ArrayList<WaterFountain> getAllWaterFountains(){
        final ArrayList<WaterFountain> results= new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                WaterFountain waterFountain = document.toObject(WaterFountain.class);
                                results.add(waterFountain);

                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return results;
    }

    public ArrayList<WaterFountain> getWaterFountainsByBuilding(String building){
        final ArrayList<WaterFountain> results= new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .whereEqualTo("building", building)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                WaterFountain waterFountain = document.toObject(WaterFountain.class);
                                results.add(waterFountain);

                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return results;
    }
    public ArrayList<WaterFountain> getWaterFountainsByBuildingAndFloor(String building, String floor){
        final ArrayList<WaterFountain> results= new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .whereEqualTo("building", building)
                .whereEqualTo("floor", floor)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d("Success", document.getId() + " => " + document.getData());
                                WaterFountain waterFountain = document.toObject(WaterFountain.class);
                                results.add(waterFountain);

                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return results;
    }
}
