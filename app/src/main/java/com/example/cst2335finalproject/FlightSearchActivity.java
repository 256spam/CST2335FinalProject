package com.example.cst2335finalproject;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * This class is the activity for searching flights
 *
 * Author: Albert Pham
 * Date: 2019-03-23
 */
public class FlightSearchActivity extends AppCompatActivity {

    Toolbar searchToolbar;


    /**
     * Sets the toolbar
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        searchToolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(searchToolbar);

    }


    /**
     * Inflate the search nav
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flight_tracker_search_nav, menu);
        return true;
    }

    /**
     * Shows a snackbar when user selects on the back icon
     *
     * @param item item on the menu selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.searchMenuBackItem:
                Snackbar snackbar = Snackbar.make(this.findViewById(R.id.searchToolBarId),"Go back?", Snackbar.LENGTH_LONG);
                snackbar.show();
                snackbar.setAction("YES", new View.OnClickListener(){

                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        }

                );

                break;


        }
        return true;
    }






}
