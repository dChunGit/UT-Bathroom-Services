package com.simplex.utbathroomservices;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.simplex.utbathroomservices.adapters.AboutAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class About extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ColabReg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        setContentView(R.layout.activity_about);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar_about);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(getString(R.string.about_app));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //add back button to actionbar
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }

        // Get the ViewPager and set it's AboutAdapter so that it can display items
        viewPager = findViewById(R.id.viewpager);
        AboutAdapter aboutAdapter =
                new AboutAdapter(getSupportFragmentManager(), getApplicationContext());
        viewPager.setAdapter(aboutAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt(position);
            viewPager.setCurrentItem(value);

        }
        // Give the TabLayout the ViewPager
        tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static String position = "POSITION";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //save page state
        System.out.println("Save State");
        outState.putInt(position, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //reset page state
        viewPager.setCurrentItem(savedInstanceState.getInt(position));
    }

    //reset choose file and flag on resume activity
    @Override
    public void onResume() {
        super.onResume();

    }

    //menu dots method - includes color picker
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //if item is action_settings
        if (id == R.id.action_apache) {

            //show apache license
            new MaterialDialog.Builder(this)
                    .cancelable(true)
                    .title(getString(R.string.apachename))
                    .titleColorRes(R.color.colorAccent)
                    .content(R.string.apache)
                    .positiveText("Ok")
                    .show();

        }else if(id == R.id.action_mit){

            //show MIT license
            new MaterialDialog.Builder(this)
                    .cancelable(true)
                    .title(getString(R.string.mitname))
                    .titleColorRes(R.color.colorAccent)
                    .content(R.string.mit)
                    .positiveText("Ok")
                    .show();
        }


        return super.onOptionsItemSelected(item);

    }

    //inflate menu in action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.licenses, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}
