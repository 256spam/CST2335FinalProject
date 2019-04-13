package com.example.cst2335finalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335finalproject.classes.FlightDatabaseHelper;

/**
 * This class handles the flight details
 *
* Author: Albert Pham
* Date: 2019-04-12
 *
 */
public class FlightDetailsActivity extends AppCompatActivity {
    FlightDatabaseHelper databaseHelper;

    private long id;
    private Button saveRemove;
    private String nameIntent,statusIntent,speedIntent,latitudeIntent,longitudeIntent,directionIntent,altitudeIntent,arivingtoIntent,departingfromIntent;

    /**
     * onCreate method initializing components
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flight_details);

        saveRemove = findViewById(R.id.saveRemoveButton);

        databaseHelper = new FlightDatabaseHelper(getApplicationContext());

        TextView name = findViewById(R.id.flightDetailsName);
        TextView status = findViewById(R.id.flightDetailsStatus);
        TextView latitude = findViewById(R.id.flightDetailsLatitude);
        TextView longitude = findViewById(R.id.flightDetailsLongitude);
        TextView direction = findViewById(R.id.flightDetailsDirection);
        TextView speed = findViewById(R.id.flightDetailsSpeed);
        TextView altitude = findViewById(R.id.flightDetailsAltitude);

        id = getIntent().getLongExtra("id",-1);
//        nameIntent = getIntent().getStringExtra("name");
//        statusIntent = "Status: " + getIntent().getStringExtra("status");
//        speedIntent = "Speed: " + getIntent().getStringExtra("speed");
//        latitudeIntent = "Latitude: " + getIntent().getStringExtra("latitude");
//        longitudeIntent = "Longitude: " + getIntent().getStringExtra("longitude");
//        directionIntent = "Direction: " + getIntent().getStringExtra("direction");
//        altitudeIntent = "Altitude: " + getIntent().getStringExtra("altitude");
//        arivingtoIntent = "Arriving to: " + getIntent().getStringExtra("arrivingTo");
//        departingfromIntent = "Departing from: " + getIntent().getStringExtra("departingFrom");

        nameIntent = getIntent().getStringExtra("name");
        statusIntent = getIntent().getStringExtra("status");
        speedIntent = getIntent().getStringExtra("speed");
        latitudeIntent = getIntent().getStringExtra("latitude");
        longitudeIntent = getIntent().getStringExtra("longitude");
        directionIntent = getIntent().getStringExtra("direction");
        altitudeIntent = getIntent().getStringExtra("altitude");
        arivingtoIntent = getIntent().getStringExtra("arrivingTo");
        departingfromIntent = getIntent().getStringExtra("departingFrom");


        name.setText(nameIntent);
        String statusString = "Status: " + statusIntent;
        String speedString = "Speed: " + speedIntent;
        String latitudeString = "Latitude: " + latitudeIntent;
        String longitudeString = "Longitude: " + longitudeIntent;
        String directionString = "Direction: " + directionIntent;
        String altitudeString = "Altitude: " + altitudeIntent;
        String arrivingToString = "Arriving to: " + arivingtoIntent;
        String departingFromString = "Departing from: " + departingfromIntent;


        status.setText(statusString);
        speed.setText(speedString);
        latitude.setText(latitudeString);
        longitude.setText(longitudeString);
        direction.setText(directionString);
        altitude.setText(altitudeString);

        int isSaved = getIntent().getIntExtra("isSave",-1);
        if(isSaved == 1){
            saveButton();
        } else if (isSaved == 0) {
            removeButton();
        } else {
            System.out.println("FlightDetailsActivity : ERROR ERROR");
        }


    }

    /**
     * Changes the button to a save button
     */
    public void saveButton(){
        saveRemove.setText(getResources().getText(R.string.flightSearchDetailSaveButton));
        saveRemove.setOnClickListener(v -> {

            boolean insertData = databaseHelper.addData(nameIntent,statusIntent,longitudeIntent,latitudeIntent,directionIntent,speedIntent,altitudeIntent,arivingtoIntent,departingfromIntent);

            if(insertData){
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Not saved",Toast.LENGTH_SHORT).show();
            }
            finish();

        });
    }

    /**
     * Changes the button to a remove button
     */
    public void removeButton(){
        saveRemove.setText(getResources().getText(R.string.flightDetailsRemoveButton));
        saveRemove.setOnClickListener(v -> {

            boolean deleteData = databaseHelper.deleteData(Long.toString(id));

            if(deleteData){
                Toast.makeText(getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Not deleted",Toast.LENGTH_SHORT).show();
            }
            finish();

        });

    }




}
