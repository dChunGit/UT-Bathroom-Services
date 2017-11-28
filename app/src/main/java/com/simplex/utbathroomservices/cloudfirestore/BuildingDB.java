package com.simplex.utbathroomservices.cloudfirestore;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by dchun on 11/26/17.
 */

public class BuildingDB {
    private ArrayList<Building> results= new ArrayList<>();
    private FirebaseFirestore mFireStore=FirebaseFirestore.getInstance();

    private DatabaseCallback databaseCallback;

    public BuildingDB(DatabaseCallback databaseCallback) {
        this.databaseCallback = databaseCallback;
    }

    public BuildingDB() {}

    public void getAllBuildings(){
        System.out.println("Call to get all buildings");
        mFireStore.collection("buildings")
                .get()
                .addOnCompleteListener(new OnCompleteListenerGet());

    }

    public void addAllBuildings(ArrayList<String> b) {
        Building building = new Building(b);
        mFireStore.collection("buildings").document("UT").set(building);

    }

    class OnCompleteListenerGet implements OnCompleteListener<QuerySnapshot> {

        @Override
        public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    Log.d("Success", document.getId() + " => " + document.getData());
                    Building building = document.toObject(Building.class);
                    results.add(building);

                }
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }

            if(databaseCallback != null) {
                databaseCallback.updateBuildings(results);
            }
        }
    }

}
