package com.etechbusinesssolutions.android.cryptoapp.cryptservice;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * Created by george on 10/29/17.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {


    //TODO: Remove
    public static final String LOG_TAG = JobSchedulerService.class.getSimpleName();

    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(),
                    "JobService task running", Toast.LENGTH_SHORT)
                    .show();
            jobFinished((JobParameters) msg.obj, false);
            return true;
        }
    });


    @Override
    public boolean onStartJob(JobParameters params) {

        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        return false;
    }

//    @Override
//    public Loader<List<Currency>> onCreateLoader(int id, Bundle args) {
//        // Create a new loader for the given URL
//        Log.i(LOG_TAG, "TEST: onCreateLoader() called ...");
//
//        // Setup the baseURI
//        Uri baseUri = Uri.parse(CRYPTO_CURRENRY_URL);
//        Uri.Builder uriBuilder = baseUri.buildUpon();
//
//        uriBuilder.appendQueryParameter("fsyms", "ETH,BTC");
//        uriBuilder.appendQueryParameter("tsyms", "USD,EUR,NGN,RUB,CAD,JPY,GBP,AUD,INR,HKD,IDR,SGD,CHF,CNY,ZAR,THB,SAR,KRW,GHS,BRL");
//
//        Log.i(LOG_TAG, "TEST: uriBuilder String" + uriBuilder.toString());
//
//        return new CrytoCurrencyLoader(this, uriBuilder.toString());
//    }
//
//    @Override
//    public void onLoadFinished(Loader<List<Currency>> loader, List<Currency> data) {
//
//        //TODO: Load the information from CryptocrrencyQueryUtils into database using content provider
//        Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");
//        Log.i(LOG_TAG, "TEST: Database data insertion started ...");
//
//        // Create a ContentValues class object
//        ContentValues values = new ContentValues();
//
//        // Check if database table already present, if it exists
//        // then update current records instead of inserting.
//        Log.i(LOG_TAG, "Checking if database is present...");
//
//
//        boolean found = isTableExists();
//        Log.i(LOG_TAG, "Found: " + found + "...");
//
//        try {
//
//            if (found) {
//
//                try {
//                    for (Currency element : data) {
//                        values.put(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE, element.getcEthValue());
//                        values.put(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE, element.getcBtcValue());
//
//                        // Update database
//                        int mRowsUpdated = getContentResolver().update(
//                                CryptoContract.CurrencyEntry.CONTENT_URI,
//                                values,
//                                "_id = ?",
//                                new String[]{String.valueOf(element.getcId())}
//                        );
//
//                        // Log data insertion to catch any errors
//                        // TODO: Remove
//                        Log.v("HomeActivity_db_update", "New row ID " + mRowsUpdated + " Element id " + element.getcId());
//                        Log.i("Row_Entry " + mRowsUpdated, element.getcName() + " " + element.getcEthValue() + " " + element.getcBtcValue());
//
//                    }
//
//
//                    Log.i(LOG_TAG, "TEST: Database data update finished ...");
//
//                } catch (NullPointerException e) {
//
//                    Log.i(LOG_TAG, "Update error iterating over the data ... " + e);
//                } catch (IllegalFormatException f) {
//
//                    Log.i(LOG_TAG, "Update format error ... " + f);
//                }
//
//
//            } else {
//
//                try {
//
//                    for (Currency element : data) {
//
//                        values.put(CryptoContract.CurrencyEntry.COLUMN_CURRENCY_NAME, element.getcName());
//                        values.put(CryptoContract.CurrencyEntry.COLUMN_ETH_VALUE, element.getcEthValue());
//                        values.put(CryptoContract.CurrencyEntry.COLUMN_BTC_VALUE, element.getcBtcValue());
//
//                        // Insert data into SQLiteDatabase
//                        Uri uri = getContentResolver().insert(CryptoContract.CurrencyEntry.CONTENT_URI, values);
//                        // Log data insertion to catch any errors
//                        // TODO: Remove
//                        Log.v("HomeActivity", "Insert new row ID " + uri);
//                        Log.i("Row Entry ", element.getcName() + " " + element.getcEthValue() + " " + element.getcBtcValue());
//
//                    }
//
//                    //TODO: Remove
//                    Log.i(LOG_TAG, "TEST: Database data insertion finished ...");
//
//                } catch (NullPointerException e) {
//
//                    Log.i(LOG_TAG, "database insert error no data to iterate over ... " + e);
//                } catch (IllegalFormatException f) {
//
//                    Log.i(LOG_TAG, "Update format error ... " + f);
//                }
//
//
//            }
//        } catch (NullPointerException g) {
//
//            Log.i(LOG_TAG, "Database existent confirmation error " + g);
//
//        } catch (IllegalFormatException f) {
//
//            Log.i(LOG_TAG, "Update format error ... " + f);
//        }
//
//
//    }
//
//
//
//    @Override
//    public void onLoaderReset(Loader<List<Currency>> loader) {
//
//        //TODO: Remove
//        Log.i(LOG_TAG, "TEST: onLoadReset() called ...");
//        loaderManager.getLoader(DATABASE_LOADER_ID).cancelLoad();
//
//    }






}
