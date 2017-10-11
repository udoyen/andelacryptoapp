package com.etechbusinesssolutions.android.cryptoapp;

/**
 * Created by george on 10/10/17.
 */

class Currency {

    private String cName;
    private long cEthValue;
    private long cBtcValue;
    private int cImageResourceId;

    Currency(String vName, long vEthValue, long vBtcValue, int vImageResourceId){
        cName = vName;
        cEthValue = vEthValue;
        cBtcValue = vBtcValue;
        cImageResourceId = vImageResourceId;
    }


    public int getcImageResourceId() {
        return cImageResourceId;
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
