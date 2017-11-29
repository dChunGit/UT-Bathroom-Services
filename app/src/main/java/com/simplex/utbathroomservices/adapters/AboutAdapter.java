package com.simplex.utbathroomservices.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.fragments.AboutFragment;

public class AboutAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private int numPages = 3;
    private Context context;
    private String tabCategories[];

    public AboutAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        //save color to theme components
        this.context = context;
        tabCategories = new String[3];
        tabCategories[0] = context.getString(R.string.about_adapter);
        tabCategories[1] = context.getString(R.string.library_adapter);
        tabCategories[2] = context.getString(R.string.changes_adapter);
    }

    @Override
    public int getCount() {
        //get the number of pages added
        return numPages;
    }

    @Override
    public Fragment getItem(int position) {
        //get a new libraries object, new position and color
        return AboutFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabCategories[position];
    }
}