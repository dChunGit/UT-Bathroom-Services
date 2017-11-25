package com.simplex.utbathroomservices;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.Rating;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Add extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

    private Location location;
    private String type, building, floorNumber, space;
    private int stallnum;
    private boolean customStallSelect = false;

    private String[] buildings = {"ADH", "AF1", "AF2", "AFP", "AHG", "ANB", "AND", "ARC", "ART", "ATT",
            "BAT", "BEL", "BEN", "BGH", "BHD", "BIO", "BLD", "BMA", "BMC", "BME", "BMS", "BOT",
            "BRB", "BRG", "BSB", "BTL", "BUR", "BWY", "CAL", "CBA", "CCG", "CCJ", "CDA", "CDL",
            "CEE", "CLA", "CLK", "CMA", "CMB", "CML", "COM", "CPB", "CPE", "CRB", "CRD", "CRH",
            "CS3", "CS4", "CS5", "CS6", "CS7", "CSS", "CT1", "DCP", "DEV", "DFA", "DFF", "DPI",
            "DTB", "E10", "E11", "E12", "E13", "E15", "E23", "E24", "E25", "ECG", "ECJ", "EER",
            "EHZ", "EPS", "ERC", "ETC", "FAC", "FC1", "FC2", "FC3", "FC4", "FC5", "FC6", "FC7",
            "FC8", "FC9", "FCS", "FDH", "FNT", "FSB", "G01", "G02", "G05", "G06", "G07", "G11",
            "GAR", "GDC", "GEA", "GEB", "GOL", "GRC", "GRE", "GRF", "GRP", "GRS", "GSB", "GUG",
            "GWB", "HCG", "HDB", "HLB", "HLP", "HMA", "HRC", "HRH", "HSM", "HSS", "HTB", "IC2",
            "ICB", "IMA", "IMB", "IPF", "JCD", "JES", "JGB", "JHH", "JON", "KIN", "LAC", "LBJ",
            "LCD", "LCH", "LDH", "LFH", "LLA", "LLB", "LLC", "LLD", "LLE", "LLF", "LS1", "LTD",
            "LTH", "MAG", "MAI", "MB1", "MBB", "MEZ", "MFH", "MHD", "MMS", "MNC", "MRH", "MSB",
            "MTC", "NEZ", "NHB", "NMS", "NOA", "NUR", "PA1", "PA3", "PA4", "PAC", "PAI", "PAR",
            "PAT", "PB2", "PB5", "PB6", "PCL", "PH1", "PH2", "PHD", "PHR", "POB", "PPA", "PPE",
            "PPL", "PRH", "QTR", "RHD", "RLM", "ROW", "RSC", "SAC", "SAG", "SBS", "SEA", "SER",
            "SJG", "SJH", "SOF", "SRH", "SSB", "SSW", "STD", "SUT", "SW7", "SWG", "SZB", "TCC",
            "TCP", "TES", "TMM", "TNH", "TR1", "TR2", "TRG", "TSB", "TSC", "TSG", "UA9", "UIL",
            "UNB", "UPB", "USS", "UTA", "UTC", "UTX", "VRX", "WAG", "WAT", "WCH", "WCS", "WEL",
            "WGB", "WIN", "WMB", "WRW", "WWH"};
    private ConcurrentHashMap<String, Bathroom> firebaseRatings = new ConcurrentHashMap<>();
    private ArrayList<Bathroom> sentRatings = new ArrayList<>();

    private AutoCompleteTextView autoCompleteTextView;
    private EditText editText, commentsEditText;
    private ScaleRatingBar overall, activity, wifi, cleanliness;
    private TextView displayStall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        System.out.println(getIntent().getExtras());

        try {
            location = getIntent().getParcelableExtra("Location");
        } catch (Exception e) {
            System.out.println("Location Not Sent or Something Else");
        }

        try{
            sentRatings = getIntent().getParcelableArrayListExtra("Ratings");
            System.out.println(sentRatings);
        } catch (Exception e) {
            System.out.println("Ratings malformed");
        }

        new Thread(() -> {
            for(Bathroom b : sentRatings) {
                firebaseRatings.put(b.getBuilding() + " " + b.getFloor(), b);
            }
            System.out.println(firebaseRatings);
        }).start();

        setUpUI();
    }

    private void setUpUI() {

        Toolbar toolbar = findViewById(R.id.addbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_addActivity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        displayStall = findViewById(R.id.customDisplay);

        FloatingActionButton imagefab = findViewById(R.id.imagefab);
        imagefab.setOnClickListener((view) -> {
            Toast.makeText(this, "Add Image", Toast.LENGTH_SHORT).show();
        });

        setUpSpinners();

        overall = findViewById(R.id.overallBar_add);
        activity = findViewById(R.id.activityBar_add);
        wifi = findViewById(R.id.wifiBar_add);
        cleanliness = findViewById(R.id.cleanBar_add);
        commentsEditText = findViewById(R.id.comments);
    }

    private void setUpSpinners() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, buildings);
        autoCompleteTextView = findViewById(R.id.completeBuilding);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);
        editText = findViewById(R.id.roomNumber);

        Spinner typespinner = findViewById(R.id.typespinner);
        Spinner stallspinner = findViewById(R.id.stallpicker);
        Spinner spacespinner = findViewById(R.id.spacePicker);
        ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this,
                R.array.type_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> stalladapter = ArrayAdapter.createFromResource(this,
                R.array.stall_array, R.layout.spinner_item);
        ArrayAdapter<CharSequence> spaceadapter = ArrayAdapter.createFromResource(this,
                R.array.space_array, R.layout.spinner_item);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stalladapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spaceadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typespinner.setAdapter(spinneradapter);
        stallspinner.setAdapter(stalladapter);
        spacespinner.setAdapter(spaceadapter);

        typespinner.setOnItemSelectedListener(this);
        stallspinner.setOnItemSelectedListener(this);
        spacespinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println(adapterView.getId());
        switch(adapterView.getId()) {
            case R.id.typespinner: {
                System.out.println("TypeSpinner");
                switch(i) {
                    case 0: type = "Bathroom"; break;
                    case 1: type = "Fountain";
                }
            } break;
            case R.id.stallpicker: {
                System.out.println("StallSpinner");
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
                System.out.println("SpaceSpinner");
                switch(i) {
                    case 0: space = "XSmall"; break;
                    case 1: space = "Small"; break;
                    case 2: space = "Medium"; break;
                    case 3: space = "Large"; break;
                    case 4: space = "XLarge";
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

                //if is a location already
                if(firebaseRatings.containsKey(building + " " + floorNumber)) {
                    //bathroomDB.addReviewForBathroom();
                } else {
                    ArrayList<Rating> newRating = new ArrayList<>();
                    Rating rating = new Rating(commentsEditText.getText().toString());
                    newRating.add(rating);
                    if(customStallSelect) {
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

                    if(type.equals("Bathroom")) {
                        BathroomDB bathroomDB = new BathroomDB();
                        bathroomDB.addBathroomToDB(location, building, floorNumber, space, stallnum,
                                wifi.getNumStars(), activity.getNumStars(), overall.getNumStars(),
                                cleanliness.getNumStars(), newRating, new ArrayList<>());
                        finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item);
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
}
