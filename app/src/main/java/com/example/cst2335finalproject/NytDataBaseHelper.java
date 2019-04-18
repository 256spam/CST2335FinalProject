package com.example.cst2335finalproject.NYTArticle;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class NytDataBaseHelper extends SQLiteOpenHelper {


    private SQLiteDatabase database;
    //db name
    public static final String DATABASE_NAME = "NytDB";
    //db version
    public static final int VERSION_NUM = 1;
   //db table name
    public static final String TABLE_NAME = "Nyt_Table";
    // db colum names
    public static final String COL_ID = "id";
    public static final String COL_Article = "article";
    public static final String COL_Url = "url";



    public NytDataBaseHelper(Activity ctx){
        super(ctx, DATABASE_NAME,null,VERSION_NUM);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.i("Database onCreate:", "trying to create the database .");
        //Make sure you put spaces between SQL statements and Java strings:
        String sql = "CREATE TABLE " + TABLE_NAME + "( "
                + COL_ID  + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_Article + " TEXT)"
                + COL_Url + " TEXT)"
                + "";


        Log.i("Database onCreate:", "the query is: " + sql);
        db.execSQL(sql);
        // log messages
        Log.i("Database onCreate:", "db created.");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database upgrade:", " Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.i("Database downgrade", " Old version:" + oldVersion + " newVersion:"+newVersion);

        //Delete the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create a new table:
        onCreate(db);
    }
}