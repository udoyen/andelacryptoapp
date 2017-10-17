package com.etechbusinesssolutions.android.cryptoapp;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by george on 10/13/17.
 */

public class DatabaseLoader extends CursorLoader {

    // Used for logging
    //TODO: Remove
    public static final String LOG_TAG = DatabaseLoader.class.getName();


    public DatabaseLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Cursor loadInBackground() {
        return super.loadInBackground();
    }
}
