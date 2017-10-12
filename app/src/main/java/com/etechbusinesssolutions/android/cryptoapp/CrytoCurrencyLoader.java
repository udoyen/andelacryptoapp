package com.etechbusinesssolutions.android.cryptoapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by george on 10/10/17.
 */

public class CrytoCurrencyLoader extends AsyncTaskLoader<List<Currency>> {

    private String cUrl;

    public CrytoCurrencyLoader (Context context, String url) {

        super(context);
        cUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();

    }

    @Override
    public List<Currency> loadInBackground() {

        //Create a currency list and return it here
        // Don't perform the request if there are no URLs, or the first URL is null
        if (cUrl == null) {
             return null;
        }

        return CrytocurrencyQueryUtils.fetchCurrencyData(cUrl);
    }
}
