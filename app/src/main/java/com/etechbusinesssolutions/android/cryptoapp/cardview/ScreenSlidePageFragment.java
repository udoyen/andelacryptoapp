package com.etechbusinesssolutions.android.cryptoapp.cardview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etechbusinesssolutions.android.cryptoapp.R;

/**
 * Created by george on 10/20/17.
 */

public class ScreenSlidePageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.image_slider, container, false);

        return rootView;
    }
}
