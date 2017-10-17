package com.etechbusinesssolutions.android.cryptoapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.etechbusinesssolutions.android.cryptoapp.data.CryptoContract;
import com.etechbusinesssolutions.android.cryptoapp.data.CryptoCurrencyDBHelper;

/**
 * Created by george on 10/10/17.
 */

public class EthFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // Used for logging
    //TODO: Remove
    public static final String LOG_TAG = EthFragment.class.getName();

    // Adapter for the list of currencies values gotten from database
    private EthCurrencyAdapter mAdapter;


    //TODO: Remove
    //Create an instance of CryptoCurrencyDBHelper
    private CryptoCurrencyDBHelper mDBHelper;

    /**
     * Constant value for the github loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int DATABASE_LOADER_ID = 3;


    public EthFragment() {

        //Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.currency_base, container, false);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // currency_base.xml layout file.
        ListView listView = rootView.findViewById(R.id.list);
        //TODO: Add an empty view for when no data exists

        // Create an {@link BtcCurrencyAdapter}, whose data source is a list of {@link Currency}.
        // The adapter knows how to create the list items for each item in the list.
        mAdapter = new EthCurrencyAdapter(getContext(), null, false);

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
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Read data from database and send to onLoadFinished()

        // Create instance of SQLiteDatabse class
        //SQLiteDatabase db = mDBHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the
        // database that will be used after this query
        String[] projection = {
                CryptoContract.CurrencyEntry._ID,
                CryptoContract.CurrencyEntry.COLUMN_CURRENCY_NAME,
                CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE
        };

        return new CursorLoader(getContext(),
                CryptoContract.CurrencyEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        //TODO: Send cursor information back to cursorAdapter
        mAdapter.swapCursor(data);


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Clears out the adapter's reference to the Cursor.
        // This prevents memory leaks.
        mAdapter.swapCursor(null);

    }
}
