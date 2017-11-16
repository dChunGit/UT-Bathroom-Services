package com.simplex.utbathroomservices;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.willy.ratingbar.ScaleRatingBar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener, LocationCallback, GoogleMap.OnMarkerClickListener {
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

    private BottomSheetBehavior bottomSheetBehavior;

    private GoogleMap mMap;
    private final LatLng mDefaultLocation = new LatLng(30.286310, -97.739560);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderApi mFusedLocationProviderApi;
    private Location mLastKnownLocation;
    private boolean followPerson;
    private int maptype = GoogleMap.MAP_TYPE_NORMAL;
    private float zoomLevel;
    private Toolbar toolbar;
    private TextView toolbar2;
    private DrawerLayout drawerLayout;
    private FloatingActionMenu floatingActionMenu;

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

        if(savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(SAVELOCATION);
            mLocationPermissionGranted = savedInstanceState.getBoolean(LOCATIONGRANTED);
            followPerson = savedInstanceState.getBoolean(FOLLOW);
            zoomLevel = savedInstanceState.getFloat(ZOOM);
            bottomSheetBehavior.setState(savedInstanceState.getInt(BOTTOMSHEET));
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

        final FloatingActionButton location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                if(mLocationPermissionGranted) {
                    followPerson = true;
                    updateLocationUI();
                    setDeviceLocation(null);
                }
            }
        });

        FloatingActionButton addLocation = findViewById(R.id.newLocation);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                if(mLocationPermissionGranted) {
                    if(mLastKnownLocation != null) {
                        Toast.makeText(getApplicationContext(),
                                mLastKnownLocation.getLatitude() + " " + mLastKnownLocation.getLongitude(),
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        FloatingActionButton zoomOut = findViewById(R.id.zoomOut);
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               zoomLevel--;
               if(mMap != null) {
                   mMap.moveCamera(CameraUpdateFactory.zoomOut());
               }
            }
        });

        FloatingActionButton zoomIn = findViewById(R.id.zoomIn);
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomLevel++;
                if(mMap != null) {
                    mMap.moveCamera(CameraUpdateFactory.zoomIn());
                }
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        floatingActionMenu = findViewById(R.id.menu);
        toolbar2 = findViewById(R.id.toolbar2);
        final Toolbar locationToolbar = findViewById(R.id.location_toolbar);

        //get bottom sheet behavior from bottom sheet view
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if(newState == BottomSheetBehavior.STATE_COLLAPSED) {

                    floatingActionMenu.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar2.setVisibility(View.VISIBLE);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setTitle(getString(R.string.title_activity_map));

                } else if(newState == BottomSheetBehavior.STATE_EXPANDED) {

                    floatingActionMenu.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                    toolbar2.setVisibility(View.GONE);
                    setSupportActionBar(locationToolbar);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle("Peter O'Donnell Jr.");
                    getSupportActionBar().setHomeButtonEnabled(true);

                    setReview();


                } else if(newState == BottomSheetBehavior.STATE_DRAGGING) {

                    floatingActionMenu.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                floatingActionMenu.setAlpha(1 - slideOffset);
                toolbar.setAlpha(1 - slideOffset);
                toolbar2.setAlpha(1-slideOffset);
                if((1 - slideOffset) < .05) {
                    toolbar.setVisibility(View.GONE);
                    toolbar2.setVisibility(View.GONE);
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar2.setVisibility(View.VISIBLE);
                }
            }
        });

        toolbar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void setReview() {
        SimpleRatingBar overallRating = findViewById(R.id.stars);
        overallRating.setRating(4.3f);
        ScaleRatingBar spaceBar = findViewById(R.id.spaceBar);
        ScaleRatingBar activityBar = findViewById(R.id.activityBar);
        ScaleRatingBar wifiBar = findViewById(R.id.wifiBar);
        ScaleRatingBar cleanBar = findViewById(R.id.cleanBar);

        spaceBar.setRating(3);
        activityBar.setRating(4);
        wifiBar.setRating(2);
        cleanBar.setRating(5);
    }

    private void setUpMap() {
        checkPermissions();
        getMapType();

        // Get map asynchronously and add callback
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Current location setup
        mFusedLocationProviderApi = LocationServices.FusedLocationApi;
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
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        System.out.println("inflating");
        if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            getMenuInflater().inflate(R.menu.map, menu);

            /*SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/
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

            return true;
        } else if(id == R.id.action_maptype) {

            MaterialDialog mapDialog = new MaterialDialog.Builder(this)
                    .customView(R.layout.map_dialog, false)
                    .cancelable(true)
                    .title("MAP TYPE")
                    .negativeText("Cancel")
                    .positiveText("Ok")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        setMapType();
                                    }
                                }
                    )
                    .show();

            View mapDialogView = mapDialog.getCustomView();

            LinearLayout street = mapDialogView.findViewById(R.id.street);
            LinearLayout satellite = mapDialogView.findViewById(R.id.satellite);
            final RadioButton streetRb = mapDialogView.findViewById(R.id.streetbutton);
            final RadioButton satRb = mapDialogView.findViewById(R.id.satellitebutton);

            street.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    maptype = GoogleMap.MAP_TYPE_NORMAL;
                    streetRb.setChecked(true);
                    satRb.setChecked(false);
                }
            });

            satellite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    maptype = GoogleMap.MAP_TYPE_HYBRID;
                    streetRb.setChecked(false);
                    satRb.setChecked(true);
                }
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

        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnMarkerClickListener(this);

        setMapType();
        moveCamera();

        //Add markers
        LatLng sydney = new LatLng(30.286791, -97.7365);
        MarkerOptions options = new MarkerOptions().position(sydney).title("POB 1");
        mMap.addMarker(options);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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
                mLastKnownLocation = null;
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
                    } else {
                        Log.i("Location", "Current location is null");
                        mMap.getUiSettings().setMyLocationButtonEnabled(false);
                    }
                } else {
                    mLastKnownLocation = location;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
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