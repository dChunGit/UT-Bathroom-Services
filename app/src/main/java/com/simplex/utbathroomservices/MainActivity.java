package com.simplex.utbathroomservices;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.Rating;
import com.simplex.utbathroomservices.fragments.SearchFragment;
import com.simplex.utbathroomservices.fragments.UpdateFragment;
import com.simplex.utbathroomservices.location.LocationCallback;
import com.simplex.utbathroomservices.location.LocationService;
import com.wang.avi.AVLoadingIndicatorView;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener, LocationCallback, GoogleMap.OnMarkerClickListener,
        UpdateFragment.onUpdateListener {
    //TODO: Save current location and update
    //TODO: Variable update rate
    //TODO: Favorites activity
    //TODO: Reviews activity
    //TODO: Search Activity
    //TODO: Settings/About/Help Activity
    //TODO: Firebase integration, search widget integration
    //TODO: Bar graph, key features, reviews recyclerview
    final String SAVELOCATION = "SAVE LOCATION";
    final String LOCATIONGRANTED = "LOCATION GRANTED";
    final String FOLLOW = "FOLLOW";
    final String ZOOM = "ZOOM";
    final String BOTTOMSHEET = "BOTTOMSHEET";

    private GoogleMap mMap;
    private final LatLng mDefaultLocation = new LatLng(30.286310, -97.739560);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderApi mFusedLocationProviderApi;
    private Location mLastKnownLocation;
    private boolean followPerson;
    private int maptype = GoogleMap.MAP_TYPE_NORMAL;
    private float zoomLevel;

    private BottomSheetBehavior bottomSheetBehavior;
    private Toolbar toolbar;
    private TextView toolbar2;
    private DrawerLayout drawerLayout;
    private FloatingActionMenu floatingActionMenu;
    private AVLoadingIndicatorView sync;

    private UpdateFragment updateFragment;
    private FragmentManager fragmentManager;
    private static final String TAG_TASK_FRAGMENT = "updateFragment";

    private HashMap<String, Bathroom> firebaseRatings;
    private LinkedList<Bathroom> firebaseList;
    private boolean syncing = false, locUpdate = false;

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

    boolean mBounded;
    LocationService mServer;

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e("BINDER", "SERVICE UNBOUND");
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("BINDER", "SERVICE BOUND");
            mBounded = true;
            LocationService.LocalBinder mLocalBinder = (LocationService.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();
            mServer.setCallbacks(MainActivity.this);
        }
    };

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        followPerson = true;
        zoomLevel = 17f;
        Intent mIntent = new Intent(this, LocationService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);

        RelativeLayout bottomSheetLayout = findViewById(R.id.locationSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        fragmentManager = getSupportFragmentManager();
        updateFragment = (UpdateFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);

        if(savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(SAVELOCATION);
            mLocationPermissionGranted = savedInstanceState.getBoolean(LOCATIONGRANTED);
            followPerson = savedInstanceState.getBoolean(FOLLOW);
            zoomLevel = savedInstanceState.getFloat(ZOOM);
            bottomSheetBehavior.setState(savedInstanceState.getInt(BOTTOMSHEET));
        } else {
            updateEntries();
        }

        setFont();
        setUpUI();
        setUpMap();
    }

    private void setFont() {

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ColabReg.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    private void setUpUI() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_activity_map));

        sync = findViewById(R.id.sync);

        final FloatingActionButton location = findViewById(R.id.location);
        location.setOnClickListener((view) ->{
            checkPermissions();
            if(mLocationPermissionGranted) {
                followPerson = true;
                updateLocationUI();
                setDeviceLocation(null);
            }
        });

        FloatingActionButton addLocation = findViewById(R.id.newLocation);
        addLocation.setOnClickListener((view) -> {
            if(!syncing && locUpdate) {
                Intent settings = new Intent(MainActivity.this, Add.class);
                //not ideal, should make location parcelable or something
                if (mLastKnownLocation != null) {
                    System.out.println("Putting in extra " + mLastKnownLocation.toString());
                    settings.putExtra("Location", mLastKnownLocation);
                }
                //System.out.println(firebaseList);
                settings.putExtra("Ratings", firebaseList);
                startActivity(settings);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(this, "Please wait until sync is complete", Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton zoomOut = findViewById(R.id.zoomOut);
        zoomOut.setOnClickListener((view) -> {
           zoomLevel--;
           if(mMap != null) {
               mMap.moveCamera(CameraUpdateFactory.zoomOut());
           }
        });

        FloatingActionButton zoomIn = findViewById(R.id.zoomIn);
        zoomIn.setOnClickListener((view) -> {
            zoomLevel++;
            if(mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });

        FloatingActionButton refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener((view) -> updateEntries());

        drawerLayout = findViewById(R.id.drawer_layout);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        floatingActionMenu = findViewById(R.id.menu);
        toolbar2 = findViewById(R.id.toolbar2);
        final CardView cardToolbar = findViewById(R.id.cardToolbar);
        final Toolbar locationToolbar = findViewById(R.id.location_toolbar);

        //get bottom sheet behavior from bottom sheet view
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if(newState == BottomSheetBehavior.STATE_COLLAPSED) {

                    floatingActionMenu.setVisibility(View.VISIBLE);
                    cardToolbar.setVisibility(View.VISIBLE);
                    toolbar2.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setTitle(getString(R.string.title_activity_map));

                } else if(newState == BottomSheetBehavior.STATE_EXPANDED) {

                    floatingActionMenu.setVisibility(View.GONE);
                    cardToolbar.setVisibility(View.GONE);
                    toolbar2.setVisibility(View.GONE);
                    setSupportActionBar(locationToolbar);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("");
                    getSupportActionBar().setHomeButtonEnabled(true);

                } else if(newState == BottomSheetBehavior.STATE_DRAGGING) {

                    floatingActionMenu.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                floatingActionMenu.setAlpha(1 - slideOffset);
                cardToolbar.setAlpha(1 - slideOffset);
                toolbar2.setAlpha(1-slideOffset);
                if((1 - slideOffset) < .05) {
                    cardToolbar.setVisibility(View.GONE);
                    toolbar2.setVisibility(View.GONE);
                } else {
                    cardToolbar.setVisibility(View.VISIBLE);
                    toolbar2.setVisibility(View.VISIBLE);
                }
            }
        });

        toolbar2.setOnClickListener((view) -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

    }

    private void setReview(Object location) {
        float orate = 0f;
        int space = 0, activity = 0, wifi = 0, clean = 0;
        ArrayList<Rating> ratings = new ArrayList<>();
        ArrayList<String> imageUrls = new ArrayList<>();
        //String[] imageUrls = new String[0];

        if(location instanceof Bathroom) {
            Bathroom bathroom = (Bathroom) location;
            orate = bathroom.getOverallRating();
            //space = bathroom.getSpace();
            //stalls = bathroom.getNumberStalls();
            activity = bathroom.getBusyness();
            wifi = bathroom.getWifiQuality();
            clean = bathroom.getCleanliness();
            ratings = bathroom.getRating();
            imageUrls = bathroom.getImage();
        }

        SimpleRatingBar overallRating = findViewById(R.id.stars);
        overallRating.setRating(orate);
        ScaleRatingBar spaceBar = findViewById(R.id.spaceBar);
        ScaleRatingBar activityBar = findViewById(R.id.activityBar);
        ScaleRatingBar wifiBar = findViewById(R.id.wifiBar);
        ScaleRatingBar cleanBar = findViewById(R.id.cleanBar);

        spaceBar.setRating(space);
        activityBar.setRating(activity);
        wifiBar.setRating(wifi);
        cleanBar.setRating(clean);
    }

    private void setUpMap() {
        checkPermissions();
        getMapType();

        // Get map asynchronously and add callback
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/
        new Thread(() -> {
            try {
                final SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.map, supportMapFragment).commit();
                runOnUiThread(() -> supportMapFragment.getMapAsync(MainActivity.this));

            } catch (Exception e) {
                //log map load exception
            }
        }).start();

        // Current location setup
        mFusedLocationProviderApi = LocationServices.FusedLocationApi;
    }

    private void updateEntries() {
        if(updateFragment == null) {
            syncing = true;
            updateFragment = UpdateFragment.newInstance();
            fragmentManager.beginTransaction().add(updateFragment, TAG_TASK_FRAGMENT).commit();
            if(sync != null) {
                sync.smoothToShow();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        System.out.println("TAG, onSavedInstanceState");
        if(mLastKnownLocation != null) {
            outState.putParcelable(SAVELOCATION, mLastKnownLocation);
        }
        outState.putBoolean(LOCATIONGRANTED, mLocationPermissionGranted);
        outState.putBoolean(FOLLOW, followPerson);
        outState.putFloat(ZOOM, zoomLevel);
        outState.putInt(BOTTOMSHEET, bottomSheetBehavior.getState());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        } else {
            //if time interval within specified
            if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
            {
                super.onBackPressed();
            }
            else {
                Toast.makeText(getBaseContext(), getString(R.string.back), Toast.LENGTH_SHORT).show();
            }

            mBackPressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        System.out.println("inflating");
        if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            getMenuInflater().inflate(R.menu.map, menu);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            return true;
        } else if (id == R.id.action_settings) {
            Intent settings = new Intent(this, Settings.class);
            startActivity(settings);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            return true;
        } else if(id == R.id.action_search) {
            new MaterialDialog.Builder(this)
                    .customView(R.layout.filter_dialog, true)
                    .cancelable(true)
                    .title("QUICK SEARCH")
                    .negativeText("Cancel")
                    .positiveText("Search")
                    .onPositive((dialog, which) -> {
                        if(which == DialogAction.POSITIVE) {
                            Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_LONG).show();
                        }
                        View filterDialogView = dialog.getCustomView();
                        ScaleRatingBar overallDialog = filterDialogView.findViewById(R.id.overallBar_dialog);
                        ScaleRatingBar spaceDialog = filterDialogView.findViewById(R.id.spaceBar_dialog);
                        ScaleRatingBar activityDialog = filterDialogView.findViewById(R.id.activityBar_dialog);
                        ScaleRatingBar wifiDialog= filterDialogView.findViewById(R.id.wifiBar_dialog);
                        ScaleRatingBar cleanDialog = filterDialogView.findViewById(R.id.cleanBar_dialog);

                        System.out.println(overallDialog.getRating() + " " + spaceDialog.getRating() + " " + activityDialog.getRating() +
                            " " + wifiDialog.getRating() + " " + cleanDialog.getRating());
                    })
                    .show();

            return true;
        } else if(id == R.id.action_maptype) {

            MaterialDialog mapDialog = new MaterialDialog.Builder(this)
                    .customView(R.layout.map_dialog, false)
                    .cancelable(true)
                    .title("MAP TYPE")
                    .negativeText("Cancel")
                    .positiveText("Ok")
                    .onPositive((dialog, which) -> {
                            setMapType();
                            saveMapType();
                        }
                    )
                    .show();

            View mapDialogView = mapDialog.getCustomView();

            LinearLayout street = mapDialogView.findViewById(R.id.street);
            LinearLayout satellite = mapDialogView.findViewById(R.id.satellite);
            final RadioButton streetRb = mapDialogView.findViewById(R.id.streetbutton);
            final RadioButton satRb = mapDialogView.findViewById(R.id.satellitebutton);

            street.setOnClickListener((view) -> {
                maptype = GoogleMap.MAP_TYPE_NORMAL;
                streetRb.setChecked(true);
                satRb.setChecked(false);
            });

            satellite.setOnClickListener((view) -> {
                maptype = GoogleMap.MAP_TYPE_HYBRID;
                streetRb.setChecked(false);
                satRb.setChecked(true);
            });

            if(maptype == GoogleMap.MAP_TYPE_NORMAL) {
                streetRb.setChecked(true);
                satRb.setChecked(false);
            } else {
                streetRb.setChecked(false);
                satRb.setChecked(true);
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.favorites) {

            Intent settings = new Intent(this, Favorites.class);
            startActivity(settings);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        } else if (id == R.id.advanced_search) {

        } else if (id == R.id.settings) {

            Intent settings = new Intent(this, Settings.class);
            startActivity(settings);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        } else if (id == R.id.about) {

        } else if (id == R.id.help) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);

        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnMarkerClickListener(this);

        setMapType();
        moveCamera();

    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(toolbar2 != null) {
            toolbar2.setText(marker.getTitle());
            setReview(firebaseRatings.get(marker.getTitle()));
        }
        return false;
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                //mLastKnownLocation = null;
                //checkPermissions();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void setDeviceLocation(Location location) {
        try {
            if (mLocationPermissionGranted) {
                if(location == null) {
                    Location current = mFusedLocationProviderApi.getLastLocation(mServer.getCurrentLocation());
                    if (current != null) {
                        mLastKnownLocation = current;
                        locUpdate = true;
                    } else {
                        Log.i("Location", "Current location is null");
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                } else {
                    mLastKnownLocation = location;
                    locUpdate = true;
                }


                if (followPerson) {
                    moveCamera();
                }

            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void updateLocationGUI(Location location) {
        updateLocationUI();
        setDeviceLocation(location);
    }

    @Override
    public void onCameraMoveStarted(int reason) {

       if (reason != GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION) {
           followPerson = false;
       }
    }

    private void moveCamera() {
        if(mLastKnownLocation != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mLastKnownLocation.getLatitude(),
                            mLastKnownLocation.getLongitude()), zoomLevel));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, zoomLevel));
        }
    }

    private void saveMapType() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("MAPTYPE", maptype);
        editor.apply();

    }

    private void getMapType() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        maptype = sharedPref.getInt("MAPTYPE", GoogleMap.MAP_TYPE_NORMAL);

    }

    private void setMapType() {
        if(mMap != null) {
            mMap.setMapType(maptype);
        }
    }
    private void addMarkers(LinkedList<MarkerOptions> markers) {

        for(MarkerOptions markerOptions : markers) {
            mMap.addMarker(markerOptions);
        }

    }

    @Override
    public void onUpdateFinish(HashMap<String, Bathroom> results, LinkedList<Bathroom> resultsList, LinkedList<MarkerOptions> markers) {
        runOnUiThread(() -> {
            if(sync != null) {
                Handler handler = new Handler();
                handler.postDelayed(() -> sync.smoothToHide(), 2000);
            }

            firebaseRatings = results;
            firebaseList = resultsList;
            System.out.println(firebaseRatings);
            //addMarkers(markers);
            updateFragment = null;
            syncing = false;

        });
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {

        }
        updateFragment = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mBounded) {
            mServer.setCallbacks(null); // unregister
            unbindService(mConnection);
            mBounded = false;
        }
        saveMapType();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}