package com.example.cst2335finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.cst2335finalproject.classes.FlightDatabaseHelper;
import com.example.cst2335finalproject.classes.Flight;
import com.example.cst2335finalproject.classes.FlightDetailFragment;
import com.example.cst2335finalproject.classes.FlightListAdapter;



/**
 * This class deals with the main activity of Flight Status Tracker
 *
 * Author: Albert Pham
 * Date: 2019-03-23
 *
 */
public class FlightTrackerActivity extends AppCompatActivity {

    FlightDatabaseHelper databaseHelper;

    Toolbar mainToolbar;
    ListView listView;
    FlightListAdapter adapter;
    Button searchAirportButton;

    Context ctx;


    /**
     * onCreate sets the listview and toolbar
     *
     * @param savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_tracker);
        ctx = FlightTrackerActivity.this;
        mainToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        final boolean isTablet = findViewById(R.id.flightFragmentLocation) != null; //check if the FrameLayout is loaded

        databaseHelper = new FlightDatabaseHelper(getApplicationContext());

        listView = findViewById(R.id.flightListView);
        adapter = new FlightListAdapter(this);
        listView.setAdapter(adapter);

        searchAirportButton = findViewById(R.id.flightSearchAirportButton);
        searchAirportButton.setOnClickListener(v -> {

            Intent intent = new Intent(FlightTrackerActivity.this, FlightSearchActivity.class);
            startActivity(intent);

        });


        FlightDetailFragment dFragment = new FlightDetailFragment(); //add a DetailFragment
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Flight flight = adapter.getItem(position);


            Bundle dataToPass = new Bundle();

            dataToPass.putString("isSave","0");
            dataToPass.putLong("id",flight.getId());
            dataToPass.putString("name", flight.getFlightName());
            dataToPass.putString("status",flight.getFlightStatus());
            dataToPass.putString("speed", flight.getFlightSpeed());
            dataToPass.putString("latitude", flight.getFlightLatitude());
            dataToPass.putString("longitude", flight.getFlightLongitude());
            dataToPass.putString("direction", flight.getFlightDirection());
            dataToPass.putString("altitude", flight.getFlightAltitude());
            dataToPass.putString("arrivingTo",flight.getFlightArrivingTo());
            dataToPass.putString("departingFrom", flight.getFlightDepartingFrom());


            if (isTablet) {
                dFragment.removeLast();
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not

                FlightTrackerActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.flightFragmentLocation, dFragment).addToBackStack( "AnyName" ).commit();

//                FlightTrackerActivity.this.getSupportFragmentManager()
//                        .beginTransaction()
//                        .add(R.id.flightFragmentLocation, dFragment) //Add the fragment in FrameLayout
//                        .addToBackStack("AnyName") //make the back button undo the transaction
//                        .commit(); //actually load the fragment.
            } else {
                // If it's a phone
                System.out.println("ITS A PHONEEEEEEEEEEEEEEEEEEE");
                Intent intent = new Intent(FlightTrackerActivity.this, FlightDetailsActivity.class);
                intent.putExtra("isSave",0);
                intent.putExtra("id",flight.getId());
                intent.putExtra("name", flight.getFlightName());
                intent.putExtra("status",flight.getFlightStatus());
                intent.putExtra("speed", flight.getFlightSpeed());
                intent.putExtra("latitude", flight.getFlightLatitude());
                intent.putExtra("longitude", flight.getFlightLongitude());
                intent.putExtra("direction", flight.getFlightDirection());
                intent.putExtra("altitude", flight.getFlightAltitude());
                intent.putExtra("arrivingTo",flight.getFlightArrivingTo());
                intent.putExtra("departingFrom", flight.getFlightDepartingFrom());

                startActivity(intent);
            }
            onResume();

        });


        // Populates the list view
        populateList();

    }


    /**
     * When activity resumes, clears the listview and repopulate
     */
    @Override
    protected void onResume() {
        super.onResume();
        adapter = new FlightListAdapter(this);
        listView.setAdapter(adapter);
        populateList();
    }

    /**
     * When activity restarts clears the listview and repopulate
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        adapter = new FlightListAdapter(this);
        listView.setAdapter(adapter);
        populateList();
    }

    /**
     * Sets the menu for the toolbar
     *
     * @param menu
     * @return boolean
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
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage;
        switch(item.getItemId()){

            case R.id.dictionaryActivityMenuItem:
                nextPage = new Intent(FlightTrackerActivity.this, DictionaryActivity.class);
                startActivity(nextPage);
                break;

            case R.id.newsfeedActivityMenuItem:
                nextPage = new Intent(FlightTrackerActivity.this, NewsfeedActivity.class);
                startActivity(nextPage);
                break;

            case R.id.flightStatusTrackerMenuItem:
                nextPage = new Intent(FlightTrackerActivity.this, FlightTrackerActivity.class);
                startActivity(nextPage);
                break;

            case R.id.newYorkTimesMenuItem:
                nextPage = new Intent(FlightTrackerActivity.this, NYTActivity.class);
                startActivity(nextPage);
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

    /**
     * Populate the list view
     */
    public void populateList(){
        Cursor data = databaseHelper.getData();


        while(data.moveToNext()){

            Flight temp = new Flight();

            temp.setId(data.getLong(0));
            temp.setFlightArrivingTo(data.getString(7));
            temp.setFlightDepartingFrom(data.getString(8));
            temp.setFlightStatus(data.getString(2));
            temp.setFlightSpeed(data.getString(5));
            temp.setFlightLongitude(data.getString(3));
            temp.setFlightLatitude(data.getString(9));
            temp.setFlightDirection(data.getString(4));
            temp.setFlightAltitude(data.getString(6));
            temp.setFlightName(data.getString(1));
            adapter.addFlight(temp);
        }
        adapter.notifyDataSetChanged();

        databaseHelper.printCursor(databaseHelper.getData());
    }

    /**
     * When back button is pressed, pop the fragments
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FlightTrackerActivity.this.getSupportFragmentManager().popBackStack();
        finish();
    }

}
