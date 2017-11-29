package com.simplex.utbathroomservices.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.simplex.utbathroomservices.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutFragment extends Fragment {

    private int mPage;
    ListView listView;

    public static AboutFragment newInstance(int page) {
        Bundle args = new Bundle();
        //put specs of instance
        args.putInt("pageNumber", page);
        //new library object
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set attributes from passed bundle
        mPage = getArguments().getInt("pageNumber");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if(mPage == 1) {
            //inflate about app view
            view = inflater.inflate(R.layout.about_tab, container, false);
            //get listview and set divider
            listView = view.findViewById(R.id.about_app);

            //get resources to populate list
            List<Map<String, String>> items = new ArrayList<>();
            String[] release = getResources().getStringArray(R.array.headers);
            String[] features = getResources().getStringArray(R.array.explanation);

            //populate list - put items in hashmap
            for(int a = 0; a < release.length; a++) {
                Map<String, String> map = new HashMap<>();
                map.put("Release", release[a]);
                map.put("Features", features[a]);
                items.add(map);
            }

            //semicustom adapter - set views from custom layout
            SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), items, R.layout.simple_list_item_2b,
                    new String[] {"Release", "Features"},
                    new int[] {android.R.id.text1,
                            android.R.id.text2});

            listView.setAdapter(simpleAdapter);

            //onlick item listener
            listView.setOnItemClickListener((arg0, art1, position, arg3) -> {
                switch (position) {
                    case 3: {
                        String url = "https://github.com/dChunGit/UT-Bathroom-Services";
                        //Chrome custom tabs - open using chrome (if not installed use default)
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        //System.out.println(Color.BLUE + " " + color);
                        Toast.makeText(getContext(), getString(R.string.dGithub) , Toast.LENGTH_SHORT).show();
                        //build and launch tab
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    } break;
                    case 4: {
                        String url = getResources().getString(R.string.contactURL);
                        //Chrome custom tabs - open using chrome (if not installed use default)
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        //System.out.println(Color.BLUE + " " + color);
                        Toast.makeText(getContext(), getString(R.string.dGithub) , Toast.LENGTH_SHORT).show();
                        //build and launch tab
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }
            });

        }else if(mPage == 2){
            view = inflater.inflate(R.layout.libraries_tab, container, false);
            listView = view.findViewById(R.id.openSource);

            List<Map<String, String>> items = new ArrayList<>();
            final String[] names = getResources().getStringArray(R.array.about);
            final String[] license = getResources().getStringArray(R.array.license);

            for(int a = 0; a < names.length; a++) {
                Map<String, String> map = new HashMap<>();
                map.put("Name", names[a]);
                map.put("License", license[a]);
                items.add(map);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), items, R.layout.simple_list_item_2,
                    new String[] {"Name", "License"},
                    new int[] {android.R.id.text1,
                            android.R.id.text2});

            listView.setAdapter(simpleAdapter);

            listView.setOnItemClickListener((arg0, art1, position, arg3) -> {
                String url;
                switch(position) {
                    case 0: url = "https://github.com/Clans/FloatingActionButton"; break;
                    case 1: url = "https://github.com/chrisjenx/Calligraphy"; break;
                    case 2: url = "https://github.com/afollestad/material-dialogs"; break;
                    case 3: url = "https://github.com/FlyingPumba/SimpleRatingBar"; break;
                    case 4: url = "https://github.com/ome450901/SimpleRatingBar"; break;
                    case 5: url = "https://github.com/81813780/AVLoadingIndicatorView"; break;
                    case 6: url = "https://github.com/aosp-mirror/platform_frameworks_support"; break;
                    case 7: url = "https://github.com/aosp-mirror/platform_frameworks_support"; break;
                    case 8: url = "https://github.com/aosp-mirror/platform_frameworks_support"; break;
                    case 9: url = "https://github.com/aosp-mirror/platform_frameworks_support"; break;
                    case 10: url = "https://github.com/aosp-mirror/platform_frameworks_support"; break;
                    case 11: url = "https://github.com/aosp-mirror/platform_frameworks_support"; break;
                    case 12: url = "https://developers.google.com/maps/android"; break;
                    case 13: url = "https://firebase.google.com"; break;
                    case 14: url = "https://developers.google.com/android/guides/setup"; break;
                    default: url = "https://google.com";
                }

                //Chrome custom tabs - open using chrome (if not installed use default
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                Toast.makeText(getContext(), getString(R.string.openLink) + " " + names[position], Toast.LENGTH_SHORT).show();

                //build and launch tab
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getActivity(), Uri.parse(url));
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            });

        }else {
            view = inflater.inflate(R.layout.changelog_tab, container, false);
            listView = view.findViewById(R.id.changelog);

            List<Map<String, String>> items = new ArrayList<>();
            String[] release = getResources().getStringArray(R.array.release);
            String[] features = getResources().getStringArray(R.array.features);

            for(int a = 0; a < release.length; a++) {
                Map<String, String> map = new HashMap<>();
                map.put("Release", release[a]);
                map.put("Features", features[a]);
                items.add(map);
            }

            SimpleAdapter simpleAdapter = new SimpleAdapter(getContext(), items, R.layout.simple_list_item_2a,
                    new String[] {"Release", "Features"},
                    new int[] {android.R.id.text1,
                            android.R.id.text2});

            listView.setAdapter(simpleAdapter);

        }

        return view;
    }
}
