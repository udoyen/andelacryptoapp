package com.connectsystems.georgek.cryptomonitoru1.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.connectsystems.georgek.cryptomonitoru1.data.CryptoContract.CurrencyEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by george on 10/11/17.
 */

public class CryptoCurrencyDBHelper extends SQLiteOpenHelper {


    // Name of database
    private static final String DATABASE_NAME = "currency.db";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CurrencyEntry.TABLE_NAME;


    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 3;

    /**
     * Constructs a new instance of {@link CryptoCurrencyDBHelper}.
     *
     * @param context of the app
     */
    public CryptoCurrencyDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {


        // Create a String that contains the SQl statement to create the pets table
        String SQL_CREATE_CURRENCY_TABLE = "CREATE TABLE " + CurrencyEntry.TABLE_NAME + " ("
                + CurrencyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CurrencyEntry.COLUMN_CURRENCY_NAME + " TEXT NOT NULL, "
                + CurrencyEntry.COLUMN_ETH_VALUE + " REAL NOT NULL DEFAULT 0, "
                + CurrencyEntry.COLUMN_BTC_VALUE + " REAL NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_CURRENCY_TABLE);
        //db.close();

    }


    /**
     * This is called when the databse must be updated
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
        // This is only a cache
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
        //db.close();

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /**
     * @return Code names of currencies for Spinner
     */
    public List<String> getAllCurrencyCodeNames() {

        List<String> codes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + CurrencyEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Add the currency code to the list
        if (cursor.moveToFirst()) {
            do {
                codes.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // CLoser connection
        cursor.close();
        db.close();

        return codes;
    }

    /**
     * @param cur_name      The currency code
     * @param crypt_version The crypto currency version
     * @return Return string value from database
     */
    public String getCurrencyValue(String cur_name, String crypt_version) {


        // Select value query
        String selectValueQuery = "SELECT " + crypt_version + " FROM "
                + CurrencyEntry.TABLE_NAME
                + " WHERE cur_name=" + "'" + cur_name + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectValueQuery, null);

        String value = null;

        //Get the database value
        if (cursor.moveToFirst()) {

            value = String.valueOf(cursor.getDouble(cursor.getColumnIndex(crypt_version)));
        }

        // Closer connection
        cursor.close();
        db.close();

        return value;
    }

}
