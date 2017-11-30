package com.simplex.utbathroomservices.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dchun on 11/19/17.
 */

public class SearchFragment extends Fragment {

    private Context context;
    protected SearchCallback searchCallback;
    private boolean asyncRunning, cancel;
    private Search search;

    public interface SearchCallback {
        void onPreExecuteSearch();
        void onProgressUpdateSearch();
        void onCancelledSearch();
        void onPostExecuteSearch(ArrayList<Bathroom> filteredBathrooms, ArrayList<WaterFountain> filteredFountains);
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        //add search params
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        Log.e("Called", "onAttach called");
        super.onAttach(context);
        this.context = context;
        if (context instanceof SearchCallback) {
            searchCallback = (SearchCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onUpdateListener");
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = null;
        if (getArguments() != null) {
            //get search params
            b = getArguments();
        }

        cancel = false;

        if (!asyncRunning) {
            search = new Search(b);

            search.execute();
        }
        setRetainInstance(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancel();
    }

    /**
     * Cancel the background task.
     */
    public void cancel() {

        if (asyncRunning) {
            cancel = true;
            search.cancel(true);
        }

    }

    public boolean isRunning() {
        return asyncRunning;
    }

    private class Search extends AsyncTask<String, Integer, Integer> {
        Bundle searchParams;
        public Search(Bundle b)
        {
            searchParams = b;
        }
        protected void onPreExecute() {
            // Proxy the call to the Activity.
            searchCallback.onPreExecuteSearch();
            asyncRunning = true;
        }

        protected void onProgressUpdate(Integer... percent) {
            searchCallback.onProgressUpdateSearch();
        }

        protected void onCancelled() {
            //System.out.println("Cancel in async");
            cancel(true);
            searchCallback.onCancelledSearch();
            asyncRunning = false;
        }

        protected void onPostExecute(Integer success) {
            searchCallback.onPostExecuteSearch((ArrayList<Bathroom>)searchParams.get("BRatings") , (ArrayList<WaterFountain>) searchParams.get("WRatings"));
            asyncRunning = false;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            //initalize with params once we figure it out
            ArrayList<Integer> userParams = (ArrayList<Integer>) searchParams.get("USpec");
            String building = null;
            int floor = userParams.get(0);
            int space = userParams.get(1);
            int stalls = userParams.get(2);
            int wifi = userParams.get(3);
            int busyness = userParams.get(4);
            int cleanliness = userParams.get(5);
            int rating = userParams.get(6);
            List<Bathroom> baths = (List<Bathroom>) searchParams.get("BRatings");
            //water fountain stuff
            List<WaterFountain> fountains = (List<WaterFountain>) searchParams.get("WRatings");
            int fountain = userParams.get(7);
            int taste = userParams.get(8);
            int temperature = userParams.get(9);
            BathroomDB bDB = new BathroomDB(); //maybe need callback?
            //do search
            /*assume default priority: building->floor, rating
             */
            if(fountain != 1) {
                Iterator<Bathroom> it = baths.iterator();
                while (it.hasNext()) {
                    int translatedSpace = 0;
                    Bathroom temp = it.next();
                    switch(temp.getSpace()) {
                        case "XSmall":
                            translatedSpace = 1;
                            break;
                        case "Small":
                            translatedSpace = 2;
                            break;
                        case "Medium":
                            translatedSpace = 3;
                            break;
                        case "Large":
                            translatedSpace = 4;
                            break;
                        case "XLarge":
                            translatedSpace = 5;
                    }
                    if (!temp.getBuilding().equals(building.toUpperCase()) ||
                            temp.getBusyness() < busyness ||
                            !(Integer.valueOf(temp.getFloor()) == floor) ||
                            temp.getNumberStalls() < stalls ||
                            temp.getCleanliness() < cleanliness ||
                            temp.getOverallRating() < rating ||
                            temp.getWifiQuality() < wifi ||
                            translatedSpace < space)
                        it.remove();
                }
            }
            else
            {
                Iterator<WaterFountain> it = fountains.iterator();
               /* while (it.hasNext()) {
                    WaterFountain temp = it.next();
                    int translatedTaste = 0;
                    int translatedTemperature = 0;
                    switch (temp.getTemperature())
                    {
                        case "cold": translatedTemperature = 5; break;
                        case "cool": translatedTemperature = 4; break;
                        case "lukewarm": translatedTemperature = 3; break;
                        case "warm": translatedTemperature = 2; break;
                        case "hot": translatedTemperature = 1; break;
                        default : translatedTemperature = 0;
                    }
                    switch(temp.getTaste())
                    {
                        case "Wow": translatedTaste = 5; break;
                        case "Pretty Good": translatedTaste = 4; break;
                        case "Meh": translatedTaste = 3; break;
                        case "Not Great": translatedTaste = 2; break;
                        case "Disgusting": translatedTaste = 1;
                    }
                    if (!temp.getBuilding().equals(building.toUpperCase()) ||
                            !(Integer.valueOf(temp.getFloor()) == floor) ||
                            temp.getOverallRating() < rating ||
                            translatedTaste < taste ||
                            translatedTemperature < temperature)
                        it.remove();
                }*/
            }
            //whatever is left, return it




            return 1;
        }
    }
}
