package com.simplex.utbathroomservices;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener, LocationCallback {
    //TODO: Save current location and update
    String SAVELOCATION = "SAVE LOCATION";
    String LOCATIONGRANTED = "LOCATION GRANTED";
    String FOLLOW = "FOLLOW";
    String ZOOM = "ZOOM";

    private BottomSheetBehavior bottomSheetBehavior;

    private GoogleMap mMap;
    //private GoogleLocationService googleLocationService;
    private final LatLng mDefaultLocation = new LatLng(30.2849, -97.7341);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted, toolbarToggle = false;
    private FusedLocationProviderApi mFusedLocationProviderApi;
    private Location mLastKnownLocation;
    private boolean followPerson;
    private int maptype = GoogleMap.MAP_TYPE_NORMAL;
    private float zoomLevel;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;

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

        if(savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(SAVELOCATION);
            mLocationPermissionGranted = savedInstanceState.getBoolean(LOCATIONGRANTED);
            followPerson = savedInstanceState.getBoolean(FOLLOW);
            zoomLevel = savedInstanceState.getFloat(ZOOM);
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

        RelativeLayout bottomSheetLayout = findViewById(R.id.locationSheet);
        final FloatingActionMenu floatingActionMenu = findViewById(R.id.menu);
        final Toolbar toolbar2 = findViewById(R.id.toolbar2);
        final Toolbar locationToolbar = findViewById(R.id.location_toolbar);

        //get bottom sheet behavior from bottom sheet view
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
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

                } else if(newState == BottomSheetBehavior.STATE_EXPANDED) {

                    floatingActionMenu.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                    toolbar2.setVisibility(View.GONE);
                    setSupportActionBar(locationToolbar);
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void setUpMap() {
        checkPermissions();
        getMapType();

        // Get map asynchronously and add callback
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Current location setup
        mFusedLocationProviderApi = LocationServices.FusedLocationApi;
        /*googleLocationService = new GoogleLocationService(this);
        googleLocationService.startUpdates();*/
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

            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        System.out.println("Option selected");
        if(id == android.R.id.home) {
            if(bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                drawerLayout.openDrawer(GravityCompat.START);
            } else {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            return true;
        }  else if (id == R.id.action_settings) {
            Intent settings = new Intent(this, Settings.class);
            startActivity(settings);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            return true;
        } else if(id == R.id.action_maptype) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mapDialogView = inflater.inflate(R.layout.map_dialog, null);

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

            new MaterialStyledDialog.Builder(this)
                    .setCustomView(mapDialogView)
                    .setCancelable(true)
                    .setTitle("CHOOSE MAP TYPE")
                    .setStyle(Style.HEADER_WITH_TITLE)
                    .setNegativeText("Cancel")
                    .setPositiveText("Ok")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        setMapType();
                                    }
                                }
                    )
                    .show();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.favorites) {
            // Handle the camera action
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

        //checkPermissions();
        setMapType();
        //updateLocationUI();
        //setDeviceLocation(null);

        //Add markers
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));*/
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
        /*if (googleLocationService != null) {
            googleLocationService.stopLocationUpdates();
        }*/
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
        /*if (googleLocationService != null) {
            startService(new Intent(this, LocationService.class));
        }*/
    }
}