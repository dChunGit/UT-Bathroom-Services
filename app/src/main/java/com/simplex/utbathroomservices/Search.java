package com.simplex.utbathroomservices;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.simplex.utbathroomservices.fragments.SearchFragment;

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
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate() {

    }

    @Override
    public void onCancelled() {
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {

        }
        searchFragment = null;
    }

    @Override
    public void onPostExecute() {

    }
}
