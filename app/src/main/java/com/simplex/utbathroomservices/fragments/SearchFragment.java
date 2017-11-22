package com.simplex.utbathroomservices.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by dchun on 11/19/17.
 */

public class SearchFragment extends Fragment {

    private Context context;
    protected SearchCallback searchCallback;
    private boolean asyncRunning, cancel;
    private Search search;

    public interface SearchCallback {
        void onPreExecute();
        void onProgressUpdate();
        void onCancelled();
        void onPostExecute();
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
        if (getArguments() != null) {
            //get search params
        }

        cancel = false;

        if (!asyncRunning) {
            search = new Search();

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

        protected void onPreExecute() {
            // Proxy the call to the Activity.
            searchCallback.onPreExecute();
            asyncRunning = true;
        }

        protected void onProgressUpdate(Integer... percent) {
            searchCallback.onProgressUpdate();
        }

        protected void onCancelled() {
            //System.out.println("Cancel in async");
            cancel(true);
            searchCallback.onCancelled();
            asyncRunning = false;
        }

        protected void onPostExecute(Integer success) {
            searchCallback.onPostExecute();
            asyncRunning = false;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            //do search
            return null;
        }
    }
}
