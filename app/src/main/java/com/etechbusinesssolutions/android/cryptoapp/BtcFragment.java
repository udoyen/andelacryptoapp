package com.etechbusinesssolutions.android.cryptoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.URL;

/**
 * Created by george on 10/10/17.
 */

public class BtcFragment extends Fragment {

    // Used for logging
    //TODO: Remove
    public static final String LOG_TAG = BtcFragment.class.getName();


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

}
