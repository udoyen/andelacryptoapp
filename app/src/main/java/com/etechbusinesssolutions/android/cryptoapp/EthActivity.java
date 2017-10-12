package com.etechbusinesssolutions.android.cryptoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_currency);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new EthFragment())
                .commit();

    }
}
