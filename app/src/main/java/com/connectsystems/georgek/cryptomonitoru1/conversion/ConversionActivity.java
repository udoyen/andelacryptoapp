package com.connectsystems.georgek.cryptomonitoru1.conversion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.connectsystems.georgek.cryptomonitoru1.R;
import com.connectsystems.georgek.cryptomonitoru1.cardview.CardActivity;
import com.connectsystems.georgek.cryptomonitoru1.cryptoservice.JobSchedulerService;
import com.connectsystems.georgek.cryptomonitoru1.data.CryptoCurrencyDBHelper;
import com.connectsystems.georgek.cryptomonitoru1.networkutil.NetworkUtil;

import java.text.DecimalFormat;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Objects;

public class ConversionActivity extends AppCompatActivity {

    private static final String CONNECTION_INTENT = "android.net.conn.CONNECTIVITY_CHANGE";
    /**
     * JobScheduler Job ID
     */
    private static final int JOB_ID = 1;
    /**
     * Value to convert
     */
    double userInput;
    /**
     * Create a spinner
     */
    Spinner spinner;
    /**
     * Used to determine if user has entered a number
     * default is false.
     */
    boolean editBoxWithText = false;
    /**
     * Used to check if conversion button
     * has been clicked
     */
    boolean conversionBtn = false;
    /**
     * Create EditText object
     */
    EditText value1;
    /**
     * Create an instance of the JobScheduler class
     */
    JobScheduler mJobScheduler;
    /**
     * Used to set the menu items
     */
    Menu menu;
    /**
     * Used to check network status
     */
    String status;
    /**
     * Used to check network status
     */
    boolean online;

    /**
     * Radio button state
     */
    private String radioBtnState;
    /**
     * Currency to convert
     */
    private String code;
    //Create an instance of CryptoCurrencyDBHelper
    private CryptoCurrencyDBHelper mDBHelper;
    MenuItem refreshMenuItem;

    private CurrencyUpdateBroadcastReceiver mCurrencyUpdateBroadcastReceiver;
    private double mValue;
    private double mCal;
    private String mResult;
    private DecimalFormat mDf;
    private TextView mResultTextView;


    //    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        mCurrencyUpdateBroadcastReceiver = new CurrencyUpdateBroadcastReceiver();


        // Register the intent here
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTION_INTENT);
        registerReceiver(mCurrencyUpdateBroadcastReceiver, intentFilter);

        value1 = (EditText) findViewById(R.id.value_to_convert_box);

        value1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // Set this to true on user entering
                // a number
                editBoxWithText = true;

            }

            @Override
            public void afterTextChanged(Editable s) {

                // Set this to true on user entering
                // a number
                editBoxWithText = true;

            }
        });


        // Setup the spinner data
        // Instantiate the spinner
        spinner = findViewById(R.id.conversion_spinner);

        // Spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the item that was selected or clicked
                code = parent.getItemAtPosition(position).toString();

                if (editBoxWithText) {

                    conversion();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        // Load the spinner data from database
        loadSpinnerData();

    }


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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRadioButtonClicked(View view) {


        final TextView currencyLogo = findViewById(R.id.conversion_currency_logo);

        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_btc:
                if (checked)
                    // Set the value of the
                    // radioBtnState variable.
                    radioBtnState = "btc_value";
                // Set the currency logo text and make it visible
                currencyLogo.setText(R.string.conversion_radio_btc_btn);
                currencyLogo.setVisibility(View.VISIBLE);

                //Check if user has entered some text, and
                //whether conversion button was clicked.
                if (editBoxWithText && conversionBtn) {

                    conversion();
                }

                break;
            case R.id.radio_eth:
                if (checked)
                    // Set the value of the
                    // radioBtnState variable.
                    radioBtnState = "eth_value";
                // Set the currency logo text and make it visible
                currencyLogo.setText(R.string.conversion_radio_eth_btn);
                currencyLogo.setVisibility(View.VISIBLE);


                //Check if user has entered some text, and
                //whether conversion button was clicked.
                if (editBoxWithText && conversionBtn) {

                    conversion();
                }

                break;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void conversion() {

        // Calculation result

        // Create a radio button object
        RadioGroup rBtn = (RadioGroup) findViewById(R.id.radio_container);
        int checked = rBtn.getCheckedRadioButtonId();


        // Grab the user input from EditText box
        EditText value1 = (EditText) findViewById(R.id.value_to_convert_box);


        // Grab the TextViews to update
        mResultTextView = (TextView) findViewById(R.id.conversion_value);


        //Checked to make sure user input isn't empty
        if (value1.getText().toString().trim().length() > 0) {

            // Grab user input.
            userInput = Double.parseDouble(value1.getText().toString().trim());


        } else {

            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a value in the number box", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.toast_custom_look));
            toast.show();

        }

        // Check if user has selected a crypto currency type
        if (radioBtnState == null || checked == -1) {

            Toast toast = Toast.makeText(ConversionActivity.this,
                    "Please select a crypto currency type to convert to!",
                    Toast.LENGTH_LONG);
            View view = toast.getView();
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.toast_custom_look));
            toast.show();


        } else {

            try {

                // Format to use for calculated conversion
                mDf = new DecimalFormat("#,###.###");


                @SuppressLint("StaticFieldLeak") AsyncTask<CryptoCurrencyDBHelper, Void, String> task = new AsyncTask<CryptoCurrencyDBHelper, Void, String>() {
                    @Override
                    protected String doInBackground(CryptoCurrencyDBHelper... cryptoCurrencyDBHelpers) {
                        CryptoCurrencyDBHelper cryptoCurrencyDBHelper = cryptoCurrencyDBHelpers[0];
                        String cValue = cryptoCurrencyDBHelper.getCurrencyValue(code, radioBtnState);
                        return cValue;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        // Get the value of the currency from tha database
                        mValue = Double.parseDouble(s);
                        // Calculate the conversion rate
                        mCal = userInput / mValue;

                        // Used to format the calculation output
                        mResult = mDf.format(mCal);

                        //Set the Conversion Result TextView
                        mResultTextView.setText(mResult);

                    }
                };


                CryptoCurrencyDBHelper cryptoCurrencyDBHelper = new CryptoCurrencyDBHelper(getApplicationContext());

                task.execute(cryptoCurrencyDBHelper);


            } catch (NumberFormatException e) {

                //info: for debudding
                Log.i("Error", "Calculation error: " + e);
            } catch (IllegalFormatException g) {

                //info: Remove
                Log.i("Error", "Error: " + g);
            }


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onConvertBtnClick(View view) {

        // Signal button click has occurred
        conversionBtn = true;

        conversion();


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
    public void onResume() {
        super.onResume();
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
