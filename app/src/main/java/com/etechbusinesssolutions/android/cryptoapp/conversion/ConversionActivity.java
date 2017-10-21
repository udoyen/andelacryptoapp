package com.etechbusinesssolutions.android.cryptoapp.conversion;

import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.etechbusinesssolutions.android.cryptoapp.R;
import com.etechbusinesssolutions.android.cryptoapp.data.CryptoCurrencyDBHelper;

import java.util.IllegalFormatException;
import java.util.List;

public class ConversionActivity extends AppCompatActivity {

    //TODO: Remove
    // For logging
    public static final String LOG_TAG = ConversionActivity.class.getSimpleName();
    //Value to convert
    double userInput;
    // Create a spinner
    Spinner spinner;
    // Radio button state
    private String radioBtnState;
    // Currency to convert
    private String code;
    //Create an instance of CryptoCurrencyDBHelper
    private CryptoCurrencyDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

        // Grab the TextViews to update when the user
        final TextView resultTextView = (TextView) findViewById(R.id.conversion_value);

        // Grab the user input from EditText box
        final EditText value = (EditText) findViewById(R.id.value_to_convert_box);


        // Convert user entered
        value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {
                    //TODO: Remove
                    Log.i(LOG_TAG, "onTextextChanged() called ...");
                    // Grab user input.
                    userInput = Double.parseDouble(value.getText().toString());

                    //Set the Conversion Result TextView
                    resultTextView.setText(conversion(userInput));

                } catch (NumberFormatException e) {

                    // Show user the error
                    Toast.makeText(ConversionActivity.this, "Please enter only numbers into box!", Toast.LENGTH_LONG).show();
                }


            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {

                try {
                    //TODO: Remove
                    Log.i(LOG_TAG, "afterTextChanged() called ...");

                    // Grab user input.
                    userInput = Double.parseDouble(value.getText().toString());

                    //Set the Conversion Result TextView
                    resultTextView.setText(conversion(userInput));

                } catch (NumberFormatException e) {

                    // Show user the error
                    Toast.makeText(ConversionActivity.this, "Please enter only numbers into box!", Toast.LENGTH_LONG).show();
                }

            }
        });


        // Setup the spinner data
        // Instantiate the spinner
        spinner = (Spinner) findViewById(R.id.conversion_spinner);

        // Spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the item that was selected or clicked
                code = parent.getItemAtPosition(position).toString();
                //TODO: Remove
                Log.i(LOG_TAG, "Conversion Spinner selected code is: " + code);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Load the spinner data from database
        loadSpinnerData();

    }


    private void loadSpinnerData() {

        //TODO: Remove
        // For logging
        Log.i(LOG_TAG, "loadSpinnerData() called...");

        mDBHelper = new CryptoCurrencyDBHelper(getApplicationContext());

        // Spinner dropdown elements
        List<String> codes = mDBHelper.getAllCurrencyCodeNames();


        // Create adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, codes);

        // Dropdown layer style
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach dataAdapter to spinner
        spinner.setAdapter(dataAdapter);

    }


    public void onRadioButtonClicked(View view) {

        final TextView currencyLogo = (TextView) findViewById(R.id.conversion_currency_logo);

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
                // TODO: Remove
                Log.i(LOG_TAG, "radioBtnState: " + radioBtnState);
                break;
            case R.id.radio_eth:
                if (checked)
                    // Set the value of the
                    // radioBtnState variable.
                    radioBtnState = "eth_value";
                // Set the currency logo text and make it visible
                currencyLogo.setText(R.string.conversion_radio_eth_btn);
                currencyLogo.setVisibility(View.VISIBLE);
                // TODO: Remove
                Log.i(LOG_TAG, "radioBtnState: " + radioBtnState);
                break;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String conversion(double userInput) {

        String result = null;

        // Check if user has selected a crypto currency type
        if (radioBtnState == null) {

            Toast.makeText(ConversionActivity.this,
                    "Please select a crypto currency type to convert to!",
                    Toast.LENGTH_LONG).show();

            return "0";

        } else {

            try {

                // Format to use for calculated conversion
                DecimalFormat df = new DecimalFormat("#.####");

                // Get the value of the currency from tha database
                double value = Double.parseDouble(mDBHelper.getCurrencyValue(code, radioBtnState));
                //TODO: Remove
                Log.i(LOG_TAG, "Database value: " + value);

                // Calculate the conversion rate
                double cal = userInput / value;

                result = String.valueOf(Double.valueOf(df.format(cal)));

                //TODO: Remove
                Log.i(LOG_TAG, "Result of the conversion: " + result);


            } catch (NumberFormatException e) {

                //TODO: Remove
                Log.i(LOG_TAG, "Calculation error: " + e);
            } catch (IllegalFormatException g) {

                //TODO: Remove
                Log.i(LOG_TAG, "Error: " + g);
            }

            return result;

        }

    }
}
