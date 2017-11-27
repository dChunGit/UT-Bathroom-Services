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

public class BathroomDB {
    private LinkedList<Bathroom> results= new LinkedList<>();
    private FirebaseFirestore mFireStore=FirebaseFirestore.getInstance();

    private DatabaseCallback databaseCallback;

    public BathroomDB(DatabaseCallback callback) {
        databaseCallback = callback;
    }

    public BathroomDB() {}
  
    public void addBathroomToDB(Location location, String building, String floor, Integer reviews, String space,
                                Integer numberStalls, Integer wifiQuality, Integer busyness,
                                Integer cleanliness, Integer overallRating, ArrayList<Rating> rating,
                                ArrayList<String> image){
        Bathroom b= new Bathroom(location.getLongitude(), location.getLatitude(),  building,  floor,  reviews, space,  numberStalls,  wifiQuality,
                busyness, cleanliness, overallRating , rating, image);
        mFireStore.collection("bathroom").document(building + " " + floor).set(b);

    }

    public void updateReviewForBathroom(Bathroom b) {
        mFireStore.collection("bathroom").document(b.getBuilding() + " " + b.getFloor()).set(b);
    }

    public void addReviewForBathroom(Bathroom b, String review){
        final ArrayList<String> id= new ArrayList<>();
        mFireStore.collection("bathroom")
                .whereEqualTo("building", b.getBuilding())
                .whereEqualTo("floor", b.getFloor())
                .get()
                .addOnCompleteListener((task) -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            id.add(document.getId());
                        }
                        Rating rating= new Rating(review);
                        b.getRating().add(rating);
                        DocumentReference bathroomRef = mFireStore.collection("bathroom").document(id.get(0));
                        bathroomRef
                                .update("rating", b.getRating())
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
                });
    }

    public void getAllBathrooms(){
        mFireStore.collection("bathroom")
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());

    }

    public void getBathroomByBuilding(String building){
        mFireStore.collection("bathroom")
                .whereEqualTo("building", building)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());

    }

    public void getBathroomByBuildingAndFloor(String building, String floor){
        mFireStore.collection("bathroom")
                .whereEqualTo("building", building)
                .whereEqualTo("floor", floor)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());

    }

    public void getBathroomByOverallRating(Integer overallRating){
        mFireStore.collection("bathroom")
                .whereGreaterThanOrEqualTo("overallRating", overallRating)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());

    }

    public void getBathroomByCleanliness(Integer cleanliness){
        mFireStore.collection("bathroom")
                .whereGreaterThanOrEqualTo("cleanliness", cleanliness)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());

    }

    public void getBathroomByBusyness(Integer busyness){
        mFireStore.collection("bathroom")
                .whereLessThanOrEqualTo("busyness", busyness)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());

    }

    public void getBathroomByWifiQuality(Integer wifiQuality){
        mFireStore.collection("bathroom")
                .whereGreaterThanOrEqualTo("wifiQuality", wifiQuality)
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());
    }

    class OnCompleteListenerGet implements OnCompleteListener<QuerySnapshot> {

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
            if(databaseCallback != null) {
                databaseCallback.updateFinishedB(results);
            }
        }
    }
}
