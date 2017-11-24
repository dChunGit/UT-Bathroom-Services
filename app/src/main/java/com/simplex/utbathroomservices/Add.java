package com.simplex.utbathroomservices;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.BathroomDB;
import com.simplex.utbathroomservices.cloudfirestore.Rating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Add extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

    private Location location;

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
    private ArrayList<Bathroom> sentRatings;

    private AutoCompleteTextView autoCompleteTextView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        try {
            location = (Location) getIntent().getExtras().get("Location");
        } catch (NullPointerException e) {
            System.out.println("Null location");
        }

        try{
            sentRatings = (ArrayList) getIntent().getExtras().get("Ratings");
        } catch (NullPointerException e) {
            System.out.println("Null Ratings");
        }

        new Thread(() -> {
            for(Bathroom b : sentRatings) {
                firebaseRatings.put(b.getBuilding() + " " + b.getFloor(), b);
            }
            System.out.println(firebaseRatings);
        }).start();

        Toolbar toolbar = findViewById(R.id.addbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_addActivity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

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

        FloatingActionButton imagefab = findViewById(R.id.imagefab);
        imagefab.setOnClickListener((view) -> {
            Toast.makeText(this, "Add Image", Toast.LENGTH_LONG).show();
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch(adapterView.getId()) {
            case R.id.typespinner: {
                switch(i) {
                    case 0: Toast.makeText(this, "Bathroom Selected", Toast.LENGTH_LONG).show(); break;
                    case 1: Toast.makeText(this, "Water Fountain Selected", Toast.LENGTH_LONG).show();
                }
            } break;
            case R.id.stallpicker: {
                switch(i) {
                    case 0: Toast.makeText(this, "None", Toast.LENGTH_LONG).show(); break;
                    case 1: Toast.makeText(this, "1", Toast.LENGTH_LONG).show(); break;
                    case 2: Toast.makeText(this, "2", Toast.LENGTH_LONG).show(); break;
                    case 3: Toast.makeText(this, "3", Toast.LENGTH_LONG).show(); break;
                    case 4: Toast.makeText(this, "4", Toast.LENGTH_LONG).show(); break;
                    case 5: Toast.makeText(this, "5", Toast.LENGTH_LONG).show(); break;
                    case 6: Toast.makeText(this, "Custom", Toast.LENGTH_LONG).show();
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
                BathroomDB bathroomDB = new BathroomDB();
                String building = autoCompleteTextView.getText().toString();
                String floorRoom = editText.getText().toString();

                //if is a location already
                if(firebaseRatings != null) {
                    if(firebaseRatings.containsKey(building + " " + floorRoom)) {
                        //bathroomDB.addReviewForBathroom();
                    } else {
                        //bathroomDB.addBathroomToDB(location, building, floorRoom, );
                    }/*Location location, String building, String floor, String space,
                            String numberStalls, Integer wifiQuality, Integer busyness,
                            Integer cleanliness, Integer overallRating, ArrayList< Rating > rating,
                            String[] image*/
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
        } else {
            Toast.makeText(getBaseContext(), "Press CLOSE again to discard review", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}
