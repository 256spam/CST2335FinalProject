package com.example.cst2335finalproject;

import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335finalproject.classes.Flight;
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

        listView.setOnItemClickListener((parent, view, position, id) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.flight_details_dialog,null);
            builder.setView(v);

            Flight flight = adapter.getItem(position);

            TextView name = v.findViewById(R.id.flightDetailsName);
            TextView status = v.findViewById(R.id.flightDetailsStatus);
            TextView latitude = v.findViewById(R.id.flightDetailsLatitude);
            TextView longitude = v.findViewById(R.id.flightDetailsLongitude);
            TextView direction = v.findViewById(R.id.flightDetailsDirection);
            TextView speed = v.findViewById(R.id.flightDetailsSpeed);
            TextView altitude = v.findViewById(R.id.flightDetailsAltitude);

            name.setText(flight.getFlightName());
            status.setText("Status: " + flight.getFlightStatus());
            speed.setText("Speed: " + flight.getFlightSpeed());
            latitude.setText("Latitude: " + flight.getFlightLatitude());
            longitude.setText("Longitude: " + flight.getFlightLongitude());
            direction.setText("Direction: " + flight.getFlightDirection());
            altitude.setText("Altitude: " + flight.getFlightAltitude());


            builder.setNegativeButton(R.string.flightSearchDetailSaveButton, (dialog, which) -> {
                // TODO: Save it in the database of saved items
                Toast.makeText(this, "Soon to be Saved", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            });

            builder.create().show();
        });

        listView.setAdapter(adapter);

        searchButton.setOnClickListener(v -> {
            progressViewLayout.setVisibility(View.VISIBLE);
            listviewLayout.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            // Clears the search before searching again
            adapter.clearFlightList();
            FlightQuery something  = new FlightQuery();
            something.execute("https://aviation-edge.com/v2/public/flights?key=51db2e-eaf120&depIata="+searchEdit.getText(),
                    "https://aviation-edge.com/v2/public/flights?key=51db2e-eaf120&arrIata="+searchEdit.getText());
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
