package com.etechbusinesssolutions.android.cryptoapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by george on 10/11/17.
 */

public class CurrencyAdapter extends ArrayAdapter<String> {

    //TODO: Remove
    private static final String LOG_TAG = CurrencyAdapter.class.getSimpleName();


    public CurrencyAdapter(Activity context, ArrayList<String> data) {

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.

        super(context, 0, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, else inflate the view
        View listItemView = convertView;
        if (listItemView == null ) {

            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.currency_list_item, parent, false
            );

            // Get the  object located at this position in the list
            getItem(position);

            // Get the TextViews and set their values
            TextView curCode = (TextView) listItemView.findViewById(R.id.currency_code);


            // TODO: Work on this convertion
            TextView curValue = (TextView) listItemView.findViewById(R.id.rate);
            //curValue.setText(Long.toString(currentCurrency.getcValue()));
        }
        return super.getView(position, convertView, parent);
    }
}
