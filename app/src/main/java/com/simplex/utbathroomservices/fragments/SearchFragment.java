package com.simplex.utbathroomservices.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;

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
        void onPostExecuteSearch();
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
            searchCallback.onPostExecuteSearch();
            asyncRunning = false;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            //initalize with params once we figure it out
            String building = null;
            int floor = -1;
            String space = null;
            int stalls = -1;
            int wifi = -1;
            int busyness = -1;
            int cleanliness = -1;
            int rating = -1;
            List<Bathroom> baths = null;
            //water fountain stuff
            List<WaterFountain> fountains = null;
            boolean fountain = false;
            int taste = -1;
            int temperature = -1;
            BathroomDB bDB = new BathroomDB(); //maybe need callback?
            //do search
            /*assume default priority: building->floor, rating
             */
            if(!fountain) {
                Iterator<Bathroom> it = baths.iterator();
                while (it.hasNext()) {
                    Bathroom temp = it.next();
                    if (!temp.getBuilding().equals(building.toUpperCase()) ||
                            temp.getBusyness() < busyness ||
                            !(Integer.valueOf(temp.getFloor()) == floor) ||
                            temp.getNumberStalls() < stalls ||
                            temp.getCleanliness() < cleanliness ||
                            temp.getOverallRating() < rating ||
                            temp.getWifiQuality() < wifi ||
                            !temp.getSpace().equals(space.toUpperCase()))
                        it.remove();
                }
            }
            else
            {
                Iterator<WaterFountain> it = fountains.iterator();
                while (it.hasNext()) {
                    WaterFountain temp = it.next();
                    if (!temp.getBuilding().equals(building.toUpperCase()) ||
                            !(Integer.valueOf(temp.getFloor()) == floor) ||
                            temp.getOverallRating() < rating ||
                            temp.getTaste() < taste ||
                            temp.get)
                        it.remove();
                }
            }



            return null;
        }
    }
}
