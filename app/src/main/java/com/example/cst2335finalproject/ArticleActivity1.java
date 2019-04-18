/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * **/

package com.example.cst2335finalproject.NYTArticle;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.example.cst2335finalproject.R;


import android.content.Intent;
import android.widget.Button;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


public class ArticleActivity1 extends AppCompatActivity {

    String inputPosition,inputHeadline;
    NytDataBaseHelper dbInitiate;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.cst2335finalproject.R.layout.activity_main_new_york_times);
        Button saveButton = findViewById(com.example.cst2335finalproject.R.id.add);

        Intent previousPage = getIntent();
        inputPosition = previousPage.getStringExtra("inputPosition");
        inputHeadline = previousPage.getStringExtra("inputHeadline");


        System.out.println("url headline " +inputHeadline);
        System.out.println("url check " +inputPosition);


        WebView webView = findViewById(R.id.wvArticle1);
        webView.loadUrl(inputPosition);

        saveButton.setOnClickListener(c -> {
            // save word into database
            //get a database:
            dbInitiate = new NytDataBaseHelper(this);
            db = dbInitiate.getWritableDatabase();
            this.saveWord(db, inputPosition,inputHeadline);
        });
    }


        private void saveWord(SQLiteDatabase db, String a, String b ){
            // get word content
            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();
            //put string word content in the word_content column:
            newRowValues.put(dbInitiate.COL_Article, inputPosition);

//            System.out.println("savedWord here" +inputHeadline);

//            newRowValues.put(dbInitiate.COL_Url, inputPosition);
            //insert into the database:
            long newId = db.insert(dbInitiate.TABLE_NAME, null, newRowValues);
            System.out.println("kevinnim" +newId);

            String saveMsg = "";
            if(newId >-10){
                saveMsg = "Article Saved!.";
            }else{
                saveMsg = "Not Completed.";
            }

            // show toast bar if saved successful
            Toast.makeText(ArticleActivity1.this, saveMsg, Toast.LENGTH_LONG).show();
        }









}// end class