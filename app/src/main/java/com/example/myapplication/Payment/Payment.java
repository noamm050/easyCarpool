package com.example.myapplication.Payment;

public class Payment {
    private Long fromId;
    private Long toId;
    private double amount;
    private Long tripId;


    public Payment() {}

    public Payment(Long fromId, Long toId, double amount, Long tripId) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.tripId = tripId;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }

    public Long getToId() {
        return toId;
    }

    public void setToId(Long toId) {
        this.toId = toId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}
