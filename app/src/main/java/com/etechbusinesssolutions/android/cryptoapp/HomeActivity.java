package com.etechbusinesssolutions.android.cryptoapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.etechbusinesssolutions.android.cryptoapp.data.CryptoContract;
import com.etechbusinesssolutions.android.cryptoapp.data.CryptoCurrencyDBHelper;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements LoaderCallbacks<List<Currency>> {

    //TODO: Remove
    public static final String LOG_TAG = HomeActivity.class.getSimpleName();
    // URL for the currency data from cryptocompare
    private static final String CRYPTO_CURRENRY_URL = "https://min-api.cryptocompare.com/data/pricemulti";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int CRYPTOCURRENCY_LOADER_ID = 1;
    SQLiteDatabase db;
    //Create an instance of CryptoCurrencyDBHelper
    private CryptoCurrencyDBHelper mDBHelper;

    //TODO: Do something with this method
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        Log.i(LOG_TAG, "TEST: Connectivity Manager Instance created ...");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //check internet connection
        Log.i(LOG_TAG, "TEST: Internet connection checked ...");
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {

            // Get a reference to the loader manager in order to interact with loaders
            Log.i(LOG_TAG, "TEST: Get the LoadManager being used ...");
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.i(LOG_TAG, "TEST: Calling initloader()...");
            loaderManager.initLoader(CRYPTOCURRENCY_LOADER_ID, null, this);

        }

        // set the content activity to use for the activity_home.xml layout file
        setContentView(R.layout.activity_home);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);


        //Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),
                HomeActivity.this);

        // Set the adapter onto the view pager
        assert viewPager != null;
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tab);
        assert tabLayout != null;
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public Loader<List<Currency>> onCreateLoader(int id, Bundle args) {

        // Create a new loader for the given URL
        Log.i(LOG_TAG, "TEST: onCreateLoader() called ...");

        // Setup the baseURI
        Uri baseUri = Uri.parse(CRYPTO_CURRENRY_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("fsyms", "ETH,BTC");
        uriBuilder.appendQueryParameter("tsyms", "USD,EUR,NGN,RUB,CAD,JPY,GBP,AUD,INR,HKD,IDR,SGD,CHF,CNY,ZAR,THB,SAR,KRW,GHS,BRL");

        Log.i(LOG_TAG, "TEST: uriBuilder String" + uriBuilder.toString());

        return new CrytoCurrencyLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Currency>> loader, List<Currency> data) {
        //TODO: Load the information from CryptocrrencyQueryUtils into database using content provider
        Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");
        Log.i(LOG_TAG, "TEST: Database data insertion started ...");

        // Create a ContentValues class object
        ContentValues values = new ContentValues();

        // Check if database table already present, if it exists
        // then update current records instead of inserting.
        Log.i(LOG_TAG, "Checking if database is present...");


        boolean found = isTableExists();
        Log.i(LOG_TAG, "Found: " + found + "...");

        try {

            if (found) {

                try {
                    for (Currency element : data) {
                        values.put(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE, element.getcEthValue());
                        values.put(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE, element.getcBtcValue());

                        // Update database
                        int mRowsUpdated = getContentResolver().update(
                                CryptoContract.CurrencyEntry.CONTENT_URI,
                                values,
                                "_id = ?",
                                new String[]{String.valueOf(element.getcId())}
                        );

                        // Log data insertion to catch any errors
                        // TODO: Remove
                        Log.v("HomeActivity db update", "New row ID " + mRowsUpdated + " Element id " + element.getcId());
                        Log.i("Row Entry " + mRowsUpdated, element.getcName() + " " + element.getcEthValue() + " " + element.getcBtcValue());

                    }


                    Log.i(LOG_TAG, "TEST: Database data update finished ...");

                } catch (NullPointerException e) {

                    Log.i(LOG_TAG, "Update error iterating over the data ... " + e);
                }


            } else {

                try {

                    for (Currency element : data) {

                        values.put(CryptoContract.CurrencyEntry.COLUMN_CURRENCY_NAME, element.getcName());
                        values.put(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE, element.getcEthValue());
                        values.put(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE, element.getcBtcValue());

                        // Insert data into SQLiteDatabase
                        Uri uri = getContentResolver().insert(CryptoContract.CurrencyEntry.CONTENT_URI, values);
                        // Log data insertion to catch any errors
                        // TODO: Remove
                        Log.v("HomeActivity", "Insert new row ID " + uri);
                        Log.i("Row Entry ", element.getcName() + " " + element.getcEthValue() + " " + element.getcBtcValue());

                    }

                    //TODO: Remove
                    Log.i(LOG_TAG, "TEST: Database data insertion finished ...");

                } catch (NullPointerException e) {

                    Log.i(LOG_TAG, "database insert error no data to iterate over ... " + e);
                }


            }
        } catch (NullPointerException g) {

            Log.i(LOG_TAG, "Database existenct confirmation error " + g);
        }


    }

    @Override
    public void onLoaderReset(Loader<List<Currency>> loader) {

        Log.i(LOG_TAG, "TEST: onLoadReset() called ...");
        getLoaderManager().destroyLoader(1);

    }

    /**
     * Used to determine if the database exists
     * so either an update is done or insert.     *
     *
     * @return true
     */
    public boolean isTableExists() {

        String[] projection = {

                CryptoContract.CurrencyEntry._ID,
                CryptoContract.CurrencyEntry.COLUMN_CURRENCY_NAME,
                CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE,
                CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE

        };

        Cursor cursor = getContentResolver().query(CryptoContract.CurrencyEntry.CONTENT_URI, projection, null, null, null);

        assert cursor != null;
        boolean exists = (cursor.getCount() > 0);
        cursor.close();

        return exists;


    }
}
