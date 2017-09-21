package com.example.chris.bcconsole.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chris on 28/08/2017.
 */

public class DBController extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "db_binalbagan_console";

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


    private static final String TABLE_ROUTES = "TABLE_ROUTE";

    private static final String route_id = "route_id";
    private static final String route_lat = "route_lat";
    private static final String route_lng = "route_lng";
    private static final String route_datetimestamp = "route_timestamp";
    private static final String del_id = "del_id";


    public DBController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String CREATE_ROUTES_TABLE = "CREATE TABLE " + TABLE_ROUTES + "("
            + route_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + route_lat + " TEXT,"
            + route_lng + " TEXT,"
            + route_datetimestamp + " DATETIME DEFAULT (datetime('now','localtime')),"
            + del_id + " TEXT"
            + ")";

    private String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
            + prd_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + prd_name + " TEXT,"
            + prd_desc + " TEXT,"
            + prd_datestamp + " TEXT,"
            + prd_timestamp + " TEXT,"
            + prd_level + " INTEGER,"
            + prd_warning + " INTEGER,"
            + prd_optimal + " INTEGER,"
            + prd_image + " TEXT,"
            + prd_category + " TEXT,"
            + prd_status + " INTEGER"
            + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ROUTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);

        onCreate(db);
    }

    public boolean insertRoute(String lat,String lng,String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(route_lat,lat);
        contentValues.put(route_lng,lng);
        contentValues.put(del_id,id);

        long result = db.insert(TABLE_ROUTES,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllRoute(){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_ROUTES + " ORDER BY "+route_id+" ASC",null );
        return result;
    }

    public Integer deleteAllRouteData () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ROUTES, route_id+" >= ?",new String[] {"0"});
    }

    public Integer deleteSpecRouteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_ROUTES, route_id+" = ?",new String[] {id});
    }

}
