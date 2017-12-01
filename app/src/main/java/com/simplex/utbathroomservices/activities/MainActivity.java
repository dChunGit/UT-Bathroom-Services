package com.simplex.utbathroomservices.activities;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.SwitchCompat;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daasuu.ei.Ease;
import com.daasuu.ei.EasingInterpolator;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
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
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.simplex.utbathroomservices.dbflow.AppDatabase;
import com.simplex.utbathroomservices.dbflow.Favorite_Item;
import com.simplex.utbathroomservices.R;
import com.simplex.utbathroomservices.adapters.ReviewAdapter;
import com.simplex.utbathroomservices.cloudfirestore.Bathroom;
import com.simplex.utbathroomservices.cloudfirestore.Rating;
import com.simplex.utbathroomservices.cloudfirestore.WaterFountain;
import com.simplex.utbathroomservices.Database;
import com.simplex.utbathroomservices.fragments.LocationFragment;
import com.simplex.utbathroomservices.fragments.UpdateFragment;
import com.wang.avi.AVLoadingIndicatorView;
import com.willy.ratingbar.ScaleRatingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback,
        GoogleMap.OnCameraMoveStartedListener, LocationFragment.UpdateLocationListener, GoogleMap.OnMarkerClickListener,
        UpdateFragment.onUpdateListener {
    //TODO: Favorites activity
    //TODO: Reviews activity
    //TODO: Search Activity
    //TODO: Settings Activity
    //TODO: Camera API
    //TODO: Create service to update entries periodically
    final String SAVELOCATION = "SAVE LOCATION";
    final String LOCATIONGRANTED = "LOCATION GRANTED";
    final String FOLLOW = "FOLLOW";
    final String ZOOM = "ZOOM";
    final String BOTTOMSHEET = "BOTTOMSHEET";
    final String LOADING = "LOADING";
    final String TOOLBAR = "TOOLBAR";
    final String SELECTED = "SELECTED";
    final String position = "POSITION";
    private boolean whichtoolbar = false;
    private static final int ADD_LOCATION = 0;

    private GoogleMap mMap;
    private final LatLng mDefaultLocation = new LatLng(30.286310, -97.739560);
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private FusedLocationProviderApi mFusedLocationProviderApi;
    private Location mLastKnownLocation;
    private boolean followPerson;
    private int maptype = GoogleMap.MAP_TYPE_NORMAL;
    private float zoomLevel;

    private boolean addedNUpdate = false;

    private BottomSheetBehavior bottomSheetBehavior;
    private Toolbar toolbar, locationToolbar;
    private RelativeLayout bottomSheetLayout;
    private CardView cardToolbar;
    private TextView toolbar2, building, room, stallamount, bottlerefill;
    private DrawerLayout drawerLayout;
    private FloatingActionMenu floatingActionMenu;
    private AVLoadingIndicatorView sync;
    private LinearLayout bathroomreview, fountainreview;
    private FloatingActionButton addLocation, location, refresh;
    private RecyclerView recyclerView;

    private UpdateFragment updateFragment;
    private LocationFragment locationFragment;
    private Database database;
    private FragmentManager fragmentManager;
    private static final String TAG_TASK_FRAGMENT = "updateFragment";
    private static final String TAG_LOC_FRAGMENT = "locationFragment";
    private static final String TAG_DATA_FRAGMENT = "database";

    private HashMap<String, Bathroom> firebaseBRatings = new HashMap<>();
    private HashMap<String, WaterFountain> firebaseWRatings = new HashMap<>();
    private ArrayList<Bathroom> saveBathroom = new ArrayList<>();
    private ArrayList<WaterFountain> saveFountain= new ArrayList<>();
    private ArrayList<String> saveBuildings= new ArrayList<>();
    private ArrayList<Marker> mapMarkers = new ArrayList<>();
    private HashMap<String, MarkerOptions> newMapmarkers = new HashMap<>();
    private ArrayList<String> oldMapmarkers = new ArrayList<>();
    private boolean syncing = false, locUpdate = false;
    private String currentSelected;

    private android.support.design.widget.FloatingActionButton fab1;
    private HashMap<String, String> favorites = new HashMap<>();
    private HashMap<String, Favorite_Item> favoriteItems = new HashMap<>();

    private long mBackPressed;
    private static final int TIME_INTERVAL = 2000;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sync = findViewById(R.id.sync);
        followPerson = true;
        zoomLevel = 17f;

        bottomSheetLayout = findViewById(R.id.locationSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);

        fragmentManager = getSupportFragmentManager();
        updateFragment = (UpdateFragment) fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT);
        locationFragment = (LocationFragment) fragmentManager.findFragmentByTag(TAG_LOC_FRAGMENT);
        //database = (Database) fragmentManager.findFragmentByTag(TAG_DATA_FRAGMENT);
        database = (Database) getApplication();

        //for location updates
        checkPermissions();

        //to store data
        /*if(database == null) {
            database = Database.newInstance();
            fragmentManager.beginTransaction().add(database, TAG_DATA_FRAGMENT).commit();
        }*/

        setFont();
        setUpUI();
        setUpMap();
        FlowManager.init(FlowConfig.builder(this)
                .addDatabaseConfig(DatabaseConfig.builder(AppDatabase.class)
                        .databaseName("UTBSDatabase")
                        .build())
                .build());

        if(savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(SAVELOCATION);
            mLocationPermissionGranted = savedInstanceState.getBoolean(LOCATIONGRANTED);
            followPerson = savedInstanceState.getBoolean(FOLLOW);
            zoomLevel = savedInstanceState.getFloat(ZOOM);
            bottomSheetBehavior.setState(savedInstanceState.getInt(BOTTOMSHEET));
            whichtoolbar = savedInstanceState.getBoolean(TOOLBAR);
            if(whichtoolbar) {
                setBottomSheetToolbar();
            }
            syncing = savedInstanceState.getBoolean(LOADING);
            if(!syncing) {
                sync.hide();
            }
            if(database != null) {
                Log.d("MainActivity", "restoring data");
                firebaseBRatings = database.getFirebaseBRatings();
                firebaseWRatings = database.getFirebaseWRatings();
                saveBathroom = database.getSaveBathroom();
                saveFountain = database.getSaveFountain();
                saveBuildings = database.getSaveBuildings();
                mapMarkers = database.getMapMarkers();
                newMapmarkers = database.getNewMapmarkers();
                oldMapmarkers = database.getOldMapmarkers();
                favorites = database.getFavorites();
                favoriteItems = database.getFavoriteItems();
            }
            String temp = savedInstanceState.getString(SELECTED);
            if(temp != null) {
                currentSelected = temp;
                processReview(currentSelected);
            }
        } else {
            new Thread(() -> {
                System.out.println("Called");
                List<Favorite_Item> favoritesList = SQLite.select()
                        .from(Favorite_Item.class)
                        .queryList();

                for(Favorite_Item favorite_item : favoritesList) {
                    String key = favorite_item.getFavorite().split("@")[0].trim();
                    if(firebaseBRatings.containsKey(key) || firebaseWRatings.containsKey(key)) {
                        favorites.put(favorite_item.getFavorite(), favorite_item.getType());
                        favoriteItems.put(favorite_item.getFavorite(), favorite_item);
                    } else {
                        favorite_item.delete();
                    }
                }

                if(database != null) {
                    database.setFavorites(favorites);
                    database.setFavoriteItems(favoriteItems);
                }
                System.out.println("Favorites: " + favoritesList);
            }).start();

            updateEntries("Update");
        }

        if(!getTutorial()) {
            //show tutorial
            if(floatingActionMenu != null) {
                floatingActionMenu.open(true);
            }
            if(location != null) {
                location.setClickable(false);
            }
            if(addLocation != null) {
                location.setClickable(false);
            }
            if(refresh != null) {
                refresh.setClickable(false);
            }

            Toolbar temp = findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.map);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_menu));


            TapTargetSequence tapTargetSequence = new TapTargetSequence(this)
                    .targets(
                            TapTarget.forToolbarNavigationIcon(temp, "Open Drawer", "View more options")
                                    .id(0)
                                    .outerCircleColor(R.color.colorAccent3)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .textColor(R.color.white)
                                    .cancelable(false)
                                    .transparentTarget(true),
                            TapTarget.forToolbarMenuItem(temp, R.id.action_search, "Quick Search",
                                    "Use this to quickly filter")
                                    .id(1)
                                    .outerCircleColor(R.color.colorAccent)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .textColor(R.color.white)
                                    .cancelable(false)
                                    .transparentTarget(true),
                            TapTarget.forToolbarOverflow(toolbar, "More Options", "Map type and quick access links")
                                    .id(2)
                                    .outerCircleColor(R.color.colorAccent2)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .textColor(R.color.white)
                                    .cancelable(false)
                                    .transparentTarget(true),
                            TapTarget.forView(findViewById(R.id.location),
                                    "Set Map Camera to Your Location",
                                    "Once clicked, the map will follow your location until moved manually")
                                    .id(3)
                                    .outerCircleColor(R.color.colorAccent4)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .textColor(R.color.white)
                                    .cancelable(false)
                                    .transparentTarget(true),
                            TapTarget.forView(findViewById(R.id.newLocation),
                                    "Add a NEW bathroom/water fountain location",
                                    "Uses your location to add a new facility to the map")
                                    .id(4)
                                    .outerCircleColor(R.color.colorAccent3)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .textColor(R.color.white)
                                    .cancelable(false)
                                    .transparentTarget(true),
                            TapTarget.forView(findViewById(R.id.refresh),
                                    "Sync the Database",
                                    "Refreshes the local database and populates the map")
                                    .id(5)
                                    .outerCircleColor(R.color.colorAccent5)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .textColor(R.color.white)
                                    .cancelable(false)
                                    .transparentTarget(true),
                            TapTarget.forView(findViewById(R.id.toolbar2),
                                    "Open to view reviews",
                                    "Shows overall ratings, individual reviews, and location information")
                                    .id(6)
                                    .outerCircleColor(R.color.colorAccent)
                                    .targetCircleColor(R.color.white)
                                    .titleTextSize(20)
                                    .textColor(R.color.white)
                                    .cancelable(false)
                                    .transparentTarget(true))
                    .listener(new TapTargetSequence.Listener() {
                        @Override
                        public void onSequenceFinish() {
                            // Yay
                            if(floatingActionMenu != null) {
                                floatingActionMenu.close(true);
                            }
                            if(location != null) {
                                location.setClickable(true);
                            }
                            if(addLocation != null) {
                                location.setClickable(true);
                            }
                            if(refresh != null) {
                                refresh.setClickable(true);
                            }
                            setTutorialComplete();
                            checkPermissions();
                            if(mLocationPermissionGranted) {
                                /*followPerson = true;
                                updateLocationUI();
                                setDeviceLocation(null);*/
                            }
                            floatingActionMenu.close(true);
                        }

                        @Override
                        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
                            if(lastTarget.id() == 5) {
                                doBounceAnimation(bottomSheetLayout);
                            }
                        }

                        @Override
                        public void onSequenceCanceled(TapTarget lastTarget) {
                            setTutorialComplete();
                        }
                    });
            tapTargetSequence.start();
        }

    }

    @TargetApi(23)
    private void setUpSheet(){
        NestedScrollView nestedScrollView = findViewById(R.id.scrollReview);
        android.support.design.widget.FloatingActionButton fab = findViewById(R.id.addreview);
        nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY)
                    fab.hide();
                else if (scrollY < oldScrollY)
                    fab.show();
            }
        });

        fab1 = findViewById(R.id.starReview);
        fab1.setOnClickListener((view) -> {
            if(currentSelected != null) {
                if (!favorites.containsKey(currentSelected)) {
                    Favorite_Item favorite_item = new Favorite_Item();
                    favorite_item.setFavorite(currentSelected);
                    favorite_item.setType(currentSelected.split("@")[1].trim());

                    favorites.put(favorite_item.getFavorite(), favorite_item.getType());
                    favoriteItems.put(favorite_item.getFavorite(), favorite_item);
                    favorite_item.save();
                    fab1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star));

                    if(database != null) {
                        database.setFavorites(favorites);
                        database.setFavoriteItems(favoriteItems);
                    }

                    Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_SHORT).show();

                } else {
                    Favorite_Item selected = favoriteItems.get(currentSelected);
                    favorites.remove(selected.getFavorite());
                    favoriteItems.remove(selected.getFavorite());
                    selected.delete();
                    fab1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border));

                    if(database != null) {
                        database.setFavorites(favorites);
                        database.setFavoriteItems(favoriteItems);
                    }

                    Toast.makeText(this, "Removed from Favorites", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void doBounceAnimation(View targetView) {
        if(targetView != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationY", 0, -40, 0);
            animator.setInterpolator(new EasingInterpolator(Ease.CUBIC_IN_OUT));
            animator.setStartDelay(500);
            animator.setDuration(1000);
            animator.start();
        }
    }

    private void doBounceAnimationH(View targetView) {
        if(targetView != null) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(targetView, "translationX", 0, 40, 0);
            animator.setInterpolator(new EasingInterpolator(Ease.CUBIC_IN_OUT));
            animator.setStartDelay(500);
            animator.setDuration(1500);
            animator.start();
        }
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

        floatingActionMenu = findViewById(R.id.menu);
        building = findViewById(R.id.buildingname);
        room = findViewById(R.id.roomname);
        stallamount = findViewById(R.id.numstalls);
        bathroomreview = findViewById(R.id.bathroomreview);
        fountainreview = findViewById(R.id.fountainreview);
        bottlerefill = findViewById(R.id.isbottlerefill);

        setUpSheet();


        location = findViewById(R.id.location);
        location.setOnClickListener((view) ->{
            if(mLocationPermissionGranted) {
                followPerson = true;
                updateLocationUI();
                setDeviceLocation(null);
                floatingActionMenu.close(true);
            }
        });

        addLocation = findViewById(R.id.newLocation);
        addLocation.setOnClickListener((view) -> {
            if(!syncing && locUpdate) {

                floatingActionMenu.close(true);
                Intent settings = new Intent(MainActivity.this, Add.class);
                //not ideal, should make location parcelable or something
                if (mLastKnownLocation != null) {
                    Log.d("MainActivity", "Putting in extra " + mLastKnownLocation.toString());
                    settings.putExtra("Location", mLastKnownLocation);
                }
                startActivityForResult(settings, ADD_LOCATION);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            } else {
                Toast.makeText(this, "Please wait until sync is complete", Toast.LENGTH_SHORT).show();
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

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener((view) -> {
            floatingActionMenu.close(true);
            updateEntries("Update");
        });

        android.support.design.widget.FloatingActionButton addfab = findViewById(R.id.addreview);
        addfab.setOnClickListener((view) -> {
            Intent settings = new Intent(MainActivity.this, Add.class);
            //send bathroom/fountain
            settings.putExtra("Selected", currentSelected);
            startActivityForResult(settings, ADD_LOCATION);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbar2 = findViewById(R.id.toolbar2);
        cardToolbar = findViewById(R.id.cardToolbar);
        locationToolbar = findViewById(R.id.location_toolbar);

        //get bottom sheet behavior from bottom sheet view
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetCallback() {

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

                if(newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    setTopSheetToolbar();

                } else if(newState == BottomSheetBehavior.STATE_EXPANDED) {
                    setBottomSheetToolbar();
                    doBounceAnimationH(recyclerView);

                } else if(newState == BottomSheetBehavior.STATE_DRAGGING) {

                    floatingActionMenu.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                    toolbar2.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //nice fade and switch views
                floatingActionMenu.setAlpha(1 - slideOffset);
                cardToolbar.setAlpha(1 - slideOffset);
                toolbar2.setAlpha(1-slideOffset);
                //sets threshold so view doesn't have a hitch on slide
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
            //the bottom toolbar displays selected location
            String text = toolbar2.getText().toString();
            Log.d("MainActivity", text);
            if(!text.equals("No Location")) {
                String type = text.split("@")[1].trim();
                String key = text.split("@")[0].trim();
                switch(type) {
                    case "Bathroom": setReview(firebaseBRatings.get(key)); break;
                    case "Fountain": setReview(firebaseWRatings.get(key));
                }
            }
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

    }

    private void setTopSheetToolbar() {

        floatingActionMenu.setVisibility(View.VISIBLE);
        cardToolbar.setVisibility(View.VISIBLE);
        toolbar2.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_activity_map));
        whichtoolbar = false;
    }

    private void setBottomSheetToolbar() {

        floatingActionMenu.setVisibility(View.GONE);
        cardToolbar.setVisibility(View.GONE);
        toolbar2.setVisibility(View.GONE);
        setSupportActionBar(locationToolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        whichtoolbar = true;
    }

    private void setReview(Object location) {
        if(location != null) {
            Log.d("MainActivity", "Setting review " + location.toString());
            recyclerView = findViewById(R.id.reviewRecycler);

            float orate;
            ArrayList<Rating> ratings;
            ArrayList<String> imageUrls = new ArrayList<>();
            SimpleRatingBar overallRating = findViewById(R.id.stars);

            if (location instanceof Bathroom) {

                bathroomreview.setVisibility(View.VISIBLE);
                fountainreview.setVisibility(View.GONE);

                int activity = 0, wifi = 0, clean = 0, stalls = 0, spaceT = 0;
                String space;

                Bathroom bathroom = (Bathroom) location;
                orate = bathroom.getOverallRating();
                space = bathroom.getSpace();
                stalls = bathroom.getNumberStalls();
                activity = bathroom.getBusyness();
                wifi = bathroom.getWifiQuality();
                clean = bathroom.getCleanliness();
                ratings = bathroom.getRating();
                imageUrls = bathroom.getImage();

                if(favoriteItems.containsKey(bathroom.getBuilding() + " " + bathroom.getFloor() + " @ Bathroom")) {
                    fab1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star));
                } else {
                    fab1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border));
                }

                ReviewAdapter reviewAdapter = new ReviewAdapter(this, ratings, "Bathroom");
                recyclerView.setAdapter(reviewAdapter);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 0));

                overallRating.setRating(orate);
                ScaleRatingBar spaceBar = findViewById(R.id.spaceBar);
                ScaleRatingBar activityBar = findViewById(R.id.activityBar);
                ScaleRatingBar wifiBar = findViewById(R.id.wifiBar);
                ScaleRatingBar cleanBar = findViewById(R.id.cleanBar);

                switch (space) {
                    case "XSmall":
                        spaceT = 1;
                        break;
                    case "Small":
                        spaceT = 2;
                        break;
                    case "Medium":
                        spaceT = 3;
                        break;
                    case "Large":
                        spaceT = 4;
                        break;
                    case "XLarge":
                        spaceT = 5;
                        break;
                    default:
                        spaceT = 0;
                }

                spaceBar.setRating(spaceT);
                activityBar.setRating(activity);
                wifiBar.setRating(wifi);
                cleanBar.setRating(clean);
                stallamount.setText(String.valueOf(stalls));

                building.setText(bathroom.getBuilding());
                room.setText(bathroom.getFloor());

            } else if (location instanceof WaterFountain) {

                bathroomreview.setVisibility(View.GONE);
                fountainreview.setVisibility(View.VISIBLE);

                boolean refill;
                String taste, temperature;
                int tasteT, tempT;

                WaterFountain waterFountain = (WaterFountain) location;
                refill = waterFountain.isBottleRefillStation();
                taste = waterFountain.getTaste();
                temperature = waterFountain.getTemperature();
                orate = waterFountain.getOverallRating();
                ratings = waterFountain.getRating();
                imageUrls = waterFountain.getImage();

                if(favoriteItems.containsKey(waterFountain.getBuilding() + " " + waterFountain.getFloor() + " @ Fountain")) {
                    fab1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star));
                } else {
                    fab1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_star_border));
                }

                ReviewAdapter reviewAdapter = new ReviewAdapter(this, ratings, "Fountain");
                recyclerView.setAdapter(reviewAdapter);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 0));

                overallRating.setRating(orate);
                if (refill) {
                    bottlerefill.setText("Yes");
                } else bottlerefill.setText("No");

                ScaleRatingBar tasteBar = findViewById(R.id.tasteBar);
                ScaleRatingBar tempBar = findViewById(R.id.tempBar);

                switch (temperature) {
                    case "cold":
                        tempT = 5;
                        break;
                    case "cool":
                        tempT = 4;
                        break;
                    case "lukewarm":
                        tempT = 3;
                        break;
                    case "warm":
                        tempT = 2;
                        break;
                    case "hot":
                        tempT = 1;
                        break;
                    default:
                        tempT = 0;
                }

                switch (taste) {
                    case "Wow":
                        tasteT = 5;
                        break;
                    case "Pretty Good":
                        tasteT = 4;
                        break;
                    case "Meh":
                        tasteT = 3;
                        break;
                    case "Not Great":
                        tasteT = 2;
                        break;
                    case "Disgusting":
                        tasteT = 1;
                        break;
                    default:
                        tasteT = 0;
                }

                tasteBar.setRating(tasteT);
                tempBar.setRating(tempT);

                building.setText(waterFountain.getBuilding());
                room.setText(waterFountain.getFloor());
            }
        }
    }

    private void setUpMap() {
        checkPermissions();
        getMapType();

        // Get map asynchronously and add callback
        final SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map, supportMapFragment).commit();
        runOnUiThread(() -> supportMapFragment.getMapAsync(MainActivity.this));

        // Current location setup
        mFusedLocationProviderApi = LocationServices.FusedLocationApi;
    }

    private void updateEntries(String type) {
        Log.d("MainActivity", "Call to update");
        if(updateFragment == null) {
            syncing = true;
            updateFragment = UpdateFragment.newInstance(type);
            fragmentManager.beginTransaction().add(updateFragment, TAG_TASK_FRAGMENT).commit();
            if(sync != null && type.equalsIgnoreCase("Update")) {
                sync.smoothToShow();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MainActivity", "TAG, onSavedInstanceState");
        outState.putParcelable(SAVELOCATION, mLastKnownLocation);
        outState.putBoolean(LOCATIONGRANTED, mLocationPermissionGranted);
        outState.putBoolean(FOLLOW, followPerson);
        outState.putFloat(ZOOM, zoomLevel);
        outState.putInt(BOTTOMSHEET, bottomSheetBehavior.getState());
        outState.putBoolean(TOOLBAR, whichtoolbar);
        outState.putBoolean(LOADING, syncing);
        outState.putString(SELECTED, currentSelected);

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
        Log.d("MainActivity", "inflating");
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
        } else if (id == R.id.action_about) {

            Intent settings = new Intent(this, About.class);
            startActivity(settings);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            return true;
        } else if(id == R.id.action_search) {
            String type;

            MaterialDialog m = new MaterialDialog.Builder(this)
                    .customView(R.layout.filter_dialog, true)
                    .cancelable(true)
                    .title("QUICK SEARCH")
                    .negativeText("Cancel")
                    .positiveText("Search")
                    .onPositive((dialog, which) -> {
                        if(which == DialogAction.POSITIVE) {
                            Toast.makeText(getApplicationContext(), "Searching", Toast.LENGTH_SHORT).show();
                        }
                        View filterDialogView = dialog.getCustomView();
                        Spinner typespinner = filterDialogView.findViewById(R.id.typespinnerDialog);
                        Spinner tempspinner = filterDialogView.findViewById(R.id.tempPicker_dialog);
                        Spinner tastespinner = filterDialogView.findViewById(R.id.tastePicker_dialog);
                        SwitchCompat switchRefill = filterDialogView.findViewById(R.id.refillStation_dialog);
                        ScaleRatingBar overallDialog = filterDialogView.findViewById(R.id.overallBar_dialog);
                        ScaleRatingBar spaceDialog = filterDialogView.findViewById(R.id.spaceBar_dialog);
                        ScaleRatingBar activityDialog = filterDialogView.findViewById(R.id.activityBar_dialog);
                        ScaleRatingBar wifiDialog= filterDialogView.findViewById(R.id.wifiBar_dialog);
                        ScaleRatingBar cleanDialog = filterDialogView.findViewById(R.id.cleanBar_dialog);

                        Log.d("MainActivity", typespinner.getSelectedItem() + " " + tempspinner.getSelectedItem() + " " +
                                switchRefill.isChecked() + " " + tastespinner.getSelectedItem() + " " + overallDialog.getRating() +
                                " " + spaceDialog.getRating() + " " + activityDialog.getRating() + " " +
                                wifiDialog.getRating() + " " + cleanDialog.getRating());
                    })
                    .show();

            View customDialog = m.getCustomView();
            LinearLayout bathroomLL = customDialog.findViewById(R.id.overall_dialog);
            LinearLayout fountainLL = customDialog.findViewById(R.id.fountainAdd_dialog);

            Spinner typespinner = customDialog.findViewById(R.id.typespinnerDialog);
            Spinner tempspinner = customDialog.findViewById(R.id.tempPicker_dialog);
            Spinner tastespinner = customDialog.findViewById(R.id.tastePicker_dialog);
            ArrayAdapter<CharSequence> spinneradapter = ArrayAdapter.createFromResource(this,
                    R.array.type_array, R.layout.spinner_item);
            ArrayAdapter<CharSequence> tempadapter = ArrayAdapter.createFromResource(this,
                    R.array.temp_array, R.layout.spinner_item);
            ArrayAdapter<CharSequence> tasteadapter = ArrayAdapter.createFromResource(this,
                    R.array.taste_array, R.layout.spinner_item);
            spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tempadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tasteadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typespinner.setAdapter(spinneradapter);
            tempspinner.setAdapter(tempadapter);
            tastespinner.setAdapter(tasteadapter);
            typespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    switch(i) {
                        case 0: {
                            bathroomLL.setVisibility(View.VISIBLE);
                            fountainLL.setVisibility(View.GONE);
                        } break;
                        case 1: {
                            fountainLL.setVisibility(View.VISIBLE);
                            bathroomLL.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


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
            //send bathroom/fountain
            settings.putParcelableArrayListExtra("BRatings", saveBathroom);
            settings.putParcelableArrayListExtra("WRatings", saveFountain);
            startActivity(settings);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        } else if(id == R.id.reviews) {

            Intent settings = new Intent(MainActivity.this, Reviews.class);
            //send bathroom/fountain
            settings.putParcelableArrayListExtra("BRatings", saveBathroom);
            settings.putParcelableArrayListExtra("WRatings", saveFountain);
            startActivity(settings);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        } else if (id == R.id.advanced_search) {
            //advanced search algorithm

        } else if(id == R.id.about) {

            Intent i = new Intent(this, About.class);
            i.putExtra(position, 0);
            startActivity(i);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            return true;
        } else if(id == R.id.library) {
            Intent i = new Intent(this, About.class);
            i.putExtra(position, 1);
            startActivity(i);
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            return true;
        } else if(id == R.id.contact) {
            //contact us dialog information
            MaterialDialog m = new MaterialDialog.Builder(this)
                    .customView(R.layout.contact_item, false)
                    .cancelable(true)
                    .title("Contact Us")
                    .titleColorRes(R.color.colorAccent)
                    .positiveText("Go")
                    .onPositive((dialog, which) -> {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        Toast.makeText(getApplicationContext(), getString(R.string.openLink) + " Github", Toast.LENGTH_SHORT).show();

                        //build and launch tab
                        CustomTabsIntent customTabsIntent = builder.build();
                        String url = getApplicationContext().getResources().getString(R.string.contactURL);
                        customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                    })
                    .negativeText("Cancel")
                    .show();

            View view = m.getCustomView();
            TextView githubIssue = view.findViewById(R.id.contactURL);
            githubIssue.setOnClickListener((view1) -> {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                Toast.makeText(getApplicationContext(), getString(R.string.openLink) + " Github", Toast.LENGTH_SHORT).show();

                //build and launch tab
                CustomTabsIntent customTabsIntent = builder.build();
                String url = getApplicationContext().getResources().getString(R.string.contactURL);
                customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            });
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

        initializeMap();

    }

    private void initializeMap() {
        setMapType();
        moveCamera();

        if(!newMapmarkers.isEmpty()) {
            addMarkers(new ArrayList<>(), true);
        }
    }

    private void checkPermissions() {

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //for location updates
            if(locationFragment == null) {
                Log.d("MainActivity", "Starting Location Updates");
                locationFragment = LocationFragment.newInstance();
                fragmentManager.beginTransaction().add(locationFragment, TAG_LOC_FRAGMENT).commit();
            }

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
                    //for location updates
                    if(locationFragment == null) {
                        Log.d("MainActivity", "Starting Location Updates");
                        locationFragment = LocationFragment.newInstance();
                        fragmentManager.beginTransaction().add(locationFragment, TAG_LOC_FRAGMENT).commit();
                    }
                }
            }
        }


    }

    private void processReview(String title) {
        if(toolbar2 != null) {
            toolbar2.setText(title);
            String[] splitter = title.split("@");
            String type = splitter[1];
            String key = splitter[0].trim();
            Log.d("MainActivity", type);

            if (type.contains("Bathroom")) {
                setReview(firebaseBRatings.get(key));
            } else if (type.contains("Fountain")) {
                setReview(firebaseWRatings.get(key));
            }
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String title = marker.getTitle();
        currentSelected = title;
        doBounceAnimation(bottomSheetLayout);
        processReview(title);

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
                    Location current = null;
                    if(locationFragment != null) {
                        current = mFusedLocationProviderApi.getLastLocation(locationFragment.getCurrentLocation());
                    }
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
    public void onLocationUpdate(Location location) {
        Log.d("MainActivity", "Updating Location: " + location);
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
        if(mMap != null) {
            if (mLastKnownLocation != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(mLastKnownLocation.getLatitude(),
                                mLastKnownLocation.getLongitude()), zoomLevel));
            } else {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, zoomLevel));
            }
        }
    }

    private void setTutorialComplete() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("TUTORIAL", true);
        editor.apply();
    }

    private boolean getTutorial() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        return sharedPref.getBoolean("TUTORIAL", false);
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

    private void addMarkers(ArrayList<MarkerOptions> markers, boolean last) {
        for(MarkerOptions m : markers) {
            newMapmarkers.put(m.getTitle(), m);
        }

        if(last) {
            Log.d("MainActivity", "New: " + newMapmarkers);
            Log.d("MainActivity", "Old: " + oldMapmarkers);
            Log.d("MainActivity", "Markers: " + mapMarkers);

            //inefficient, should prob only remove ones no longer in the map
            for(Marker m : mapMarkers) {
                Log.d("MainActivity", "Removing");
                m.remove();
            }

            mapMarkers.clear();
            oldMapmarkers.clear();

            for(String title : newMapmarkers.keySet()) {
                Log.d("MainActivity", title);
                MarkerOptions markerOptions = newMapmarkers.get(title);
                Marker marker = mMap.addMarker(markerOptions);

                oldMapmarkers.add(marker.getId());
                mapMarkers.add(marker);
            }
            
            if(database != null) {
                database.setNewMapmarkers(newMapmarkers);
                database.setMapMarkers(mapMarkers);
                database.setOldMapmarkers(oldMapmarkers);
            }

            newMapmarkers.clear();
        }

    }

    @Override
    public void onUpdateBFinish(HashMap<String, Bathroom> firebaseUpdate, ArrayList<Bathroom> resultsList,
                                ArrayList<MarkerOptions> markers, boolean doAll) {
        runOnUiThread(() -> {
            firebaseBRatings = firebaseUpdate;
            saveBathroom = resultsList;
            
            if(database != null) {
                database.setFirebaseBRatings(firebaseBRatings);
                database.setSaveBathroom(saveBathroom);
            }

            Log.d("MainActivity", firebaseBRatings.toString());
            updateFragment = null;
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT)).commitAllowingStateLoss();

            if(!doAll) {
                syncing = false;
                addMarkers(markers, true);
                if(sync != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> sync.smoothToHide(), 1000);
                }
            } else {
                addMarkers(markers, false);
                updateEntries("Fountain|Update");
            }

        });
    }

    @Override
    public void onUpdateWFinish(HashMap<String, WaterFountain> firebaseUpdate, ArrayList<WaterFountain> resultsList,
                                ArrayList<MarkerOptions> markers, boolean doAll) {
        runOnUiThread(() -> {
            firebaseWRatings = firebaseUpdate;
            saveFountain = resultsList;
            
            if(database != null) {
                database.setFirebaseWRatings(firebaseWRatings);
                database.setSaveFountain(saveFountain);
            }
            
            Log.d("MainActivity", firebaseWRatings.toString());
            addMarkers(markers, true);
            updateFragment = null;
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT)).commitAllowingStateLoss();

            if(!doAll) {
                syncing = false;
                if (sync != null) {
                    Handler handler = new Handler();
                    handler.postDelayed(() -> sync.smoothToHide(), 1000);
                }
            } else {
                updateEntries("Buildings");
            }
        });
    }

    @Override
    public void onUpdateBuildingFinish(ArrayList<String> b) {
        runOnUiThread(() -> {
            saveBuildings = b;
            
            if(database != null) {
                database.setSaveBuildings(saveBuildings);
            }
            
            updateFragment = null;
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT)).commitAllowingStateLoss();

            syncing = false;
            if (sync != null) {
                Handler handler = new Handler();
                handler.postDelayed(() -> sync.smoothToHide(), 1000);
            }

            //update review selected if it is currently opened
            if(bottomSheetBehavior != null) {
                Log.d("MainActivity", "bottomsheetbehavior not null");
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    if(currentSelected != null) {
                        Log.d("MainActivity", currentSelected + " not null");
                        processReview(currentSelected);
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_TASK_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {

        }
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_LOC_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {

        }
        try {
            fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag(TAG_DATA_FRAGMENT)).commitAllowingStateLoss();
        } catch (Exception e) {
            
        }
        locationFragment = null;
        updateFragment = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        saveMapType();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(addedNUpdate) {
            updateEntries("Update");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_LOCATION) {
            //can't invoke directly bc of savedInstanceState
            if(resultCode == Activity.RESULT_OK){
                addedNUpdate = true;
            }
        }
    }
}