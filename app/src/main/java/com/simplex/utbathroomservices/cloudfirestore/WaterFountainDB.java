package com.simplex.utbathroomservices.cloudfirestore;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Created by zoeng on 11/10/17.
 */


public class WaterFountainDB {
    private FirebaseFirestore mFireStore;

    public WaterFountainDB() {
        mFireStore = FirebaseFirestore.getInstance();
    }

    public void addWaterFountainToDB(Location location, String building, String floor, String temperature, boolean isBottleRefillStation, String taste, Integer overallRating,ArrayList<Rating> rating, String[] image) {
        WaterFountain wf = new WaterFountain(location, building, floor, temperature, isBottleRefillStation, taste, overallRating,rating, image);
        mFireStore.collection("waterfountain").add(wf);

    }
    public void addReviewForWaterFountain(WaterFountain wf, String review){
        final ArrayList<String> id= new ArrayList<String>();
        mFireStore.collection("waterfountain")
                .whereEqualTo("building", wf.getBuilding())
                .whereEqualTo("floor", wf.getFloor())
                .whereEqualTo("location", wf.getLocation())
                .whereEqualTo("overallRating", wf.getOverallRating())
                .whereEqualTo("temperature", wf.getTemperature())
                .whereEqualTo("taste", wf.getTaste())
                .whereEqualTo("isBottleRefillStation", wf.isBottleRefillStation())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                id.add(document.getId());
                            }
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        Rating rating= new Rating(review);
        wf.rating.add(rating);
        DocumentReference waterFountainRef = mFireStore.collection("waterfountain").document(id.get(0));
        waterFountainRef
                .update("rating", wf.rating)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Success", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error", "Error updating document", e);
                    }
                });
    }
    public ArrayList<WaterFountain> getAllWaterFountains() {
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
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

    public ArrayList<WaterFountain> getWaterFountainsByBuilding(String building) {
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
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

    public ArrayList<WaterFountain> getWaterFountainsByBuildingAndFloor(String building, String floor) {
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
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
    public ArrayList<WaterFountain> getWaterFountainsByTaste(String taste){
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .whereEqualTo("taste", taste)
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
    public ArrayList<WaterFountain> getWaterFountainsByTemperature(String temperature){
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .whereEqualTo("temperature", temperature)
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
    public ArrayList<WaterFountain> getWaterFountainsByTasteAndTemperature(String taste, String temperature){
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .whereEqualTo("taste", taste)
                .whereEqualTo("temperature", temperature)
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
    public ArrayList<WaterFountain> getWaterFountainsByOverallRating(Integer overallRating){
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .whereGreaterThanOrEqualTo("overallRating", overallRating)
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
    public ArrayList<WaterFountain> getWaterFountainsByIsWaterBottleStation(Boolean isBottleRefillStation){
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .whereEqualTo("isBottleRefillStation", isBottleRefillStation)
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
