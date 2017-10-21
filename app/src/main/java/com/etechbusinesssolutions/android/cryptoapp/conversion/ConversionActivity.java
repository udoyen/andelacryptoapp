package com.etechbusinesssolutions.android.cryptoapp.conversion;

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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.etechbusinesssolutions.android.cryptoapp.R;
import com.etechbusinesssolutions.android.cryptoapp.data.CryptoCurrencyDBHelper;

import java.text.DecimalFormat;
import java.util.IllegalFormatException;
import java.util.List;

public class ConversionActivity extends AppCompatActivity {

    //TODO: Remove
    // For logging
    public static final String LOG_TAG = ConversionActivity.class.getSimpleName();

    /**
     * Value to convert
     */
    double userInput;

    /**
     * Create a spinner
     */
    Spinner spinner;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversion);

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
        spinner = (Spinner) findViewById(R.id.conversion_spinner);

        // Spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the item that was selected or clicked
                code = parent.getItemAtPosition(position).toString();
                //TODO: Remove
                Log.i(LOG_TAG, "Conversion Spinner selected code is: " + code);
                //TODO: Remove
                if (editBoxWithText) {
                    Log.i(LOG_TAG, "conversion() called from Spinner object");
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onRadioButtonClicked(View view) {
        Log.i(LOG_TAG, "Inside onRadioClicked() method ...");

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

                //Check if user has entered some text, and
                //whether conversion button was clicked.
                if (editBoxWithText && conversionBtn) {
                    //TODO: Remove
                    Log.i(LOG_TAG, "conversion() called from btc radio button click");
                    conversion();
                }

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


                 //Check if user has entered some text, and
                 //whether conversion button was clicked.
                if (editBoxWithText && conversionBtn) {
                    //TODO: Remove
                    Log.i(LOG_TAG, "conversion() called from btc radio button click");
                    conversion();
                }
                // TODO: Remove
                Log.i(LOG_TAG, "radioBtnState: " + radioBtnState);
                break;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void conversion() {

        // Calculation result
        double cal;

        // Create a radio button object
        RadioGroup rBtn = (RadioGroup) findViewById(R.id.radio_container);
        int checked = rBtn.getCheckedRadioButtonId();
        //TODO: Remove
        Log.i(LOG_TAG, "Is radio button checked: " + checked);

        // Grab the user input from EditText box
        EditText value1 = (EditText) findViewById(R.id.value_to_convert_box);
        //TODO: Remove
        Log.i(LOG_TAG, "EditTex grabbed ...");

        // Grab the TextViews to update
        TextView resultTextView = (TextView) findViewById(R.id.conversion_value);
        //TODO: Remove
        Log.i(LOG_TAG, "Result textview grabbed ...");

        //Checked to make sure user input isn't empty
        if (value1.getText().toString().trim().length() > 0) {

            // Grab user input.
            userInput = Double.parseDouble(value1.getText().toString().trim());
            //TODO: Remove
            Log.i(LOG_TAG, "Converting user input to double for user in conversion ...");

        } else {

            Toast.makeText(getApplicationContext(), "Please enter a value in the number box", Toast.LENGTH_LONG).show();

        }

        String result;

        // Check if user has selected a crypto currency type
        if (radioBtnState == null || checked == -1) {

            Toast.makeText(ConversionActivity.this,
                    "Please select a crypto currency type to convert to!",
                    Toast.LENGTH_LONG).show();

        } else {

            try {

                // Format to use for calculated conversion
                DecimalFormat df = new DecimalFormat("#,###.###");

                // Get the value of the currency from tha database
                double value = Double.parseDouble(mDBHelper.getCurrencyValue(code, radioBtnState));
                //TODO: Remove
                Log.i(LOG_TAG, "Database value: " + value);


                // Calculate the conversion rate
                cal = userInput / value;

                // Used to format the calculation output
                result = df.format(cal);

                //TODO: Remove
                Log.i(LOG_TAG, "Result of the conversion: " + result);

                //Set the Conversion Result TextView
                resultTextView.setText(result);


            } catch (NumberFormatException e) {

                //TODO: Remove
                Log.i(LOG_TAG, "Calculation error: " + e);
            } catch (IllegalFormatException g) {

                //TODO: Remove
                Log.i(LOG_TAG, "Error: " + g);
            }


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onConvertBtnClick(View view) {

        // Signal button click has occurred
        conversionBtn = true;

        // Call the conversion method
        //TODO: Remove
        Log.i(LOG_TAG, "onConvertBtnClick() called ...");
        conversion();
        //TODO: Remove
        Log.i(LOG_TAG, "conversion() called from Convert button click...");


    }
}
