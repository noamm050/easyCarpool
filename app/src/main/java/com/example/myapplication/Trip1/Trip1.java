package com.example.myapplication.Trip1;

import com.example.myapplication.User.User;

import java.time.LocalTime;
import java.util.Set;


public class Trip1 {

    private String origin;
    private String destination;
    private String startHour;
    private double distance;
    private Long driverId;
    private Set<User> seats ;
    private Long id;
    private String startDate ;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

//    public String getTripDate() {
//        return tripDate;
//    }

//    public void setTripDate(String tripDate) {
//        this.tripDate = tripDate;
//    }

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public void setPassengerCounter(Integer passengerCounter) {
        this.passengerCounter = passengerCounter;
    }

    private int passengerCapacity ;
    private Integer passengerCounter ;


    public Integer getPassengerCounter() {
        return passengerCounter;
    }

    public void setPassengerCounter(int passengerCounter) {
        this.passengerCounter = passengerCounter;
    }



    public Trip1 () {

    }


    public Trip1(String origin, String destination, String startHour, double distance, Set<User> seats) {
        this.origin = origin;
        this.destination = destination;
        this.startHour = startHour;
        this.distance = distance;
        this.seats = seats;
    }

    public Trip1(String origin, String destination, String startHour, double distance) {
        this.origin = origin;
        this.destination = destination;
        this.startHour = startHour;
        this.distance = distance;
    }




    public String getOrigin() {
        return origin;
    }
//
//    public int getCapacity() {
//        return capacity;
//    }

//    public void setCapacity(int capacity) {
//        this.capacity = capacity;
//    }
    public Long getTripId() {
        return id;
    }

    public void setTripId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getDate() {
//        return date;
//    }

//    public void setDate(String date) {
//        this.date = date;
//    }
    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public void setSeats(Set<User> seats) {
        this.seats = seats;
    }


    public Set<User> getSeats() {
        return seats;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


}
