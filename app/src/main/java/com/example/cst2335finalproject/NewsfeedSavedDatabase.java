package com.example.cst2335finalproject;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * class is used to create a new database for the newsfeed activity.
 */
public class NewsfeedSavedDatabase extends SQLiteOpenHelper  {
    public static final String DATABASE_NAME = "NewsfeedItemsSaved";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "Newsfeed";
    public static final String COL_ID = "_id";
    public static final String COL_Title= "TITLE";
    public static final String COL_TEXT= "TEXT";
    public static final String COL_URL = "URL";

    public NewsfeedSavedDatabase(Activity ctx) {
        //The factory parameter should be null, unless you know a lot about Database Memory management
        super(ctx, DATABASE_NAME, null, VERSION_NUM );
    }

    public void onCreate(SQLiteDatabase db)
    {
        //Make sure you put spaces between SQL statements and Java strings:
        db.execSQL("CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_Title + " TEXT, "
                + COL_TEXT + " TEXT, "
                + COL_URL + " TEXT)");
    }


    /**
     * delete old database and add new one if version number is upgraded
     * @param db - database to be added to new one
     * @param oldVersion - version of old database
     * @param newVersion - version of the new databe
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    /**
     * delete new database and add old database if needed
     * @param db - dabase to be added
     * @param oldVersion - version number of old database
     * @param newVersion - version number of new database
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", "Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}
