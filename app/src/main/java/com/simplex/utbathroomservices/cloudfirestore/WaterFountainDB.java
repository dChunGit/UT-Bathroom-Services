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
import java.util.LinkedList;

/**
 * Created by zoeng on 11/10/17.
 */


public class WaterFountainDB {
    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private LinkedList<WaterFountain> results = new LinkedList<>();

    private DatabaseCallback databaseCallback;

    public WaterFountainDB(DatabaseCallback callback) {
        databaseCallback = callback;
    }

    public WaterFountainDB() {}

    public void addWaterFountainToDB(Location location, String building, String floor, Integer reviews, String temperature, boolean isBottleRefillStation, String taste, Integer overallRating,ArrayList<Rating> rating, ArrayList<String> image) {
        WaterFountain wf = new WaterFountain(location, building, floor, reviews, temperature, isBottleRefillStation, taste, overallRating,rating, image);
        mFireStore.collection("waterfountain").document(building + " " + floor).set(wf);

    }

    public void addReviewForWaterFountain(WaterFountain wf, String review){
        final ArrayList<String> id= new ArrayList<String>();
        mFireStore.collection("waterfountain")
                .whereEqualTo("building", wf.getBuilding())
                .whereEqualTo("floor", wf.getFloor())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                id.add(document.getId());
                            }
                            Rating rating= new Rating(review);
                            wf.getRating().add(rating);
                            DocumentReference waterFountainRef = mFireStore.collection("waterfountain").document(id.get(0));
                            waterFountainRef
                                    .update("rating", wf.getRating())
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
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void getAllWaterFountains() {
        mFireStore.collection("waterfountain")
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    public void getWaterFountainsByBuilding(String building) {
        mFireStore.collection("waterfountain")
                .whereEqualTo("building", building)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    public void getWaterFountainsByBuildingAndFloor(String building, String floor) {
        mFireStore.collection("waterfountain")
                .whereEqualTo("building", building)
                .whereEqualTo("floor", floor)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    public void getWaterFountainsByTaste(String taste){
        mFireStore.collection("waterfountain")
                .whereEqualTo("taste", taste)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    public void getWaterFountainsByTemperature(String temperature){
        mFireStore.collection("waterfountain")
                .whereEqualTo("temperature", temperature)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    public void getWaterFountainsByTasteAndTemperature(String taste, String temperature){
        mFireStore.collection("waterfountain")
                .whereEqualTo("taste", taste)
                .whereEqualTo("temperature", temperature)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    public void getWaterFountainsByOverallRating(Integer overallRating){
        final ArrayList<WaterFountain> results = new ArrayList<WaterFountain>();
        mFireStore.collection("waterfountain")
                .whereGreaterThanOrEqualTo("overallRating", overallRating)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    public void getWaterFountainsByIsWaterBottleStation(Boolean isBottleRefillStation){
        mFireStore.collection("waterfountain")
                .whereEqualTo("isBottleRefillStation", isBottleRefillStation)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    class OnCompleteListenerGet implements OnCompleteListener<QuerySnapshot> {

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

            if(databaseCallback != null) {
                databaseCallback.updateFinishedF(results);
            }
        }
    }
}
