package com.simplex.utbathroomservices.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.Database;
import com.simplex.utbathroomservices.SearchParams;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.simplex.utbathroomservices.interfaces.SearchCallback;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by dchun on 11/19/17.
 */

public class SearchFragment extends Fragment {

    private Context context;
    protected SearchCallback searchCallback;
    private boolean asyncRunning, cancel;
    private Search search;
    private SearchParams searchParams;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(SearchParams searchParams) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        //add search userParams
        args.putParcelable("Params", searchParams);
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
        Database database = (Database) getActivity().getApplication();
        if (getArguments() != null) {
            //get search userParams
            searchParams = getArguments().getParcelable("Params");
            System.out.println(searchParams);
        }

        cancel = false;

        if (!asyncRunning) {
            search = new Search(searchParams, database.getSaveBathroom(), database.getSaveFountain());
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
        private SearchParams userParams;
        private ArrayList<Bathroom> bathrooms = new ArrayList<>();
        private ArrayList<WaterFountain> fountains = new ArrayList<>();
        private ArrayList<MarkerOptions> markerOptions = new ArrayList<>();

        public Search(SearchParams userParams, ArrayList<Bathroom> bathrooms, ArrayList<WaterFountain> fountains) {
            System.out.println("Searching");
            System.out.println(bathrooms);
            System.out.println(fountains);
            this.userParams = userParams;
            this.bathrooms = bathrooms;
            this.fountains = fountains;
        }

        protected void onCancelled() {
            //System.out.println("Cancel in async");
            cancel(true);
            searchCallback.onCancelledSearch();
            asyncRunning = false;
        }

        protected void onPostExecute(Integer success) {
            setUpMarkers(bathrooms);
            setUpMarkers(fountains);
            searchCallback.onPostExecuteSearch(bathrooms, fountains, markerOptions);
            asyncRunning = false;
        }

        private <T> void setUpMarkers(ArrayList<T> results) {
            for(T item: results) {
                float hue;
                double longitude, latitude;
                String title, type;
                if(item instanceof Bathroom) {
                    Bathroom bathroom = (Bathroom) item;
                    title = bathroom.getBuilding() + " " + bathroom.getFloor();
                    System.out.println(title);
                    longitude = bathroom.getLongitude();
                    latitude = bathroom.getLatitude();
                    type = "Bathroom";
                    hue = BitmapDescriptorFactory.HUE_ROSE;

                } else {
                    WaterFountain fountain = (WaterFountain) item;
                    title = fountain.getBuilding() + " " + fountain.getFloor();
                    System.out.println(title);
                    longitude = fountain.getLongitude();
                    latitude = fountain.getLatitude();
                    type = "Fountain";
                    hue = BitmapDescriptorFactory.HUE_CYAN;
                }

                LatLng location = new LatLng(latitude, longitude);
                MarkerOptions options = new MarkerOptions().position(location).title(title + " @ " + type).
                        icon(BitmapDescriptorFactory.defaultMarker(hue));
                synchronized (this) {
                    markerOptions.add(options);
                }

            }

        }

        @Override
        protected Integer doInBackground(String... strings) {
            System.out.println("Searching algo: " + userParams);
            //initalize with userParams once we figure it out
            String searchType = userParams.getSearchType();
            String typeFilter = userParams.getType();
            String building = userParams.getBuilding();
            //int floor = userParams.getFloor().;
            int space = userParams.getSpace();
            int stalls = userParams.getStalls();
            int wifi = userParams.getWifi();
            int busyness = userParams.getBusyness();
            int cleanliness = userParams.getCleanliness();
            int rating = userParams.getRating();
            //water fountain stuff
            boolean isFillable = userParams.getIsFillable();
            int taste = userParams.getTaste();
            int temperature = userParams.getTemperature();
            //do search
            /*assume default priority: building->floor, rating
             */
            if(typeFilter.equals("Bathroom")) {
                Iterator<Bathroom> it = bathrooms.iterator();
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
                            break;
                        default: translatedSpace = 0;
                    }
                    if(searchType.equals("Simple")) {
                        if (temp.getBusyness() < busyness ||
                                temp.getCleanliness() < cleanliness ||
                                temp.getOverallRating() < rating ||
                                temp.getWifiQuality() < wifi ||
                                translatedSpace < space)
                            it.remove();
                    } else {
                        if(building.equals("")) {
                            if (temp.getBusyness() < busyness ||
                                    temp.getNumberStalls() < stalls ||
                                    temp.getCleanliness() < cleanliness ||
                                    temp.getOverallRating() < rating ||
                                    temp.getWifiQuality() < wifi ||
                                    translatedSpace < space)
                                it.remove();
                        } else {
                            if (!temp.getBuilding().equals(building.toUpperCase()) ||
                                    temp.getBusyness() < busyness ||
                                    temp.getNumberStalls() < stalls ||
                                    temp.getCleanliness() < cleanliness ||
                                    temp.getOverallRating() < rating ||
                                    temp.getWifiQuality() < wifi ||
                                    translatedSpace < space)
                                it.remove();
                        }
                    }
                }

            } else if(typeFilter.equals("Fountain")){
                Iterator<WaterFountain> it = fountains.iterator();
                while (it.hasNext()) {
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
                        case "Disgusting": translatedTaste = 1; break;
                        default: translatedTaste = 0;
                    }
                    if(searchType.equals("Simple")) {
                        if (temp.getOverallRating() < rating ||
                                translatedTaste < taste ||
                                translatedTemperature < temperature ||
                                isFillable != temp.isBottleRefillStation())
                            it.remove();
                    } else {
                        if(building.equals("")) {
                            if (temp.getOverallRating() < rating ||
                                    translatedTaste < taste ||
                                    translatedTemperature < temperature ||
                                    isFillable != temp.isBottleRefillStation())
                                it.remove();
                        } else {
                            if (!temp.getBuilding().equals(building.toUpperCase()) ||
                                    temp.getOverallRating() < rating ||
                                    translatedTaste < taste ||
                                    translatedTemperature < temperature ||
                                    isFillable != temp.isBottleRefillStation())
                                it.remove();
                        }
                    }
                }
            }
            //whatever is left, return it
            return 1;
        }
    }
}