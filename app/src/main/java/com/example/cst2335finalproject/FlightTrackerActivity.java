package com.example.cst2335finalproject;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335finalproject.classes.Flight;
import com.example.cst2335finalproject.classes.FlightListAdapter;

/**
 * This class deals with the main activity of Flight Status Tracker
 *
 * Author: Albert Pham
 * Date: 2019-03-23
 *
 */
public class FlightTrackerActivity extends AppCompatActivity {

    Toolbar mainToolbar;
    ListView listView;
    FlightListAdapter adapter;


    /**
     * onCreate sets the listview and toolbar
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tracker);

        mainToolbar = findViewById(R.id.mainToolbar);

        listView = findViewById(R.id.flightListView);
        adapter = new FlightListAdapter(this);
        setSupportActionBar(mainToolbar);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.flight_details_dialog,null);
            builder.setView(v);

            Flight flight = adapter.getItem(position);


            TextView name = v.findViewById(R.id.flightDetailsName);
            TextView status = v.findViewById(R.id.flightDetailsStatus);
            TextView location = v.findViewById(R.id.flightDetailsLocation);
            TextView speed = v.findViewById(R.id.flightDetailsSpeed);
            TextView altitude = v.findViewById(R.id.flightDetailsAltitude);


            name.setText(flight.getFlightName());
            status.setText("Status: " + flight.getFlightStatus());
            location.setText("Location: " +flight.getFlightLocation());
            // TODO: THESE AREN'T SET YET
            speed.setText("Speed: Not set yet");
            altitude.setText("Altitude: ALTITUDE NOT SET YET");


            builder.setNegativeButton(R.string.flightDetailsRemoveButton, (dialog, which) -> {
                // TODO: Actually remove it from the list and from the database
                dialog.cancel();
            });


            builder.create().show();
        });


        // TODO: Temporary flights to show listview below

        Flight one = new Flight("F134", "Ottawa","LANDED");
        Flight two = new Flight("F135", "Montreal","FLYING");
        Flight three = new Flight("F136", "Toronto","CRASHING");


        adapter.addFlight(one);
        adapter.notifyDataSetChanged();
        adapter.addFlight(two);
        adapter.notifyDataSetChanged();
        adapter.addFlight(three);
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        // TODO: Temporary flights to show listview above

    }


    /**
     * Sets the menu for the toolbar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flight_tracker_nav, menu);
        return true;
    }

    /**
     *
     * React depending on which item on the nav menu was selected
     *
     * @param item item selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.searchMenuItem:
                Intent nextPage = new Intent(this, FlightSearchActivity.class);
                startActivity(nextPage);
                break;

            case R.id.listMenuItem:
                Toast.makeText(this, "Page under construction!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.aboutMenuItem:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.flight_info_dialog,null);
                builder.setView(v);

                builder.setNegativeButton(R.string.flightHelpOkButton, (dialog, which) -> dialog.cancel());

                builder.create().show();

                break;

        }
        return true;
    }


}
