package com.example.kurtiscc.upccatalog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurtiscc on 2/5/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    public static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "UPCDB.db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        TableUPC.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          TableUPC.onUpgrade(db, oldVersion, newVersion);
    }

    public void insert(UPCData upc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(TableUPC.COLUMN_UPC_CODE, upc.getUpccode());
        values.put(TableUPC.COLUMN_PRODUCT_NAME, upc.getProductname());
        values.put(TableUPC.COLUMN_IMAGE_URL, upc.getImage());
        // Insert row

        db.insert(TableUPC.TABLE_UPC, null, values);
        db.close();
    }

    public List<UPCData> getAllUPCData(){
        List<UPCData> myList = new ArrayList<UPCData>();
        // Select all participants
        String selectQuery = "SELECT * FROM " + TableUPC.TABLE_UPC;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery(selectQuery, null);

        // Loops through all rows and adds them to the list
        if( res.moveToFirst() ) // Checks if there are entries
        {
            do {
                UPCData upc = new UPCData();
                upc.setId(res.getInt(0));
                upc.setUpccode(res.getString(1));
                upc.setProductname(res.getString(2));
                upc.setImage(res.getString(3));

                myList.add(upc);
            } while (res.moveToNext()); // Will continue looping until there are no more entries

        }
        res.close();
        db.close();

        return myList;
    }

    public void delete(UPCData upc){
        SQLiteDatabase mydb = this.getWritableDatabase();

        mydb.delete(TableUPC.TABLE_UPC, TableUPC.COLUMN_ID + " = ?",
                new String[] { String.valueOf(upc.getId()) });

        mydb.close();
    }


}
