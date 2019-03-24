package com.example.cst2335finalproject.classes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cst2335finalproject.R;

import java.util.ArrayList;
/**
 * This class is the adapter for the list view on the main page
 *
 * Author: Albert Pham
 * Date: 2019-03-23
 */
public class FlightListAdapter extends BaseAdapter {


    ArrayList<Flight> flights;
    Activity ctx;

    public FlightListAdapter(Activity ctx){
        this.ctx = ctx;
        flights = new ArrayList<>();
    }

    /**
     * Adding a flight to the array list
     *
     * @param flight
     */
    public void addFlight(Flight flight){
        flights.add(flight);
    }

    /**
     * Get flight count, how many flights are saved under the list
     * @return
     */
    @Override
    public int getCount() {
        return flights.size();
    }

    /**
     * Get the flight with the index
     *
     * @param position index of the flight on the array list
     * @return
     */
    @Override
    public Flight getItem(int position) {
        return flights.get(position);
    }

    /**
     * Get the item ID of the index
     *
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get the view of the specific flight
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = ctx.getLayoutInflater();
        View view = null;
        Flight flight = flights.get(position);


        view = layoutInflater.inflate(R.layout.flight_list,null);
        TextView name = view.findViewById(R.id.listFlightName);
        TextView location = view.findViewById(R.id.listFlightLocation);
        TextView status = view.findViewById(R.id.listFlightStatus);

        name.setText(flight.getFlightName());
        location.setText(flight.getFlightLocation());
        status.setText(flight.getFlightStatus());

        return view;
    }


}
