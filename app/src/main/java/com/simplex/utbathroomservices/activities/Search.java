package com.simplex.utbathroomservices.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

<<<<<<< HEAD:app/src/main/java/com/simplex/utbathroomservices/Search.java
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
=======
import com.simplex.utbathroomservices.R;
>>>>>>> David:app/src/main/java/com/simplex/utbathroomservices/activities/Search.java
import com.simplex.utbathroomservices.fragments.SearchFragment;

import java.util.ArrayList;

public class Search extends AppCompatActivity implements SearchFragment.SearchCallback{
    private SearchFragment searchFragment;
    private FragmentManager fragmentManager;

    private static final String TAG_TASK_FRAGMENT = "SearchFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fragmentManager = getSupportFragmentManager();
        searchFragment = (SearchFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

    }

    public void startSearch() {
        searchFragment = SearchFragment.newInstance();
        fragmentManager.beginTransaction().add(searchFragment, TAG_TASK_FRAGMENT).commit();
    }

    @Override
    public void onPreExecuteSearch() {

    }

    @Override
    public void onProgressUpdateSearch() {

    }

    @Override
    public void onCancelledSearch() {
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {

        }
        searchFragment = null;
    }

    @Override
    public void onPostExecuteSearch(ArrayList<Bathroom> filteredBathrooms, ArrayList<WaterFountain> filteredFountains) {
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {

        }
        searchFragment = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

}
