package com.example.chris.bcconsole.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.chris.bcconsole.classes.Products;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 28/08/2017.
 */

public class DBController extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db)binalbagan";

    private static final String TABLE_PRODUCTS = "TABLE_PRODUCTS";

    private static final String prd_id = "prd_id";
    private static final String prd_name = "prd_name";
    private static final String prd_desc = "prd_desc";
    private static final String prd_datestamp = "prd_datestamp";
    private static final String prd_timestamp = "prd_timestamp";
    private static final String prd_level = "prd_level";
    private static final String prd_optimal = "prd_optimal";
    private static final String prd_warning = "prd_warning";
    private static final String prd_image = "prd_image";
    private static final String prd_category = "prd_category";
    private static final String prd_status = "prd_status";


    public DBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
                + prd_id + " INTEGER PRIMARY KEY,"
                + prd_name + " TEXT,"
                + prd_desc + " TEXT,"
                + prd_datestamp + " TEXT,"
                + prd_timestamp + " TEXT,"
                + prd_level + " INTEGER,"
                + prd_warning + " INTEGER,"
                + prd_optimal + " INTEGER,"
                + prd_image + " TEXT,"
                + prd_category + " TEXT,"
                + prd_status + " INTEGER,"
                + ")AUTO_INCREMENT=20000000";
        Log.i("SQLite: ", CREATE_PRODUCTS_TABLE);

        db.execSQL(CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);


        onCreate(db);
    }

    public void addProduct(Products prod) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(prd_name, prod.getName());
        values.put(prd_desc, prod.getDesc());
        values.put(prd_datestamp, prod.getDatestamp());
        values.put(prd_timestamp, prod.getTimestamp());
        values.put(prd_level, prod.getLevel());
        values.put(prd_warning, prod.getWarning());
        values.put(prd_optimal, prod.getOptimal());
        values.put(prd_image, prod.getImage());
        values.put(prd_category, prod.getCategory());
        values.put(prd_status, prod.getStatus());


        db.insert(TABLE_PRODUCTS, null, values);
        db.close();
    }

    public List<Products> getAllProducts() {
        List<Products> productList = new ArrayList<Products>();
        // Select All Query

        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Products prod = new Products();
                prod.setId(Integer.parseInt(cursor.getString(0)));
                prod.setName(cursor.getString(1));
                prod.setDesc(cursor.getString(2));
                prod.setDatestamp(cursor.getString(3));
                prod.setTimestamp(cursor.getString(4));
                prod.setLevel(Integer.parseInt(cursor.getString(5)));
                prod.setWarning(Integer.parseInt(cursor.getString(6)));
                prod.setOptimal(Integer.parseInt(cursor.getString(7)));
                prod.setImage(cursor.getString(8));
                prod.setCategory(cursor.getString(9));
                prod.setStatus(cursor.getString(10));

                // Adding contact to list
                productList.add(prod);
            } while (cursor.moveToNext());
        }

        // return product list
        return productList;
    }
}
