package com.etechbusinesssolutions.android.cryptoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;
import java.util.List;

/**
 * Created by george on 10/10/17.
 */

public class BtcFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Currency>>{

    // Used for logging
    //TODO: Remove
    public static final String LOG_TAG = BtcFragment.class.getName();

    /**
     * URL for BTC crypto currency compared to 20 major international currencies
     */
    private static final String ETH_CRYPTO_CURRENCY_RATES_URL = "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=USD,EUR,NGN,RUB,CAD,JPY,GBP,AUD,INR,HKD,IDR,SGD,CHF,CNY,ZAR,THB,SAR,KRW,GHS,BRL";

    /**
     * Constant value for the github loader ID. We can choose any integer
     * This really comes into play when you're using multiple loaders
     */
    private static final int GITHUB_LOADER_ID = 1;

    // Used to setup UrlQuery String
    URL url = null;

    public BtcFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Loader<List<Currency>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Currency>> loader, List<Currency> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Currency>> loader) {

    }
}
