package com.example.cst2335finalproject.classes;

/**
 * This class holds the details of a flight
 *
 * Author: Albert Pham
 * Date: 2019-03-23
 */
public class Flight {


    private String flightName,flightLocation,flightStatus,flightSpeed,flightAltitude;


    public Flight(String flightName, String flightLocation, String flightStatus){
        this.flightName = flightName;
        this.flightLocation = flightLocation;
        this.flightStatus = flightStatus;


    }

    /**
     * Gets the flight name
     *
     * @return flight name
     */
    public String getFlightName(){
        return flightName;
    }

    /**
     * Gets the flight location
     * @return flight location
     */
    public String getFlightLocation(){
        return flightLocation;
    }

    /**
     * Gets the flight status
     * @return flight status
     */
    public String getFlightStatus(){
        return flightStatus;
    }

    /**
     * Gets the flight speed
     * @return flight speed
     */
    public String getFlightSpeed(){
        return flightSpeed;
    }

    /**
     * Gets the flight altitude
     * @return flight altitude
     */
    public String getFlightAltitude(){
        return flightAltitude;
    }

    /**
     * Sets flight name
     * @param flightName flight name
     */
    public void setFlightName(String flightName){
        this.flightName = flightName;
    }

    /**
     * Sets flight location
     * @param flightLocation flight location
     */
    public void setFlightLocation(String flightLocation){
        this.flightLocation = flightLocation;
    }

    /**
     * Sets flight status
     * @param flightStatus flight status
     */
    public void setFlightStatus(String flightStatus){
        this.flightStatus = flightStatus;
    }

    /**
     * Sets flight speed
     * @param flightSpeed flight speed
     */
    public void setFlightSpeed(String flightSpeed){
        this.flightSpeed = flightSpeed;
    }

    /**
     * Sets flight altitude
     * @param flightAltitude flight altitude
     */
    public void setFlightAltitude(String flightAltitude){
        this.flightAltitude = flightAltitude;
    }



}
