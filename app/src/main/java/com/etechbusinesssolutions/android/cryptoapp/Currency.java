package com.etechbusinesssolutions.android.cryptoapp;

/**
 * Created by george on 10/10/17.
 */

public class Currency {

    private String cName;
    private double cEthValue;
    private double cBtcValue;


    Currency(String vName, double vEthValue, double vBtcValue) {
        cName = vName;
        cEthValue = vEthValue;
        cBtcValue = vBtcValue;


    }


    public String getcName() {
        return cName;
    }

    public double getcEthValue() {
        return cEthValue;
    }

    public double getcBtcValue() {
        return cBtcValue;
    }


}
