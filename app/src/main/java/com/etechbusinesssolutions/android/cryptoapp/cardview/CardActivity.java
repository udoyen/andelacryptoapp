package com.etechbusinesssolutions.android.cryptoapp.cardview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.etechbusinesssolutions.android.cryptoapp.R;
import com.etechbusinesssolutions.android.cryptoapp.conversion.ConversionActivity;
import com.etechbusinesssolutions.android.cryptoapp.data.CryptoCurrencyDBHelper;
import com.etechbusinesssolutions.android.cryptoapp.data.CurrencyHelper;

import java.text.DecimalFormat;
import java.util.List;

public class CardActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //TODO: Remove
    // For logging
    public static final String LOG_TAG = CardActivity.class.getSimpleName();

    //Create an instance of CryptoCurrencyDBHelper
    private CryptoCurrencyDBHelper mDBHelper;


    // Create a spinner
    Spinner spinner;

    // String to identify intent source
    private static final String ETH_CODE = "eth_value";
    private static final String BTC_CODE = "btc_value";

    String currency_code;

    //PagerImages
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * Format to use for displayed currencies
     */
    DecimalFormat df = new DecimalFormat("#,###.###");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Bundle extras = getIntent().getExtras();

        // Make sure extra actually captures something from main
        if(extras == null) {
            Log.i(LOG_TAG, "Extras was null ...");
            return;
        }

        currency_code = extras.getString("CURRENCY_CODE");

        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        View inflatedLayout = inflater.inflate(R.layout.image_slider, null, false);


        // Instantiate the spinner
        spinner = (Spinner) findViewById(R.id.currency_name_spinner);


        // Spinner listener
        spinner.setOnItemSelectedListener(this);

        // Load the spinner data from database
        loadSpinnerData();


        // Set up CardView to take user to conversion view
        CardView cardView = (CardView) findViewById(R.id.card_container);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Remove
                Log.i(LOG_TAG, "CardView onCLick event fired ...");
                Toast.makeText(CardActivity.this, "Clicked on CardView", Toast.LENGTH_LONG).show();

                Intent customConversionRate = new Intent(getApplicationContext(), ConversionActivity.class);
                startActivity(customConversionRate);

            }
        });



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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // Get the Card currency value
        TextView curValue = (TextView) findViewById(R.id.card_currency_value);
        // The Currency logo
        TextView logoText = (TextView) findViewById(R.id.card_currency_logo);

        // Get the item that was selected or clicked
        String code = parent.getItemAtPosition(position).toString();
        //TODO: Remove
        Log.i(LOG_TAG, "Spinner selected code is: " + code);
        Log.i(LOG_TAG, "currency_code is: " + currency_code + " and code is " + code);


        mDBHelper = new CryptoCurrencyDBHelper(getApplicationContext());


        if (currency_code != null) {
            //TODO: Remove
            Log.i(LOG_TAG, "Inside currency_code if block ...");

            if (currency_code.equals(ETH_CODE)) {

                //TODO: Remove
                Log.i(LOG_TAG, "Calling value from database in eth if block ...");

                String value = mDBHelper.getCurrencyValue(code, ETH_CODE);
                double num = Double.parseDouble(value);
                curValue.setText(df.format(num));
                logoText.setText(CurrencyHelper.getCurrencySymbol(code));

            }
            if (currency_code.equals(BTC_CODE)) {

                //TODO: Remove
                Log.i(LOG_TAG, "Calling value from database in eth if block ...");

                String value = mDBHelper.getCurrencyValue(code, BTC_CODE);
                double num = Double.parseDouble(value);
                curValue.setText(df.format(num));
                logoText.setText(CurrencyHelper.getCurrencySymbol(code));

            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

        // TODO: What should happen when nothing is selected?

    }
}
