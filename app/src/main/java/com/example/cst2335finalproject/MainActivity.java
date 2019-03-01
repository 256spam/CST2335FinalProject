package com.example.cst2335finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button  menubutton1;
    Button  menubutton2;
    Button  menubutton3;
    Button  menubutton4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
