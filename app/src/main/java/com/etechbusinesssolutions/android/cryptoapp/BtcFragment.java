package com.etechbusinesssolutions.android.cryptoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 10/10/17.
 */

public class BtcFragment extends Fragment  implements LoaderManager.LoaderCallbacks<List<String>>{

    // Used for logging
    //TODO: Remove
    public static final String LOG_TAG = BtcFragment.class.getName();

    // Adapter for the list of currencies values gotten from database
    private CurrencyAdapter mAdapter;


    /**
     * Constant value for the github loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int DATABASE_LOADER_ID = 2;

    // Used to setup UrlQuery String
    URL url = null;

    public BtcFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.currency_base, container, false);

        // Create an empty ArrayList of currencies
        final ArrayList<Currency> currency = new ArrayList<>();


        // Create an {@link CurrencyAdapter}, whose data source is a list of {@link Currency}.
        // The adapter knows how to create the list items for each item in the list.
        mAdapter= new CurrencyAdapter(getActivity(), currency);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // currency_base.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(mAdapter);

        //****LoadManager will load information****
        // Get a reference to the loader manager in order to interact with loaders
        Log.i(LOG_TAG, "TEST: Get the LoadManager being used ...");
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        Log.i(LOG_TAG, "TEST: Calling initloader()...");
        loaderManager.initLoader(DATABASE_LOADER_ID, null, this);

        return rootView;
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {

    }
}
