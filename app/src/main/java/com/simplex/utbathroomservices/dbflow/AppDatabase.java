package com.simplex.utbathroomservices.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by dchun on 11/30/17.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "UTBSDatabase";

    public static final int VERSION = 1;

}
