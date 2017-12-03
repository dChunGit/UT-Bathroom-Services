package com.simplex.utbathroomservices.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.MarkerOptions;
import com.simplex.utbathroomservices.Database;
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.SearchParams;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.simplex.utbathroomservices.fragments.SearchFragment;
import com.willy.ratingbar.ScaleRatingBar;

import java.lang.reflect.Array;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Search extends AppCompatActivity implements SearchFragment.SearchCallback, AdapterView.OnItemSelectedListener{
    private SearchFragment searchFragment;
    private FragmentManager fragmentManager;

    private SwitchCompat switchRefill;
    private String type, building;
    private Spinner typespinner, tempspinner, tastespinner, stallspinner, spacespinner;
    private ScaleRatingBar overall, activity, wifi, cleanliness;
    private int overallV, activityV, wifiV, cleanlinessV, spaceV, tempV, tasteV;
    private int stallnum;
    private boolean customStallSelect = false, isFillable;

    private ArrayList<String> buildingsList = new ArrayList<>();
    private String[] buildings = new String[0];

    private AutoCompleteTextView autoCompleteTextView;
    private TextView displayStall;
    private CardView bathroomLL, fountainLL;
    private FloatingActionButton fab;

    private Database database;

    private static final String TAG_SEARCH_FRAGMENT = "SearchFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fragmentManager = getSupportFragmentManager();
        searchFragment = (SearchFragment) fragmentManager.findFragmentByTag(TAG_SEARCH_FRAGMENT);
        database = (Database) getApplication();

        if(database != null) {
            buildingsList = database.getSaveBuildings();
            buildings = buildingsList.toArray(new String[buildingsList.size()]);
        }

        setUpUI();
        setUpSpinners();


    }

    @TargetApi(23)
    private void setUpFabs() {
        NestedScrollView nestedScrollView = findViewById(R.id.parentAnim_search);
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY)
                    fab.hide();
                else if (scrollY < oldScrollY)
                    fab.show();
            }
        });
    }

    private void setUpUI() {
        Toolbar toolbar = findViewById(R.id.addbarSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_search));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener((view) -> onBackPressed());

        //set font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ColabReg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        displayStall = findViewById(R.id.customDisplay_search);
        bathroomLL = findViewById(R.id.bathcard_search);
        fountainLL = findViewById(R.id.fountaincard_search);

        switchRefill = findViewById(R.id.refillStation_search);
        switchRefill.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isFillable = isChecked;
        });

        fab = findViewById(R.id.search_start);
        fab.setOnClickListener((view) -> {
            startSearch();
        });

        setUpFabs();

        overall = findViewById(R.id.overallBar_search);
        activity = findViewById(R.id.activityBar_search);
        wifi = findViewById(R.id.wifiBar_search);
        cleanliness = findViewById(R.id.cleanBar_search);

        overall.setOnRatingChangeListener((ratingBar, rating) -> {
            overallV = (int) rating;
        });
        activity.setOnRatingChangeListener((ratingBar, rating) -> {
            activityV = (int) rating;
        });
        wifi.setOnRatingChangeListener((ratingBar, rating) -> {
            wifiV = (int) rating;
        });
        cleanliness.setOnRatingChangeListener((ratingBar, rating) -> {
            cleanlinessV = (int) rating;
        });
    }
    private void setUpSpinners() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, buildings);
        autoCompleteTextView = findViewById(R.id.completeBuilding_search);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        typespinner = findViewById(R.id.typespinner_search);
        stallspinner = findViewById(R.id.stallpicker_search);
        spacespinner = findViewById(R.id.spacePicker_search);
        tempspinner = findViewById(R.id.tempPicker_search);
        tastespinner = findViewById(R.id.tastePicker_search);
        ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> stalladapter = ArrayAdapter.createFromResource(this,
                R.array.stall_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> spaceadapter = ArrayAdapter.createFromResource(this,
                R.array.space_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> tempadapter = ArrayAdapter.createFromResource(this,
                R.array.temp_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> tasteadapter = ArrayAdapter.createFromResource(this,
                R.array.taste_array, R.layout.spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stalladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spaceadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tempadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tasteadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typespinner.setAdapter(spinneradapter);
        stallspinner.setAdapter(stalladapter);
        spacespinner.setAdapter(spaceadapter);
        tempspinner.setAdapter(tempadapter);
        tastespinner.setAdapter(tasteadapter);

        typespinner.setOnItemSelectedListener(this);
        stallspinner.setOnItemSelectedListener(this);
        spacespinner.setOnItemSelectedListener(this);
        tempspinner.setOnItemSelectedListener(this);
        tastespinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch(adapterView.getId()) {
            case R.id.typespinner: {
                switch(i) {
                    case 0: {
                        type = "Bathroom";
                        bathroomLL.setVisibility(View.VISIBLE);
                        fountainLL.setVisibility(View.GONE);
                    } break;
                    case 1: {
                        type = "Fountain";
                        fountainLL.setVisibility(View.VISIBLE);
                        bathroomLL.setVisibility(View.GONE);
                    }
                }
            } break;
            case R.id.stallpicker: {
                //displaystall is for custom input
                displayStall.setVisibility(View.INVISIBLE);
                customStallSelect = false;

                switch(i) {
                    case 0: stallnum = 0; break;
                    case 1: stallnum = 1; break;
                    case 2: stallnum = 2; break;
                    case 3: stallnum = 3; break;
                    case 4: stallnum = 4; break;
                    case 5: stallnum = 5; break;
                    case 6: {
                        displayStall.setVisibility(View.VISIBLE);
                        customStallSelect = true;
                    }
                }
            } break;
            case R.id.spacePicker: {
                spaceV = i + 1;
            } break;
            case R.id.tempPicker: {
                tempV = 5- i;
            } break;
            case R.id.tastePicker: {
                tasteV = 5 - i;
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void startSearch() {
        building = autoCompleteTextView.getText().toString();

        if (customStallSelect) {
            try {
                int temp = Integer.valueOf(displayStall.getText().toString());
                if (temp >= 0) {
                    stallnum = temp;
                } else {
                    Toast.makeText(this, "Please Enter a Valid Number", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                //not a number
                Toast.makeText(this, "Please Enter a Valid Number", Toast.LENGTH_SHORT).show();
            }
        }

        SearchParams searchParams = new SearchParams("Custom", (String) typespinner.getSelectedItem(),
            building, "", spaceV, stallnum, wifiV,activityV, cleanlinessV, overallV,
            switchRefill.isChecked(), tasteV, tempV);
        searchFragment = SearchFragment.newInstance(searchParams);
        fragmentManager.beginTransaction().add(searchFragment, TAG_SEARCH_FRAGMENT).commit();
    }

    @Override
    public void onCancelledSearch() {
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_SEARCH_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {

        }
        searchFragment = null;
    }

    @Override
    public void onPostExecuteSearch(ArrayList<Bathroom> filteredBathrooms, ArrayList<WaterFountain> filteredFountains, ArrayList<MarkerOptions> markerOptions) {
        System.out.println("Filter Finished");
        System.out.println(filteredBathrooms);
        System.out.println(filteredFountains);
        System.out.println(markerOptions);

        fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_SEARCH_FRAGMENT)).commitAllowingStateLoss();
        searchFragment = null;

        Intent i = new Intent();
        i.putParcelableArrayListExtra("SEARCH_MARKER", markerOptions);
        setResult(Activity.RESULT_OK, i);
        finish();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_SEARCH_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {

        }
        searchFragment = null;
    }

}
