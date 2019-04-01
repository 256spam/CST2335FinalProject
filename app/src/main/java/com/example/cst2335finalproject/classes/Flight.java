package com.example.cst2335finalproject.classes;

/**
 * This class holds the details of a flight
 *
 * Author: Albert Pham
 * Date: 2019-03-23
 */
public class Flight {


    private String flightName, flightLatitude, flightLongitude, flightDirection,flightStatus,flightSpeed,flightAltitude;
    private String flightDepartingFrom,flightArrivingTo;

    /**
     * Initial constructor
     *
     * @param flightName flight name
     * @param flightLatitude flight latitude
     * @param flightLongitude flight longitude
     * @param flightDirection flight direction
     * @param flightStatus flight status
     * @param flightSpeed flight speed
     * @param flightAltitude flight altitude
     */
    public Flight(String flightName, String flightLatitude,String flightLongitude, String flightDirection, String flightStatus, String flightSpeed,String flightAltitude){
        this.flightName = flightName;
        this.flightLatitude = flightLatitude;
        this.flightLongitude = flightLongitude;
        this.flightDirection = flightDirection;
        this.flightStatus = flightStatus;
        this.flightSpeed = flightSpeed;
        this.flightAltitude = flightAltitude;
    }

    /**
     * Default constructor
     */
    public Flight(){

    }


    /**
     * Gets the flight arrival details
     * @return flight arriving
     */
    public String getFlightArrivingTo() {
        return flightArrivingTo;
    }

    /**
     * Gets the flight departure details
     * @return flight departing
     */
    public String getFlightDepartingFrom() {
        return flightDepartingFrom;
    }

    /**
     * Gets the flight latitude
     *
     * @return flight latitude
     */
    public String getFlightLatitude() {
        return flightLatitude;
    }

    /**
     * Gets the flight direction
     *
     * @return get flight direction
     */
    public String getFlightDirection() {
        return flightDirection;
    }

    /**
     * Gets the flight longitude
     *
     * @return flight longitude
     */
    public String getFlightLongitude() {
        return flightLongitude;
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

    /**
     * Sets the flight direction
     * @param flightDirection flight direction
     */
    public void setFlightDirection(String flightDirection) {
        this.flightDirection = flightDirection;
    }

    /**
     * Sets the flight latitude
     *
     * @param flightLatitude flight latitude
     */
    public void setFlightLatitude(String flightLatitude) {
        this.flightLatitude = flightLatitude;
    }

    /**
     * Sets flight longitude
     * @param flightLongitude flight longitude
     */
    public void setFlightLongitude(String flightLongitude) {
        this.flightLongitude = flightLongitude;
    }


    /**
     * Sets the flight arriving to
     * @param flightArrivingTo arriving to
     */
    public void setFlightArrivingTo(String flightArrivingTo) {
        this.flightArrivingTo = flightArrivingTo;
    }

    /**
     * Sets flight departing from
     * @param flightDepartingFrom departing from
     */
    public void setFlightDepartingFrom(String flightDepartingFrom) {
        this.flightDepartingFrom = flightDepartingFrom;
    }
}
