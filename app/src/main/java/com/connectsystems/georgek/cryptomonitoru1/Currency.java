package com.connectsystems.georgek.cryptomonitoru1;



/**
 * Created by george on 10/10/17.
 */

public class Currency {

    private String cName;
    private double cEthValue;
    private double cBtcValue;
    private int cId;


    Currency(String vName, double vEthValue, double vBtcValue, int vId) {
        cName = vName;
        cEthValue = vEthValue;
        cBtcValue = vBtcValue;
        cId = vId;


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


    public int getcId() {
        return cId;
    }
}
