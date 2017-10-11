package com.etechbusinesssolutions.android.cryptoapp.data;

import android.provider.BaseColumns;

/**
 * Created by george on 10/11/17.
 */

final class CryptoContract {

    // TO prevent accidental instantiation of this class,
    // give it an empty constructor
    private CryptoContract() {}

    /**
     * Inner class that defines constant values for the pets database table.
     * Each entry in the table represents a single pet.
     */
    static final class CurrencyEntry implements BaseColumns {

        // Name of the database table
        final static String TABLE_NAME = "eth";


        /**
         * Unique ID number for the eth (only for use in the database table).
         *
         * Type: INTEGER
         */
        final static String _ID = BaseColumns._ID;

        /**
         * Currency name
         *
         * Type: TEXT
         */
        final static String COLUMN_CURRENCY_NAME = "cur_name";

        /**
         * Eth value
         *
         * Type: BigDecimal
         */
        final static String COLUMN_ETH_VALUE = "eth_value";

        /**
         * Btc value
         *
         * Type: BigDecimal
         */
        final static String COLUMN_BTC_VALUE = "btc_value";


    }


}
