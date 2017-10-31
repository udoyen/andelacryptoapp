package com.etechbusinesssolutions.android.cryptoapp.cryptservice;

import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.etechbusinesssolutions.android.cryptoapp.CrytocurrencyQueryUtils;
import com.etechbusinesssolutions.android.cryptoapp.Currency;
import com.etechbusinesssolutions.android.cryptoapp.data.CryptoContract;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * Created by george on 10/30/17.
 */

public class MyQuerry extends AsyncTaskLoader {


    //TODO: Remove
    public static final String LOG_TAG = MyQuerry.class.getSimpleName();

    // URL for the currency data from cryptocompare
    private static final String CRYPTO_CURRENRY_URL = "https://min-api.cryptocompare.com/data/pricemulti";


    /**
     * Constant value for the earthquake loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int SERVICE_LOADER_ID = 4;

    public MyQuerry(Context context) {
        super(context);
    }


    public static String dataUrl() {

        // Setup the baseURI
        Uri baseUri = Uri.parse(CRYPTO_CURRENRY_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("fsyms", "ETH,BTC");
        uriBuilder.appendQueryParameter("tsyms", "USD,EUR,NGN,RUB,CAD,JPY,GBP,AUD,INR,HKD,IDR,SGD,CHF,CNY,ZAR,THB,SAR,KRW,GHS,BRL");

        Log.i(LOG_TAG, "TEST: uriBuilder String from MyQuerry" + uriBuilder.toString());

        String url = uriBuilder.toString();

        return url;

    }



    public static List<Currency> getData() {
        List<Currency> cryptoValues = new ArrayList<>();

        cryptoValues.addAll(CrytocurrencyQueryUtils.fetchCurrencyData(dataUrl()));

        return cryptoValues;
    }

    public boolean loadDatabase() {

        List<Currency> data;

        data = getData();

        ContentValues values = new ContentValues();



        try {
            for (Currency element : data) {
                values.put(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE, element.getcEthValue());
                values.put(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE, element.getcBtcValue());

                // Update database
                int mRowsUpdated = getContext().getContentResolver().update(
                        CryptoContract.CurrencyEntry.CONTENT_URI,
                        values,
                        "_id = ?",
                        new String[]{String.valueOf(element.getcId())}
                );

                // Log data insertion to catch any errors
                // TODO: Remove
                Log.v("MyQuerry_db_update", "New row ID " + mRowsUpdated + " Element id " + element.getcId());
                Log.i("MyQuerry_Row_Entry " + mRowsUpdated, element.getcName() + " " + element.getcEthValue() + " " + element.getcBtcValue());

            }


            Log.i(LOG_TAG, "TEST: Database data update finished in MyQuerry...");

        } catch (NullPointerException e) {

            Log.i(LOG_TAG, "Update error iterating over the data in MyQuerry ... " + e);
        } catch (IllegalFormatException f) {

            Log.i(LOG_TAG, "Update format error in MyQuerry... " + f);
        }

        return true;
    }

    @Override
    public Object loadInBackground() {

        return loadDatabase();
    }


}
