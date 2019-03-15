package com.example.cst2335finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class DictionaryActivity extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        toolbar = findViewById(R.id.dictionary_toolbar);
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dictionary_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent nextPage;

        switch(item.getItemId()){

            case R.id.dictionarySearch:

                break;

            case R.id.dictionaryLocalItem:

                break;

            case R.id.dictionarySaveItem:

                break;


            case R.id.dictionaryDeleteItem:

                break;

            case R.id.dictionaryHelpItem:
                //TODO make this display a dialogue instead of a toast with proper instructions
                CharSequence text = "Author: Peter Best, Version Number: 0.1, Instructions: TODO";

                Toast toast2 = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast2.show();
                break;

        }
        return true;
    }
}
