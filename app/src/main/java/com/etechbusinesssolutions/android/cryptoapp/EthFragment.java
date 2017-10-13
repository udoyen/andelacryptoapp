package com.etechbusinesssolutions.android.cryptoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by george on 10/10/17.
 */

public class EthFragment extends Fragment {

    // Used for logging
    //TODO: Remove
    public static final String LOG_TAG = EthFragment.class.getName();




    /**
     * Constant value for the github loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int DATABASE_LOADER_ID = 2;

    public EthFragment() {

        //Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.currency_base, container, false);

        // Create list of currencies
        final ArrayList<Currency> currency = new ArrayList<>();

        // TODO: Get information from the database


        // Create an {@link CurrencyAdapter}, whose data source is a list of {@link Currency}.
        // The adapter knows how to create the list items for each item in the list.
        CurrencyAdapter adapter = new CurrencyAdapter(getActivity(), currency);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // currency_base.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        return rootView;
    }

}
