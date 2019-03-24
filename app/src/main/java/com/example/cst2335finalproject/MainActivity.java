package com.example.cst2335finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button  menubutton1;
    Button  menubutton2;
    Button  menubutton3;
    Button  menubutton4;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        menubutton1 = findViewById(R.id.menuButton1);
        menubutton2 = findViewById(R.id.menuButton2);
        menubutton3 = findViewById(R.id.menuButton3);
        menubutton4 = findViewById(R.id.menuButton4);

        menubutton1.setOnClickListener( c -> {
            Intent nextPage = new Intent(MainActivity.this, DictionaryActivity.class);
            startActivity(nextPage);
        });

        menubutton2.setOnClickListener( c -> {
            Intent nextPage = new Intent(MainActivity.this, FlightTrackerActivity.class);
            startActivity(nextPage);
        });

        menubutton3.setOnClickListener( c -> {
            Intent nextPage = new Intent(MainActivity.this, NewsfeedActivity.class);
            startActivity(nextPage);
        });

        menubutton4.setOnClickListener( c -> {
            Intent nextPage = new Intent(MainActivity.this, NYTActivity.class);
            startActivity(nextPage);
        });


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_nav, menu);
        return true;
    }

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
