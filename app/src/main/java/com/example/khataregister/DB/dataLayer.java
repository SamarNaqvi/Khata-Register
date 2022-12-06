package com.example.khataregister.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class dataLayer implements sqlInterface {


    private Context context;

    public dataLayer(Context ctx) {
        context = ctx;
    }


    @Override
    public void save(Hashtable<String, String> attributes, String tableName) {
        dbHelper dbHelper = new dbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        ContentValues content = new ContentValues();
        Enumeration<String> keys = attributes.keys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            content.put(key,attributes.get(key));
        }

        db.insert(tableName,null,content);

//        String id = tableName=="Product"? "id":"prodid";
//        String [] arguments = new String[1];
//        arguments[0] = attributes.get(id);
//        Hashtable obj = loadById(arguments[0].toUpperCase(),tableName);
//
//        if (obj.get(id) != null && obj.get(id).equals(arguments[0])){
//            db.update(tableName,content,"PRODID = ?",arguments);
//        }
//        else{
//            db.insert(tableName,null,content);
//        }

    }

    @Override
    public void save(ArrayList<Hashtable<String, String>> objects, String tableName) {
        for(Hashtable<String,String> obj : objects){
            save(obj, tableName);
        }
    }

    @Override
    public ArrayList<Hashtable<String, String>> loadALL(String tableName) {
        dbHelper dbHelper = new dbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM "+tableName + "";
        Cursor cursor = db.rawQuery(query,null);



        ArrayList<Hashtable<String,String>> objects = new ArrayList<Hashtable<String, String>>();
        while(cursor.moveToNext()){
            Hashtable<String,String> obj = new Hashtable<String, String>();
            String [] columns = cursor.getColumnNames();

            for(String col : columns){
                int columnIndex = cursor.getColumnIndex(col);
                String val = cursor.getString(columnIndex);
                obj.put(col.toLowerCase(),val);
            }
            objects.add(obj);
        }

        return objects.size()==0?null:objects;

    }



    @Override
    public Hashtable<String, String> loadById(String id, String tableName) {
        dbHelper dbHelper = new dbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String idName = tableName=="Product"? "Id": "PRODID";
        String query = "SELECT * FROM "+ tableName+" WHERE "+idName+" = ?";
        String[] arguments = new String[1];
        arguments[0] = id;
        Cursor cursor = db.rawQuery(query,arguments);


        Hashtable<String,String> obj = new Hashtable<String, String>();
        while(cursor.moveToNext()){
            String [] columns = cursor.getColumnNames();
            for(String col : columns){
                int columnIndex = cursor.getColumnIndex(col);
                obj.put(col.toLowerCase(),cursor.getString(columnIndex));
            }
        }

        return obj;
    }

    @Override
    public Integer getCustomerId() {
        dbHelper dbHelper = new dbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM "+ "customerTable"+" WHERE "+"receivable"+" = ?";
        String[] arguments = new String[1];
        arguments[0] = "no receivable";
        Cursor cursor = db.rawQuery(query,arguments);


        Hashtable<String,String> obj = new Hashtable<String, String>();
        while(cursor.moveToNext()){
            String [] columns = cursor.getColumnNames();
            for(String col : columns){
                int columnIndex = cursor.getColumnIndex(col);
                obj.put(col.toLowerCase(),cursor.getString(columnIndex));
            }
        }

        return Integer.parseInt(obj.get("id"));
    }

    public void truncateDb()
    {
        dbHelper dbHelper = new dbHelper(context);
        SQLiteDatabase db  = dbHelper.getWritableDatabase();

        db.execSQL("delete from userTable");
        db.execSQL("delete from productTable");
        db.execSQL("delete from customerTable");
        db.execSQL("delete from ProductsBought");

    }

    public void updateBalance(Hashtable<String, String> customer) {
        dbHelper dbHelper = new dbHelper(context);
        SQLiteDatabase db  = dbHelper.getWritableDatabase();

        String[] arg = new String[1];
        arg[0] = customer.get("id");

        ContentValues content = new ContentValues();
        content.put("id",customer.get("id"));
        content.put("name",customer.get("name"));
        content.put("img",customer.get("img"));
        content.put("receivable",customer.get("receivable"));

        db.update("customerTable", content, "id = ?", arg);
    }
}
