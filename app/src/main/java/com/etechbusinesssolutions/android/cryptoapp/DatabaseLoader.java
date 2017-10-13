package com.etechbusinesssolutions.android.cryptoapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by george on 10/13/17.
 */

public class DatabaseLoader extends AsyncTaskLoader<List<Currency>> {

    // Used for logging
    //TODO: Remove
    public static final String LOG_TAG = DatabaseLoader.class.getName();


    private String mCurrency_Name;
    private long mCurrency_Value;


    public DatabaseLoader(Context context, String currency_name, long currency_value) {

        super(context);
        mCurrency_Name = currency_name;
        mCurrency_Value = currency_value;

    }

    @Override
    protected void onStartLoading() {

        Log.i(LOG_TAG, "TEST: onStartLoading() called ...");
        forceLoad();
    }

    @Override
    public List<Currency> loadInBackground() {

        Log.i(LOG_TAG, "Test: loadInBackground() called ...");

        // TODO: Create call to database to retrieve information for loader
        return null;
    }
}
