package com.etechbusinesssolutions.android.cryptoapp;

/**
 * Created by george on 10/10/17.
 */

public class Currency {

    private String cName;
    private long cValue;
    private int cImageResourceId;

    Currency(String vName, long vValue, int vImageResourceId){
        cName = vName;
        cValue = vValue;
        cImageResourceId = vImageResourceId;
    }


    public int getcImageResourceId() {
        return cImageResourceId;
    }

    public float getcValue() {
        return cValue;
    }

    public String getcName() {
        return cName;
    }
}
