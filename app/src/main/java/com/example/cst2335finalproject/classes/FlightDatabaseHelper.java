package com.example.cst2335finalproject.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * This class is used to store and remove data from a database
 *
 * Author: Albert Pham
 * Date: 2019-04-12
 *
 */
public class FlightDatabaseHelper extends SQLiteOpenHelper {


    final static int VERSION_NUM = 1;

    final static String TABLE_NAME = "Flights";
    final static String COL_NAME = "Name";
    final static String COL_STATUS = "Status";
    final static String COL_LATITUDE = "Latitude";
    final static String COL_LONGITUDE = "Longitude";
    final static String COL_DIRECTION = "Direction";
    final static String COL_SPEED = "Speed";
    final static String COL_ALTITUDE = "Altitude";
    final static String COL_ARRIVINGTO = "Arrival";
    final static String COL_DEPARTINGFROM = "Departure";

    /**
     * constructor gets the context from the activity that calls it
     * @param ctx context
     */
    public FlightDatabaseHelper(Context ctx) {
        super(ctx,TABLE_NAME,null,VERSION_NUM);
    }

    /**
     * on open
     *
     * @param db database
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    /**
     * onCreate creates the table for the database
     *
     * @param db database
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_NAME + " TEXT," + COL_STATUS + " TEXT,"+ COL_LONGITUDE + " TEXT,"+ COL_DIRECTION + " TEXT,"+ COL_SPEED + " TEXT,"+ COL_ALTITUDE + " TEXT,"+ COL_ARRIVINGTO + " TEXT,"+ COL_DEPARTINGFROM + " TEXT,"+ COL_LATITUDE + " TEXT)");
    }

    /**
     * Drops the table if the table exists
     *
     * @param db db
     * @param oldVersion old version
     * @param newVersion new version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     *
     * Inserts a row into the database
     *
     * @param name name
     * @param status status
     * @param longitude longitude
     * @param latitude latitude
     * @param direction direction
     * @param speed speed
     * @param altitude altitude
     * @param arrivingTo arrivingTo
     * @param departingFrom departingFrom
     * @return
     */
    public boolean addData(String name, String status, String longitude, String latitude, String direction, String speed, String altitude, String arrivingTo, String departingFrom){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME, name);
        contentValues.put(COL_STATUS, status);
        contentValues.put(COL_LATITUDE, latitude);
        contentValues.put(COL_LONGITUDE, longitude);
        contentValues.put(COL_DIRECTION, direction);
        contentValues.put(COL_SPEED, speed);
        contentValues.put(COL_ALTITUDE, altitude);
        contentValues.put(COL_ARRIVINGTO, arrivingTo);
        contentValues.put(COL_DEPARTINGFROM, departingFrom);

        Log.d(TAG,"addData: Adding " + COL_NAME + " to " +TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Delete row from database
     *
     * @param id id
     * @return boolean
     */
    public boolean deleteData(String id){
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NAME,"ID=?", new String[]{id});

        if(result == -1){
            return false;
        } else {
            return true;
        }
    }


    /**
     * Gets the cursor data
     * @return cursor
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    /**
     * prints the cursor
     * @param c cursor
     */
    public void printCursor(Cursor c){
        Log.d(TAG, "printCursor(): VERSION NUMBER: "+ VERSION_NUM);
        int col = c.getColumnCount();
        Log.d(TAG, "printCursor(): COLUMN COUNT: "+ col);

        for(int i=0;i<col;i++){
            Log.d(TAG, "printCursor(): COLUMN NAME: "+ c.getColumnName(i));
        }


        Log.d(TAG, "printCursor(): ROW COUNT: "+ c.getCount());
        int row = 1;
        while(c.moveToNext()){
            Log.d(TAG, "printCursor(): ROW " + row++ + ": " + c.getString(0)+ " " + c.getString(1)+ " "+c.getString(2));
        }



    }

}
