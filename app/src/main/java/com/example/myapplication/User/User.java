package com.example.myapplication.User;

import com.example.myapplication.Trip1.Trip1;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String firstName ;
    private String lastName ;
    private String email ;
    private String password ;
    private long id ;
    private double paid ;
    private double received ;
    private Set<Trip1> trips=new HashSet<>();

    public long getId() {
        return id;
    }

    public Set<Trip1> getTrips() {
        return trips;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getReceived() {
        return received;
    }

    public void setReceived(double received) {
        this.received = received;
    }

    public User(String firstName, String lastName, String email, String password, long id, double paid, double received) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = id;
        this.paid = paid;
        this.received = received;
    }

    public User() {

    }

    public User(String firstName, String lastName, String email, String password, long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public long getUserId() {
        return id;
    }

    public void setUserId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
