package com.example.cst2335finalproject.classes;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cst2335finalproject.R;

/**
 * Class is for flight detail fragment
 *
* Author: Albert Pham
* Date: 2019-04-12
 */
public class FlightDetailFragment extends Fragment {
    FlightDatabaseHelper databaseHelper;


    private Button saveRemove;
    private boolean isTablet;
    private Bundle dataFromActivity;
    private String isSaveString;
    private String nameString,statusString,speedString,latitudeString,longitudeString,directionString,altitudeString,arrivingToString,departingFromString;
    private View result;
    private long id;

    public void setTablet(boolean tablet) { isTablet = tablet; }

    /**
     * Creates the detail fragment
     * @param inflater inflater
     * @param container container
     * @param savedInstanceState saved instance state
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("id");
        isSaveString = dataFromActivity.getString("isSave");
        nameString = dataFromActivity.getString("name");
        statusString = dataFromActivity.getString("status");
        speedString = dataFromActivity.getString("speed");
        latitudeString = dataFromActivity.getString("latitude");
        longitudeString = dataFromActivity.getString("longitude");
        directionString = dataFromActivity.getString("direction");
        altitudeString = dataFromActivity.getString("altitude");
        arrivingToString = dataFromActivity.getString("arrivingTo");
        departingFromString = dataFromActivity.getString("departingFrom");

        // Inflate the layout for this fragment
        result =  inflater.inflate(R.layout.flight_details, container, false);

        databaseHelper = new FlightDatabaseHelper(result.getContext().getApplicationContext());

        saveRemove = result.findViewById(R.id.saveRemoveButton);

        //show the info
        TextView name = result.findViewById(R.id.flightDetailsName);
        TextView status = result.findViewById(R.id.flightDetailsStatus);
        TextView latitude = result.findViewById(R.id.flightDetailsLatitude);
        TextView longitude = result.findViewById(R.id.flightDetailsLongitude);
        TextView direction = result.findViewById(R.id.flightDetailsDirection);
        TextView speed = result.findViewById(R.id.flightDetailsSpeed);
        TextView altitude = result.findViewById(R.id.flightDetailsAltitude);


        name.setText(dataFromActivity.getString("name"));
        status.setText("Status: " +dataFromActivity.getString("status"));
        latitude.setText("Latitude: " +dataFromActivity.getString("latitude"));
        longitude.setText("Longitude: " +dataFromActivity.getString("longitude"));
        direction.setText("Direction: " +dataFromActivity.getString("direction"));
        speed.setText("Speed: " +dataFromActivity.getString("speed"));
        altitude.setText("Altitude: " +dataFromActivity.getString("altitude"));


        // add save or delete button depending on if its save or not
        if(isSaveString.equals("1")){
            saveButton();
        }else {
            removeButton();
        }

        return result;
    }

    /**
     * Sets the button as SAVE
     */
    public void saveButton(){
        saveRemove.setText(getResources().getText(R.string.flightSearchDetailSaveButton));
        saveRemove.setOnClickListener(v -> {

            boolean insertData = databaseHelper.addData(nameString,statusString,longitudeString,latitudeString,directionString,speedString,altitudeString,arrivingToString,departingFromString);

            if(insertData){
                Toast.makeText(result.getContext().getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(result.getContext().getApplicationContext(),"Not saved",Toast.LENGTH_SHORT).show();
            }


            if(isTablet){
                FlightDetailFragment.this.getActivity().getSupportFragmentManager().beginTransaction().remove(FlightDetailFragment.this).commit();
                FlightDetailFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }

        });
    }

    /**
     * Sets the button as REMOVE
     */
    public void removeButton(){
        saveRemove.setText(getResources().getText(R.string.flightDetailsRemoveButton));
        saveRemove.setOnClickListener(v -> {

            boolean deleteData = databaseHelper.deleteData(Long.toString(id));

            if(deleteData){
                Toast.makeText(result.getContext().getApplicationContext(),"Deleted",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(result.getContext().getApplicationContext(),"Not deleted",Toast.LENGTH_SHORT).show();
            }

            if(isTablet){
                FlightDetailFragment.this.getActivity().getSupportFragmentManager().beginTransaction().remove(FlightDetailFragment.this).commit();
                FlightDetailFragment.this.getActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }

    /**
     * Removes the last frag if there are any
     */
    public void removeLast(){

        try{
            FlightDetailFragment.this.getActivity().getSupportFragmentManager().beginTransaction().remove(FlightDetailFragment.this).commit();
//            FlightDetailFragment.this.getActivity().getSupportFragmentManager().popBackStack();
        } catch (NullPointerException e){

        }
    }




}
