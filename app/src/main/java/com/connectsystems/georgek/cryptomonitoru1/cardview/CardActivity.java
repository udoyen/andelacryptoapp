package com.connectsystems.georgek.cryptomonitoru1.cardview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.connectsystems.georgek.cryptomonitoru1.R;
import com.connectsystems.georgek.cryptomonitoru1.conversion.ConversionActivity;
import com.connectsystems.georgek.cryptomonitoru1.data.CryptoContract;
import com.connectsystems.georgek.cryptomonitoru1.data.CryptoCurrencyDBHelper;
import com.connectsystems.georgek.cryptomonitoru1.data.CurrencyHelper;
import com.connectsystems.georgek.cryptomonitoru1.networkutil.NetworkUtil;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

public class CardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    // String to identify intent source
    private static final String ETH_CODE = "eth_value";
    private static final String BTC_CODE = "btc_value";
    private static final String CONNECTION_INTENT = "android.net.conn.CONNECTIVITY_CHANGE";
    /**
     * JobScheduler Job ID
     */
    private static final int JOB_ID = 1;
    // Create a spinners
    Spinner spinner;
    Spinner curSpinner;
    /**
     * Name of the database currency
     * value from the Intent origin.
     */
    String currency_code;
    /**
     * Name of the column for which
     * the Intent originated
     */
    int columnPosition;
    /**
     * Format to use for displayed currencies
     */
    DecimalFormat df = new DecimalFormat("#,###.###");
    // Get the Card currency value
    TextView curValue;
    // The Currency logo
    TextView logoText;
    // Get the cryto currency image
    ImageView cryptImage;
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


    //Create an instance of CryptoCurrencyDBHelper
    private CryptoCurrencyDBHelper mDBHelper;
    /**
     * Used to check if the spinner is
     * drawn for the first time
     */
    private boolean spinnerClicked = false;
    /**
     * Used to check if the crypto spinner
     * was drawn for the first time
     */
    private boolean curSpinnerClicked = false;
    private String mCode;
    private int mCurrencyValueIndexBtc;
    private int mCurrencyValueIndexEth1;
    private String mCode1;
    private int mCurrencyValueIndexEth;
    private int mCurrencyValueIndexBtcSpinner;


    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        mCurrencyUpdateBroadcastReceiver = new CurrencyUpdateBroadcastReceiver();

        // Register the intent here
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTION_INTENT);
        registerReceiver(mCurrencyUpdateBroadcastReceiver, intentFilter);


        Bundle extras = getIntent().getExtras();

        // Make sure extra actually captures something from main
        if (extras == null) {

            return;
        }

        currency_code = extras.getString("CURRENCY_CODE");
        columnPosition = extras.getInt("COLUMN_NAME");

        // Instantiate the spinners
        spinner = findViewById(R.id.currency_name_spinner);
        curSpinner = findViewById(R.id.crypt_cur_spinner);


        // Load the spinner data from database
        loadSpinnerData();
        // Load the crypto spinner
        loadCryptoSpinner();


        // Spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the Card currency value
                curValue = findViewById(R.id.card_currency_value);
                // The Currency logo
                logoText = findViewById(R.id.card_currency_logo);

                // Get the cryto currency image
                cryptImage = findViewById(R.id.card_crypto_image);


                // Get the item that was selected or clicked
                mCode = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorSecondaryText, null));


                mDBHelper = new CryptoCurrencyDBHelper(getApplicationContext());

                // Check the state of the spinner
                if (!spinnerClicked) {

                    spinner.setSelection(columnPosition - 1);
                    spinnerChecker();

                }

                if (currency_code != null) {


                    if (currency_code.equals(ETH_CODE)) {

                        final double[] ethValue = {0};
                        String[] projection = {
                                CryptoContract.CurrencyEntry._ID,
                                CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE
                        };

                        Cursor cursor = getApplicationContext().getContentResolver().query(CryptoContract.CurrencyEntry.CONTENT_URI,
                                projection,
                                "cur_name = ?",
                                new String[]{mCode},
                                null);
                        assert cursor != null;
                        mCurrencyValueIndexEth1 = cursor.getColumnIndex(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE);

                        if (cursor.moveToFirst()) {
                            ethValue[0] = cursor.getDouble(mCurrencyValueIndexEth1);
                        }

                        curValue.setText(df.format(ethValue[0]));
                        logoText.setText(CurrencyHelper.getCurrencySymbol(mCode));
                        // Top image for CardView
                        cryptImage.setImageResource(R.drawable.ethereum);
                        cursor.close();
                    }

                    if (currency_code.equals(BTC_CODE)) {

                        final double[] btcValue = {0};
                        String[] projection = {
                                CryptoContract.CurrencyEntry._ID,
                                CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE
                        };

                        Cursor cursor = getApplicationContext().getContentResolver().query(CryptoContract.CurrencyEntry.CONTENT_URI,
                                projection,
                                "cur_name = ?",
                                new String[]{mCode},
                                null);
                        assert cursor != null;
                        mCurrencyValueIndexBtc = cursor.getColumnIndex(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE);

                        if (cursor.moveToFirst()) {
                            btcValue[0] = cursor.getDouble(mCurrencyValueIndexBtc);
                        }
                        curValue.setText(df.format(btcValue[0]));
                        logoText.setText(CurrencyHelper.getCurrencySymbol(mCode));
                        // Top image for CardView
                        cryptImage.setImageResource(R.drawable.bitcoin);
                        cursor.close();

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(columnPosition - 1);
            }
        });

        curSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @TargetApi(Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the Card currency value
                curValue = findViewById(R.id.card_currency_value);
                // The Currency logo
                logoText = findViewById(R.id.card_currency_logo);

                // Get the crypto currency image
                cryptImage = findViewById(R.id.card_crypto_image);

                // Get the spinner item that is currently selected
                mCode1 = spinner.getSelectedItem().toString();
                String cryptSelected = parent.getItemAtPosition(position).toString();


                mDBHelper = new CryptoCurrencyDBHelper(getApplicationContext());


                if (!curSpinnerClicked) {


                    if (currency_code != null) {
                        if (currency_code.equals(ETH_CODE)) {

                            curSpinner.setSelection(0);

                        }
                        if (currency_code.equals(BTC_CODE)) {

                            curSpinner.setSelection(1);

                        }


                    }

                    cryptoCurSpinnerChecker();

                }

                if (currency_code != null) {

                    if (cryptSelected.equals(getString(R.string.code_eth_text))) {

                        currency_code = "eth_value";
                    }

                    if (cryptSelected.equals(getString(R.string.code_btc_text))) {

                        currency_code = "btc_value";
                    }
                }


                if (currency_code != null) {

                    if (currency_code.equals(ETH_CODE)) {

                        final double[] ethValue = {0};
                        String[] projection = {
                                CryptoContract.CurrencyEntry._ID,
                                CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE
                        };

                        Cursor cursor = getApplicationContext().getContentResolver().query(CryptoContract.CurrencyEntry.CONTENT_URI,
                                projection,
                                "cur_name = ?",
                                new String[]{mCode1},
                                null);
                        assert cursor != null;
                        mCurrencyValueIndexEth1 = cursor.getColumnIndex(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE);

                        if (cursor.moveToFirst()) {
                            ethValue[0] = cursor.getDouble(mCurrencyValueIndexEth1);
                        }

                        curValue.setText(df.format(ethValue[0]));
                        // Top image for CardView
                        cryptImage.setImageResource(R.drawable.ethereum);
                        cursor.close();
                    }

                    if (currency_code.equals(BTC_CODE)) {

                        final double[] btcValue = {0};
                        String[] projection = {
                                CryptoContract.CurrencyEntry._ID,
                                CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE
                        };

                        Cursor cursor = getApplicationContext().getContentResolver().query(CryptoContract.CurrencyEntry.CONTENT_URI,
                                projection,
                                "cur_name = ?",
                                new String[]{mCode1},
                                null);
                        assert cursor != null;
                        mCurrencyValueIndexBtcSpinner = cursor.getColumnIndex(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE);

                        if (cursor.moveToFirst()) {
                            btcValue[0] = cursor.getDouble(mCurrencyValueIndexBtcSpinner);
                        }

                        curValue.setText(df.format(btcValue[0]));
                        // Top image for CardView
                        cryptImage.setImageResource(R.drawable.bitcoin);
                        cursor.close();

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Set up CardView to take user to conversion view
        CardView cardView = (CardView) findViewById(R.id.card_container);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent customConversionRate = new Intent(getApplicationContext(), ConversionActivity.class);
                startActivity(customConversionRate);

            }
        });

    }

    /**
     * Loads currency choice spinner
     */
    private void loadSpinnerData() {

        @SuppressLint("StaticFieldLeak") AsyncTask<CryptoCurrencyDBHelper, Void, List<String>> task = new AsyncTask<CryptoCurrencyDBHelper, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(CryptoCurrencyDBHelper... cryptoCurrencyDBHelpers) {
                CryptoCurrencyDBHelper cryptoCurrencyDBHelper = cryptoCurrencyDBHelpers[0];
                // Spinner dropdown elements
                List<String> codes = cryptoCurrencyDBHelper.getAllCurrencyCodeNames();
                return codes;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                // Create adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(),
                        android.R.layout.simple_spinner_item, strings);

                // Dropdown layer style
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Attach dataAdapter to spinner
                spinner.setAdapter(dataAdapter);
            }
        };


        mDBHelper = new CryptoCurrencyDBHelper(getApplicationContext());
        task.execute(mDBHelper);


    }

    private void loadCryptoSpinner() {


        // Create an adapter from the string array resource and use
        // android's inbuilt layout file simple_spinner_item
        // that represents the default spinner in the UI
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.crypto_array, android.R.layout.simple_spinner_item);

        // Set the layout to use for each dropdown item
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        curSpinner.setAdapter(adapter);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * Checks the original state
     * of the currency spinner setOnItemSelectedListener
     * event.
     */
    public void spinnerChecker() {

        if (!spinnerClicked) {

            spinnerClicked = true;

        }
    }

    /**
     * Checks the original state
     * of the crypto spinner setOnItemSelectedListener
     * event.
     */
    public void cryptoCurSpinnerChecker() {

        if (!curSpinnerClicked) {

            curSpinnerClicked = true;
        }
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate the menu options from the menu xml file
        //This add menu items to the app bar
        getMenuInflater().inflate(R.menu.network_available, menu);
        menu.findItem(R.id.menu_refresh).setVisible(false);
        this.menu = menu;

        if (isConnected()) {
            // Let user know the status of the device network
            menu.findItem(R.id.menu_network_available).setVisible(true);
            menu.findItem(R.id.menu_network_absent).setVisible(false);
        } else {
            // Let user know the status of the device network
            menu.findItem(R.id.menu_network_available).setVisible(false);
            menu.findItem(R.id.menu_network_absent).setVisible(true);
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

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(mCurrencyUpdateBroadcastReceiver, new IntentFilter(MY_INTENT));
        registerReceiver(mCurrencyUpdateBroadcastReceiver, new IntentFilter(CONNECTION_INTENT));

    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mCurrencyUpdateBroadcastReceiver);
    }

    // TODO: Correct the spelling
    private class CurrencyUpdateBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            // Set the network menu status
            if (Objects.equals(intent.getAction(), CONNECTION_INTENT)) {

                status = NetworkUtil.getConnectivityStatusString(context);
                online = (Objects.equals(status, "Wifi enabled") || Objects.equals(status, "Mobile data enabled"));
                supportInvalidateOptionsMenu();

            }


        }
    }


}
