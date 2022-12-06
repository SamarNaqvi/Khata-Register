package com.example.khataregister;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Khata_Register_test.db";
    private static boolean tableExists = false;
    public dbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {


        String productTable = "CREATE TABLE productTable (Id TEXT PRIMARY KEY, " +
                "name TEXT," +
                "price REAL," + "quantity TEXT," + "date TEXT) ";

        String customerTable = "CREATE TABLE customerTable (" +
                "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name Text,"+
                "img Text,"+
                "receivable Text )";//+ "prodid TEXT, FOREIGN KEY (prodid) REFERENCES PRODUCT(Id)); ";

          String customerProducts = "" +
                  "CREATE TABLE ProductsBought (" +
                  "prodid TEXT, " +
                  "custId TEXT," +
                  "FOREIGN KEY (custId) REFERENCES Customer(Id),  " +
                  "FOREIGN KEY (prodid) REFERENCES PRODUCT(Id), " +
                  "PRIMARY KEY (prodid, custId)); ";


        String userTable = "CREATE TABLE userTable (email TEXT PRIMARY KEY, " +
                 "name Text,"+
                 "password TEXT,"+ " sales TEXT) ";


        sqLiteDatabase.execSQL(productTable);
        sqLiteDatabase.execSQL(customerTable);
        sqLiteDatabase.execSQL(customerProducts);
        sqLiteDatabase.execSQL(userTable);

        if(!tableExists)
        {
            tableExists=true;
        }

    }

    public void initProducts(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Product");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS CART");
        onCreate(sqLiteDatabase);
        tableExists=false;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,oldVersion,newVersion);
    }
}

