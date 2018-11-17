package com.connectsystems.georgek.cryptomonitoru1;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by george on 10/10/17.
 */

public class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[]{"BTC", "ETH"};
    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context){

        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new BtcFragment();
        } else {
            return new EthFragment();
        }
    }

    @Override
    public int getCount() {

        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];
    }
}
