package com.example.kurtiscc.upccatalog;

import android.database.sqlite.SQLiteDatabase;
/**
 * Created by kurtiscc on 2/5/2015.
 */
public class TableUPC {
    public static final String TABLE_UPC = "upc";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_UPC_CODE = "upccode";
    public static final String COLUMN_PRODUCT_NAME = "productname";
    public static final String COLUMN_IMAGE_URL = "image";

    public static final String CREATE_TABLE = "CREATE TABLE upc ( " +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_UPC_CODE + " TEXT, " +
            COLUMN_PRODUCT_NAME + " TEXT, " +
            COLUMN_IMAGE_URL + " TEXT ) ";

    public static void onCreate(SQLiteDatabase database){
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_UPC);
        onCreate(database);
    }
    public static void dropTable(SQLiteDatabase database)
    {
        database.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE);
        onCreate(database);
    }



}
