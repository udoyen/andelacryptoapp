package com.connectsystems.georgek.cryptomonitoru1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class EthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_currency);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.viewpager, new EthFragment())
                .commit();

    }
}
