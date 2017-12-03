package com.simplex.utbathroomservices.activities;

import android.animation.LayoutTransition;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.simplex.utbathroomservices.Database;
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.Building;
import com.simplex.utbathroomservices.cloudfirestore.DatabaseCallback;
import com.simplex.utbathroomservices.cloudfirestore.Rating;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountainDB;
import com.simplex.utbathroomservices.dbflow.AppDatabase;
import com.simplex.utbathroomservices.dbflow.Rating_Item;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class Add extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatabaseCallback {

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

    private Location location;
    private String type, building, floorNumber, space, temp, taste;
    private int overallV, activityV, wifiV, cleanlinessV;
    private int stallnum;
    private boolean customStallSelect = false, isFillable;

    private HashMap<String, Bathroom> firebaseBRatings = new HashMap<>();
    private HashMap<String, WaterFountain> firebaseWRatings = new HashMap<>();
    private ArrayList<String> buildingsList = new ArrayList<>();
    private String[] buildings = new String[0];

    private AutoCompleteTextView autoCompleteTextView;
    private EditText editText, commentsEditText;
    private ScaleRatingBar overall, activity, wifi, cleanliness;
    private TextView displayStall;
    private CardView bathroomLL, fountainLL;
    private SwitchCompat switchRefill;
    private Spinner typespinner, tempspinner, tastespinner, stallspinner, spacespinner;

    private String editLocation;

    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        System.out.println(getIntent().getExtras());
        database = (Database) getApplication();

        try {
            location = getIntent().getParcelableExtra("Location");
        } catch (Exception e) {
            System.out.println("Location Not Sent or Something Else");
        }

        try{
            editLocation = getIntent().getStringExtra("Selected");
            System.out.println(editLocation);
        } catch (Exception e) {
            System.out.println("Something");
        }

        if(database != null) {
            firebaseBRatings = database.getFirebaseBRatings();
            firebaseWRatings = database.getFirebaseWRatings();
            buildingsList = database.getSaveBuildings();
            buildings = buildingsList.toArray(new String[buildingsList.size()]);
        }

        FlowManager.init(FlowConfig.builder(this)
                .addDatabaseConfig(DatabaseConfig.builder(AppDatabase.class)
                        .databaseName("UTBSDatabase")
                        .build())
                .build());

        setUpUI();
        setUpInfo();
    }

    private void setUpInfo() {
        //if adding for pre-existing location, restore views to match
        if(editLocation != null) {
            String[] title = editLocation.split("@");
            if(title[1].trim().equals("Bathroom")) {
                //if this doesn't work, post in new runnable
                typespinner.setSelection(0);
                Bathroom bathroom = firebaseBRatings.get(title[0].trim());
                autoCompleteTextView.setText(bathroom.getBuilding());
                editText.setText(bathroom.getFloor());

            } else if(title[1].trim().equals("Fountain")){
                typespinner.setSelection(1);
                WaterFountain waterFountain = firebaseWRatings.get(title[0].trim());
                autoCompleteTextView.setText(waterFountain.getBuilding());
                editText.setText(waterFountain.getFloor());
            }
        }

    }

    private void setUpUI() {

        Toolbar toolbar = findViewById(R.id.addbar);
        setSupportActionBar(toolbar);
        //set title based on if editing existing location or not
        if(editLocation != null) {
            getSupportActionBar().setTitle(R.string.title_addActivityAlt);
        } else {
            getSupportActionBar().setTitle(getString(R.string.title_addActivity));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener((view) -> onBackPressed());

        //set font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ColabReg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        displayStall = findViewById(R.id.customDisplay);
        bathroomLL = findViewById(R.id.bathcard);
        fountainLL = findViewById(R.id.fountaincard);

        ((ViewGroup) findViewById(R.id.parentAnim)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) findViewById(R.id.bathcard)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
        ((ViewGroup) findViewById(R.id.fountaincard)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);

        FloatingActionButton imagefab = findViewById(R.id.imagefab);
        imagefab.setOnClickListener((view) -> {
            Toast.makeText(this, "Add Image", Toast.LENGTH_SHORT).show();
        });

        switchRefill = findViewById(R.id.refillStation);
        switchRefill.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isFillable = isChecked;
        });
        setUpSpinners();

        overall = findViewById(R.id.overallBar_add);
        activity = findViewById(R.id.activityBar_add);
        wifi = findViewById(R.id.wifiBar_add);
        cleanliness = findViewById(R.id.cleanBar_add);
        commentsEditText = findViewById(R.id.comments);

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
        autoCompleteTextView = findViewById(R.id.completeBuilding);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        editText = findViewById(R.id.roomNumber);

        typespinner = findViewById(R.id.typespinner);
        stallspinner = findViewById(R.id.stallpicker);
        spacespinner = findViewById(R.id.spacePicker);
        tempspinner = findViewById(R.id.tempPicker);
        tastespinner = findViewById(R.id.tastePicker);
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
                switch(i) {
                    case 0: space = "XSmall"; break;
                    case 1: space = "Small"; break;
                    case 2: space = "Medium"; break;
                    case 3: space = "Large"; break;
                    case 4: space = "XLarge";
                }
            } break;
            case R.id.tempPicker: {
                switch(i) {
                    case 0: temp = "cold"; break;
                    case 1: temp = "cool"; break;
                    case 2: temp = "lukewarm"; break;
                    case 3: temp = "warm"; break;
                    case 4: temp = "hot";
                }
            } break;
            case R.id.tastePicker: {
                switch(i) {
                    case 0: taste = "Wow"; break;
                    case 1: taste = "Pretty Good"; break;
                    case 2: taste = "Meh"; break;
                    case 3: taste = "Not Great"; break;
                    case 4: taste = "Disgusting";
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addmenu, menu);

        return true;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //don't care I think
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_save: {
                building = autoCompleteTextView.getText().toString();
                floorNumber = editText.getText().toString();

                if(!building.isEmpty() && !floorNumber.isEmpty()) {

                    //if is a location already
                    if (firebaseBRatings.containsKey(building + " " + floorNumber)) {
                        Bathroom bathroom = setUpBathroom(firebaseBRatings.get(building + " " + floorNumber));
                        BathroomDB bathroomDB = new BathroomDB(this);
                        bathroomDB.updateReviewForBathroom(bathroom);

                    } else if (firebaseWRatings.containsKey(building + " " + floorNumber)) {
                        WaterFountain waterFountain = setUpFountain(firebaseWRatings.get(building + " " + floorNumber));
                        WaterFountainDB waterFountainDB = new WaterFountainDB(this);
                        waterFountainDB.updateReviewForFountain(waterFountain);

                    } else {
                        //add new location

                        ArrayList<Rating> newRating = new ArrayList<>();
                        //if entered a custom stall number, check to make sure is valid
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

                        if (type.equals("Bathroom")) {
                            UUID uuid = UUID.randomUUID();
                            Rating rating = new Rating(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), building, floorNumber,
                                    commentsEditText.getText().toString(), space, stallnum, wifiV, activityV, overallV,
                                    cleanlinessV, "cold", false, "Meh");
                            newRating.add(rating);

                            Rating_Item rating_item = new Rating_Item();
                            rating_item.setLocation(building + " " + floorNumber);
                            rating_item.setUuid(uuid);
                            rating_item.setType("Bathroom");
                            rating_item.save();

                            BathroomDB bathroomDB = new BathroomDB(this);
                            bathroomDB.addBathroomToDB(location, building, floorNumber, 1, space, stallnum,
                                    wifiV, activityV, overallV,
                                    cleanlinessV, newRating, new ArrayList<>());

                        } else if (type.equals("Fountain")) {
                            UUID uuid = UUID.randomUUID();
                            Rating rating = new Rating(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), building, floorNumber,
                                    commentsEditText.getText().toString(), "XSmall", 0, 0, 0, 0,
                                    overallV, temp, isFillable, taste);
                            newRating.add(rating);

                            Rating_Item rating_item = new Rating_Item();
                            rating_item.setLocation(building + " " + floorNumber);
                            rating_item.setUuid(uuid);
                            rating_item.setType("Fountain");
                            rating_item.save();

                            WaterFountainDB waterFountainDB = new WaterFountainDB(this);
                            waterFountainDB.addWaterFountainToDB(location, building, floorNumber, 1, temp,
                                    isFillable, taste, overallV, newRating, new ArrayList<>());

                        }
                    }
                } else {
                    Toast.makeText(this, "Please Enter a Valid Building and Floor/Room Number", Toast.LENGTH_LONG).show();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
    
    private WaterFountain setUpFountain(WaterFountain waterFountain) {
        int reviews = waterFountain.getReviews();
        int tempP = getTempString(waterFountain.getTemperature());
        int tasteP = getTasteString(waterFountain.getTaste());
        int overallP = waterFountain.getOverallRating();

        waterFountain.setTemperature(getTempVal(((tempP * reviews) + getTempString(temp))/(reviews + 1)));
        //need to make bottle refill an arraylist at some point to get majority instead of latest
        waterFountain.setTaste(getTasteVal(((tasteP * reviews) + getTasteString(taste))/(reviews + 1)));
        waterFountain.setOverallRating(((overallP * reviews) + overallV)/(reviews + 1));

        ArrayList<Rating> newRating = waterFountain.getRating();

        UUID uuid = UUID.randomUUID();
        Rating rating = new Rating(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(),
                waterFountain.getBuilding(), waterFountain.getFloor(), commentsEditText.getText().toString(), "XSmall", 0,
                0, 0, 0, overallV, temp, isFillable, taste);

        Rating_Item rating_item = new Rating_Item();
        rating_item.setLocation(building + " " + floorNumber);
        rating_item.setUuid(uuid);
        rating_item.setType("Fountain");
        rating_item.save();

        newRating.add(rating);
        waterFountain.setRating(newRating);

        waterFountain.setReviews(reviews + 1);
        return waterFountain;
    }

    private String getTasteVal(int i) {
        String taste = "";
        switch(i) {
            case 1: taste = "Wow"; break;
            case 2: taste = "Pretty Good"; break;
            case 3: taste = "Meh"; break;
            case 4: taste = "Not Great"; break;
            case 5: taste = "Disgusting";
        }
        return taste;
    }

    private int getTasteString(String type) {
        switch(type) {
            case "Wow": return 5;
            case "Pretty Good": return 4;
            case "Meh": return 3;
            case "Not Great": return 2;
            case "Disgusting": return 1;
            default: return 0;
        }
    }

    private String getTempVal(int i) {
        String temp = "";

        switch(i) {
            case 1: temp = "cold"; break;
            case 2: temp = "cool"; break;
            case 3: temp = "lukewarm"; break;
            case 4: temp = "warm"; break;
            case 5: temp = "hot";
        }
        return temp;
    }
    
    private int getTempString(String type) {
        switch(type) {
            case "cold": return 5; 
            case "cool": return 4; 
            case "lukewarm": return 3; 
            case "warm": return 2; 
            case "hot": return 1;
            default: return 0;
        }
    }
    
    private Bathroom setUpBathroom(Bathroom bathroom) {
        int spaceP = getSpaceVal(bathroom.getSpace());
        int stallNumP = bathroom.getNumberStalls();
        int wifiP = bathroom.getWifiQuality(), activityP = bathroom.getBusyness(),
                overallP = bathroom.getOverallRating(), cleanP = bathroom.getCleanliness();
        int reviews = bathroom.getReviews();

        bathroom.setSpace(getSpaceString(((spaceP * reviews) + getSpaceVal(space))/(reviews + 1)));
        bathroom.setNumberStalls(((stallNumP * reviews) + stallnum)/(reviews + 1));
        bathroom.setWifiQuality(((wifiP * reviews) + wifiV)/(reviews + 1));
        bathroom.setBusyness(((activityP * reviews) + activityV)/(reviews + 1));
        bathroom.setOverallRating(((overallP * reviews) + overallV)/(reviews + 1));
        bathroom.setCleanliness(((cleanP * reviews) + cleanlinessV)/(reviews + 1));

        ArrayList<Rating> newRating = bathroom.getRating();
        UUID uuid = UUID.randomUUID();
        Rating rating = new Rating(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(),
                bathroom.getBuilding(), bathroom.getFloor(), commentsEditText.getText().toString(), space, stallnum,
                wifiV, activityV, overallV, cleanlinessV, "cold", false, "Meh");

        Rating_Item rating_item = new Rating_Item();
        rating_item.setLocation(building + " " + floorNumber);
        rating_item.setUuid(uuid);
        rating_item.setType("Bathroom");
        rating_item.save();

        newRating.add(rating);
        bathroom.setRating(newRating);

        bathroom.setReviews(reviews + 1);

        return bathroom;
    }
    
    private int getSpaceVal(String space) {
        switch(space) {
            case "XSmall": return 1; 
            case "Small": return 2; 
            case "Medium": return 3; 
            case "Large": return 4; 
            case "XLarge": return 5; 
            default: return 0;
        }
    }

    private String getSpaceString(int select) {
        String space = "";

        switch(select) {
            case 1: space = "XSmall"; break;
            case 2: space = "Small"; break;
            case 3: space = "Medium"; break;
            case 4: space = "Large"; break;
            case 5: space = "XLarge";
        }

        return space;
    }

    @Override
    public void onBackPressed() {
        //if time interval within specified
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        } else {
            Toast.makeText(getBaseContext(), "Press CLOSE again to discard review", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public void updateFinishedB(ArrayList<Bathroom> r) {
        //ignore
    }

    @Override
    public void updateFinishedF(ArrayList<WaterFountain> r) {
        //ignore
    }

    @Override
    public void updateBuildings(ArrayList<Building> b) {
        //ignore
    }

    @Override
    public void addFinished(boolean success) {
        if(success) {
            Intent i = new Intent();
            setResult(Activity.RESULT_OK, i);
            finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        } else {
            Toast.makeText(this, "Error Saving to Database. Please try again", Toast.LENGTH_LONG).show();
        }
    }

}
