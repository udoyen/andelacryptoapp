package com.connectsystems.georgek.cryptomonitoru1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.connectsystems.georgek.cryptomonitoru1.cryptoservice.JobSchedulerService;
import com.connectsystems.georgek.cryptomonitoru1.data.CryptoContract;
import com.connectsystems.georgek.cryptomonitoru1.networkutil.NetworkUtil;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Currency>> {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SimpleFragmentPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // URL for the currency data from cryptocompare
    private static final String CRYPTO_CURRENRY_URL = "https://min-api.cryptocompare.com/data/pricemulti";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int CRYPTOCURRENCY_LOADER_ID = 1;

    /**
     * JobScheduler Job ID
     */
    private static final int JOB_ID = 1;
    private static final String TAG = HomeActivity.class.getSimpleName();

    public static final String MY_INTENT = "com.connectsystems.georgek.cryptomonitoru1.CUSTOM_INTENT";
    private static final String CONNECTION_INTENT = "android.net.conn.CONNECTIVITY_CHANGE";

    /**
     * Create an instance of the JobScheduler class
     */
    JobScheduler mJobScheduler;

    /**
     * Used to set the menu items
     */
    Menu menu = null;
    /**
     * Used to check network status
     */
    String status;

    /**
     * Used to check network status
     */
    boolean online;

    MenuItem refreshMenuItem;

    private CurrencyUpdateBroadcastReceiver mCurrencyUpdateBroadcastReceiver;
    private boolean mExists;
    boolean found;
    public static boolean startedWithNetwork;

    private void receiverLoad() {


        if (startedWithNetwork) {
            refreshMenuItem = menu.findItem(R.id.menu_refresh);
            refreshMenuItem.setVisible(true);
            getLoaderManager().restartLoader(CRYPTOCURRENCY_LOADER_ID, null, HomeActivity.this);

        } else {
            getLoaderManager().initLoader(CRYPTOCURRENCY_LOADER_ID, null, HomeActivity.this);

        }
        getLoaderManager().getLoader(CRYPTOCURRENCY_LOADER_ID);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpStrictMode();

        mCurrencyUpdateBroadcastReceiver = new CurrencyUpdateBroadcastReceiver();


        //region intentfilter
        // Register the intent here
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MY_INTENT);
        intentFilter.addAction(CONNECTION_INTENT);
        registerReceiver(mCurrencyUpdateBroadcastReceiver, intentFilter);
        //endregion

        // Initialize JobScheduler
        //region jobscheduler
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJobScheduler.schedule(new JobInfo.Builder(JOB_ID,
                new ComponentName(this, JobSchedulerService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(60000) // one minute
                .build());
        //endregion

        //region network
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //check internet connection
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            startedWithNetwork = true;


            //call.run();
            //This is where my sync code will be, but for testing purposes I only have a Log statement            L
            // Get a reference to the loader manager in order to interact with loaders
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(CRYPTOCURRENCY_LOADER_ID, null, HomeActivity.this);

        } else {
            startedWithNetwork = false;
            Toast toast = Toast.makeText(getApplicationContext(), "Please activate your network to download currency values!", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.setBackground(ContextCompat.getDrawable(HomeActivity.this, R.drawable.toast_custom_look));
            toast.show();
        }
        //endregion


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tab);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


    }

    private void setUpStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build();
            StrictMode.setThreadPolicy(policy);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu options from the menu xml file
        //This add menu items to the app bar
        getMenuInflater().inflate(R.menu.network_available, menu);
        this.menu = menu;

        if (menu != null) {

            if (isConnected()) {
                // Let user know the status of the device network
                menu.findItem(R.id.menu_network_available).setVisible(true);
                menu.findItem(R.id.menu_network_absent).setVisible(false);
            } else {
                // Let user know the status of the device network
                menu.findItem(R.id.menu_network_available).setVisible(false);
                menu.findItem(R.id.menu_network_absent).setVisible(true);
            }


        }


        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem netMenuItem = menu.findItem(R.id.menu_network_available);
        MenuItem nonetMenuItem = menu.findItem(R.id.menu_network_absent);

        netMenuItem.setVisible(online);
        nonetMenuItem.setVisible(!online);

        return true;
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }


    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mCurrencyUpdateBroadcastReceiver, new IntentFilter(MY_INTENT));
        registerReceiver(mCurrencyUpdateBroadcastReceiver, new IntentFilter(CONNECTION_INTENT));
        getLoaderManager().restartLoader(CRYPTOCURRENCY_LOADER_ID, null, this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(mCurrencyUpdateBroadcastReceiver, new IntentFilter(MY_INTENT));
        registerReceiver(mCurrencyUpdateBroadcastReceiver, new IntentFilter(CONNECTION_INTENT));
        getLoaderManager().restartLoader(CRYPTOCURRENCY_LOADER_ID, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mCurrencyUpdateBroadcastReceiver);
    }


    @Override
    public android.content.Loader<List<Currency>> onCreateLoader(int id, Bundle args) {

        // Setup the baseURI
        Uri baseUri = Uri.parse(CRYPTO_CURRENRY_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("fsyms", "ETH,BTC");
        uriBuilder.appendQueryParameter("tsyms", "USD,EUR,NGN,RUB,CAD,JPY,GBP,AUD,INR,HKD,IDR,SGD,CHF,CNY,ZAR,THB,SAR,KRW,GHS,BRL");


        return new CrytoCurrencyLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Currency>> loader, final List<Currency> data) {

        //Used to delay the API data load icon on the Actionbar
        final Handler handler = new Handler();

        // Check if database table already present, if it exists
        // then update current records instead of inserting.
        found = isTableExists();
        try {

            if (found) {

                try {

                    @SuppressLint("StaticFieldLeak") AsyncTask<ContentValues, Void, Void> task = new AsyncTask<ContentValues, Void, Void>() {
                        @Override
                        protected Void doInBackground(ContentValues... contentValues) {
                            ContentValues contentValues1 = contentValues[0];
                            try {
                                for (Currency element : data) {
                                    contentValues1.put(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE, element.getcEthValue());
                                    contentValues1.put(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE, element.getcBtcValue());

                                    // Update database
                                    getContentResolver().update(
                                            CryptoContract.CurrencyEntry.CONTENT_URI,
                                            contentValues1,
                                            "_id = ?",
                                            new String[]{String.valueOf(element.getcId())}
                                    );


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            return null;
                        }
                    };

                    ContentValues contentValues = new ContentValues();

                    task.execute(contentValues);


                } catch (NullPointerException e) {

                    Log.i("Error", "Update error iterating over the data ... " + e);
                } catch (IllegalFormatException f) {

                    Log.i("Error", "Update format error ... " + f);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Remove the API call icon in Actionbar
                        refreshMenuItem = menu.findItem(R.id.menu_refresh);
                        refreshMenuItem.setVisible(false);
                    }
                }, 8000);


            } else {

                try {

                    @SuppressLint("StaticFieldLeak") AsyncTask<ContentValues, Void, Void> task = new AsyncTask<ContentValues, Void, Void>() {
                        @Override
                        protected Void doInBackground(ContentValues... contentValues) {
                            ContentValues contentValues1 = contentValues[0];
                            try {
                                for (Currency element : data) {

                                    contentValues1.put(CryptoContract.CurrencyEntry.COLUMN_CURRENCY_NAME, element.getcName());
                                    contentValues1.put(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE, element.getcEthValue());
                                    contentValues1.put(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE, element.getcBtcValue());

                                    // Insert data into SQLiteDatabase
                                    getContentResolver().insert(CryptoContract.CurrencyEntry.CONTENT_URI, contentValues1);


                                }
                            } catch (Exception e) {

                                e.printStackTrace();

                            }
                            return null;
                        }
                    };

                    ContentValues contentValues = new ContentValues();
                    task.execute(contentValues);


                } catch (NullPointerException e) {

                    Log.i("Error", "database insert error no data to iterate over ... " + e);
                } catch (IllegalFormatException f) {

                    Log.i("Error", "Update format error ... " + f);
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Remove the API call icon in Actionbar
                        refreshMenuItem = menu.findItem(R.id.menu_refresh);
                        refreshMenuItem.setVisible(false);
                    }
                }, 8000);


            }
        } catch (NullPointerException g) {

            Log.i("Error", "Database existent confirmation error " + g);

        } catch (IllegalFormatException f) {

            Log.i("Error", "Update format error ... " + f);
        }

    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Currency>> loader) {

        getLoaderManager().destroyLoader(CRYPTOCURRENCY_LOADER_ID);
        refreshMenuItem = menu.findItem(R.id.menu_refresh);
        refreshMenuItem.setVisible(false);

    }


    /**
     * Used to determine if the database exists
     * so either an update is done or insert.
     *
     * @return boolean
     */
    public boolean isTableExists() {
        Cursor cursor;
        String[] projections = {

                CryptoContract.CurrencyEntry._ID,
                CryptoContract.CurrencyEntry.COLUMN_CURRENCY_NAME,
                CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE,
                CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE

        };

        cursor = getContentResolver().query(CryptoContract.CurrencyEntry.CONTENT_URI, projections, null, null, null);
        assert cursor != null;
        mExists = (cursor.getCount() > 0);

        return mExists;


    }

    private class CurrencyUpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (Objects.equals(intent.getAction(), MY_INTENT)) {

                startedWithNetwork = true;
                receiverLoad();
            }

            // Set the network menu status
            if (Objects.equals(intent.getAction(), CONNECTION_INTENT)) {

                status = NetworkUtil.getConnectivityStatusString(context);
                online = (Objects.equals(status, "Wifi enabled") || Objects.equals(status, "Mobile data enabled"));
                supportInvalidateOptionsMenu();

            }


        }
    }


}
