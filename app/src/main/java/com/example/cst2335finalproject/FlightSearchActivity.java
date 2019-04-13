package com.example.cst2335finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cst2335finalproject.classes.Flight;
import com.example.cst2335finalproject.classes.FlightDetailFragment;
import com.example.cst2335finalproject.classes.FlightListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class is the activity for searching flights
 *
 * Author: Albert Pham
 * Date: 2019-03-23
 */
public class FlightSearchActivity extends AppCompatActivity {


    Toolbar searchToolbar;
    ListView listView;
    FlightListAdapter adapter;
    ProgressBar progressBar;
    EditText searchEdit;
    Button searchButton;

    LinearLayout progressViewLayout;
    LinearLayout listviewLayout;


    /**
     * Sets the toolbar and list view
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);

        final boolean isTablet = findViewById(R.id.flightSearchFragmentLocation) != null; //check if the FrameLayout is loaded

        progressViewLayout = findViewById(R.id.searchProgressViewLayout);
        listviewLayout = findViewById(R.id.searchListViewLayout);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        searchEdit = findViewById(R.id.searchEdit);
        searchButton = findViewById(R.id.searchButton);

        searchToolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(searchToolbar);

        listView = findViewById(R.id.flightListView);
        adapter = new FlightListAdapter(this);



        FlightDetailFragment dFragment = new FlightDetailFragment(); //add a DetailFragment
        listView.setOnItemClickListener((parent, view, position, id) -> {
        Flight flight = adapter.getItem(position);


            Bundle dataToPass = new Bundle();

            dataToPass.putString("isSave","1");
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
                 FlightSearchActivity.this.getSupportFragmentManager()
                         .beginTransaction()
                         .add(R.id.flightSearchFragmentLocation, dFragment) //Add the fragment in FrameLayout
                         .addToBackStack("AnyName") //make the back button undo the transaction
                         .commit(); //actually load the fragment.
             } else {
                 // If it's a phone

                 Intent intent = new Intent(FlightSearchActivity.this, FlightDetailsActivity.class);
                 intent.putExtra("isSave",1);
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





        });



        SharedPreferences pref = getSharedPreferences("prevSearch",Context.MODE_PRIVATE);
        String searchString = pref.getString("airportSearched", getResources().getString(R.string.flightSearchEditHint));
        searchEdit.setHint(searchString);



        listView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {

            // Shared pref saves the previous search
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("airportSearched", searchEdit.getText().toString().toUpperCase());
            editor.apply();


            progressViewLayout.setVisibility(View.VISIBLE);
            listviewLayout.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            // Clears the search before searching again
            adapter.clearFlightList();
            FlightQuery something  = new FlightQuery();
            // Personal account key
//            something.execute("https://aviation-edge.com/v2/public/flights?key=51db2e-eaf120&depIata="+searchEdit.getText(),
//                    "https://aviation-edge.com/v2/public/flights?key=51db2e-eaf120&arrIata="+searchEdit.getText());

            // Algonquin college key
            something.execute("https://aviation-edge.com/v2/public/flights?key=65e13d-5d40f1&depIata="+searchEdit.getText(),
                    "https://aviation-edge.com/v2/public/flights?key=65e13d-5d40f1&arrIata="+searchEdit.getText());




        });
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


    /**
     * Class is used to query the flight status tracker API
     */
    private class FlightQuery extends AsyncTask<String, Integer, String> {

        /**
         * Uses the API to grab json data adds the flight into the adapter
         * @param strings links
         * @return string
         */
        @Override
        protected String doInBackground(String... strings) {
            try {
                // Below reads the JSON

                // Flights departing from link
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }

                String result = sb.toString();
                JSONArray flights = new JSONArray(result);

                for(int i=0;i<flights.length();i++){
                    JSONObject flight = flights.getJSONObject(i);

                    Flight temp = new Flight();

                    JSONObject flightNumber = flight.getJSONObject("flight");
                    JSONObject geography = flight.getJSONObject("geography");
                    JSONObject speed = flight.getJSONObject("speed");
                    JSONObject departure = flight.getJSONObject("departure");
                    JSONObject arrival = flight.getJSONObject("arrival");

                    temp.setFlightName(flightNumber.getString("number"));
                    temp.setFlightAltitude(geography.getString("altitude"));
                    temp.setFlightDirection(geography.getString("direction"));
                    temp.setFlightLatitude(geography.getString("latitude"));
                    temp.setFlightLongitude(geography.getString("longitude"));
                    temp.setFlightSpeed(speed.getString("horizontal"));
                    temp.setFlightStatus(flight.getString("status"));
                    temp.setFlightDepartingFrom(departure.getString("iataCode"));
                    temp.setFlightArrivingTo(arrival.getString("iataCode"));




                    adapter.addFlight(temp);
                }


                url = new URL(strings[1]);
                urlConnection = (HttpURLConnection) url.openConnection();
                inStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inStream));
                sb = new StringBuilder();

                while ((line = reader.readLine()) != null){
                    sb.append(line + "\n");
                }

                result = sb.toString();
                flights = new JSONArray(result);

                for(int i=0;i<flights.length();i++){
                    JSONObject flight = flights.getJSONObject(i);

                    Flight temp = new Flight();

                    JSONObject flightNumber = flight.getJSONObject("flight");
                    JSONObject geography = flight.getJSONObject("geography");
                    JSONObject speed = flight.getJSONObject("speed");
                    JSONObject departure = flight.getJSONObject("departure");
                    JSONObject arrival = flight.getJSONObject("arrival");

                    temp.setFlightName(flightNumber.getString("number"));
                    temp.setFlightAltitude(geography.getString("altitude"));
                    temp.setFlightDirection(geography.getString("direction"));
                    temp.setFlightLatitude(geography.getString("latitude"));
                    temp.setFlightLongitude(geography.getString("longitude"));
                    temp.setFlightSpeed(speed.getString("horizontal"));
                    temp.setFlightStatus(flight.getString("status"));
                    temp.setFlightDepartingFrom(departure.getString("iataCode"));
                    temp.setFlightArrivingTo(arrival.getString("iataCode"));



                    adapter.addFlight(temp);
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Something went wrong with the API",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }


        /**
         * Sets the list visible and takes the progress bar away
         * @param s string
         */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter.notifyDataSetChanged();
            progressViewLayout.setVisibility(View.INVISIBLE);
            listviewLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


}
