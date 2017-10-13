package com.etechbusinesssolutions.android.cryptoapp;

/**
 * Created by george on 10/10/17.
 */

public class Currency {

    private String cName;
    private long cEthValue;
    private long cBtcValue;


    Currency(String vName, long vBtcValue, long vEthValue){
        cName = vName;
        cEthValue = vEthValue;
        cBtcValue = vBtcValue;

    }


    public String getcName() {
        return cName;
    }

    public long getcEthValue() {
        return cEthValue;
    }

    public long getcBtcValue() {
        return cBtcValue;
    }
}
