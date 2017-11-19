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

public class BathroomDB {
    FirebaseFirestore mFireStore=FirebaseFirestore.getInstance();

    public void addBathroomToDB(Location location, String building, String floor, String space, Integer numberStalls, Integer wifiQuality, Integer busyness, Integer cleanliness, Integer overallRating, ArrayList<Rating> rating, String[] image){
        Bathroom b= new Bathroom( location,  building,  floor,  space,  numberStalls,  wifiQuality,  busyness,cleanliness, overallRating , rating, image);
        mFireStore.collection("bathroom").add(b);

    }
    public void addReviewForBathroom(Bathroom b, String review){
        final ArrayList<String> id= new ArrayList<String>();
        mFireStore.collection("bathroom")
                .whereEqualTo("building", b.getBuilding())
                .whereEqualTo("floor", b.getFloor())
                .whereEqualTo("location", b.getLocation())
                .whereEqualTo("space", b.getSpace())
                .whereEqualTo("numberStalls", b.getNumberStalls())
                .whereEqualTo("wifiQuality", b.getWifiQuality())
                .whereEqualTo("busyness", b.getBusyness())
                .whereEqualTo("cleanliness", b.getCleanliness())
                .whereEqualTo("overallRating", b.getOverallRating())
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
        b.rating.add(rating);
        DocumentReference bathroomRef = mFireStore.collection("bathroom").document(id.get(0));
        bathroomRef
                .update("rating", b.rating)
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
    public ArrayList<Bathroom> getBathroomByOverallRating(Integer overallRating){
        final ArrayList<Bathroom> results= new ArrayList<Bathroom>();
        mFireStore.collection("bathroom")
                .whereGreaterThanOrEqualTo("overallRating", overallRating)
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
    public ArrayList<Bathroom> getBathroomByCleanliness(Integer cleanliness){
        final ArrayList<Bathroom> results= new ArrayList<Bathroom>();
        mFireStore.collection("bathroom")
                .whereGreaterThanOrEqualTo("cleanliness", cleanliness)
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
    public ArrayList<Bathroom> getBathroomByBusyness(Integer busyness){
        final ArrayList<Bathroom> results= new ArrayList<Bathroom>();
        mFireStore.collection("bathroom")
                .whereLessThanOrEqualTo("busyness", busyness)
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
    public ArrayList<Bathroom> getBathroomByWifiQuality(Integer wifiQuality){
        final ArrayList<Bathroom> results= new ArrayList<Bathroom>();
        mFireStore.collection("bathroom")
                .whereGreaterThanOrEqualTo("wifiQuality", wifiQuality)
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
