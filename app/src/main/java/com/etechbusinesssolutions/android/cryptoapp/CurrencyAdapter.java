package com.etechbusinesssolutions.android.cryptoapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
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

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
