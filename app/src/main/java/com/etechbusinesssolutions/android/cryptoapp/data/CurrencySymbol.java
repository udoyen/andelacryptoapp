package com.etechbusinesssolutions.android.cryptoapp.data;

/**
 * Created by george on 10/17/17.
 * Class used to get the currency symbols for each Fragment
 */

public class CurrencySymbol {

    public static String getCurrencySymbol(String cur) {

        String symbol = null;

        switch (cur) {
            case "USD":
            case "AUD":
            case "CAD":
            case "HKD":
            case "SGD":
                symbol = "$";
                break;
            case "JPY":
            case "CNY":
                symbol = "¥";
                break;
            case "RUB":
                symbol = "p.";
                break;
            case "NGN":
                symbol = "₦";
                break;
            case "GBP":
                symbol = "£";
                break;
            case "INR":
                symbol = "₹";
                break;
            case "THB":
                symbol = "฿";
                break;
            case "SAR":
                symbol = "ر.س";
                break;
            case "KRW":
                symbol = "₩";
                break;
            case "BRL":
                symbol = "R$";
                break;
            case "EUR":
                symbol = "€";
                break;
            case "IDR":
                symbol = "Rp";
                break;
            case "ZAR":
                symbol = "R";
                break;
            case "CHF":
                symbol = "₣";
                break;
            case "GHS":
                symbol =  "₵";
                break;

        }

        return symbol;
    }
}
