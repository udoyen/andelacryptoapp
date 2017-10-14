package com.etechbusinesssolutions.android.cryptoapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 10/13/17.
 */

public class DatabaseLoader extends AsyncTaskLoader<List<String>> {

    // Used for logging
    //TODO: Remove
    public static final String LOG_TAG = DatabaseLoader.class.getName();


    private ArrayList<String> mC;


    public DatabaseLoader(Context context, ArrayList<Cursor> vC) {

        super(context);
//        mC = vC;
//        mCurrency_Name = currency_name;
//        mCurrency_Value = currency_value;

    }

    @Override
    protected void onStartLoading() {

        Log.i(LOG_TAG, "TEST: onStartLoading() called ...");
        forceLoad();
    }

    @Override
    public List<String> loadInBackground() {

        Log.i(LOG_TAG, "Test: loadInBackground() called ...");

        // TODO: Create call to database to retrieve information for loader
        return null;
    }
}
