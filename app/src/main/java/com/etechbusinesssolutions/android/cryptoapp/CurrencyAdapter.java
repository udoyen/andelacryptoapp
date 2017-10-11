package com.etechbusinesssolutions.android.cryptoapp;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by george on 10/11/17.
 */

public class CurrencyAdapter extends ArrayAdapter<Currency> {

    //TODO: Remove
    private static final String LOG_TAG = CurrencyAdapter.class.getSimpleName();


    public CurrencyAdapter(Activity context, ArrayList<Currency> currency) {
        super(context, 0, currency);
    }
}
