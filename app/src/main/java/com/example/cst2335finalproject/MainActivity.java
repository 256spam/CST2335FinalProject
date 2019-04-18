package com.example.cst2335finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    /**
     * When program starts and activity is created, establish toolbar and set it
     * @param savedInstanceState bundle of info from last saved instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

    }


    /**
     *  inflates given menu to create options menu
     * @param menu menu to inflate
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_nav, menu);
        return true;
    }

    /**
     * when one of the options in the toolbar is selected, move to related activity
     * @param item the item that was clicked
     * @return that it was selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent nextPage;

        switch(item.getItemId()){

            case R.id.dictionaryActivityMenuItem:
                nextPage = new Intent(MainActivity.this, DictionaryActivity.class);
                startActivity(nextPage);
                break;

            case R.id.newsfeedActivityMenuItem:
                nextPage = new Intent(MainActivity.this, NewsfeedActivity.class);
                startActivity(nextPage);
                break;

            case R.id.flightStatusTrackerMenuItem:
                nextPage = new Intent(MainActivity.this, FlightTrackerActivity.class);
                startActivity(nextPage);
                break;

            case R.id.newYorkTimesMenuItem:
                nextPage = new Intent(MainActivity.this, NYTActivity.class);
                startActivity(nextPage);
                break;
        }
        return true;
    }



}
