package com.flight.model;

import java.util.UUID;

public class Booking {
    private String bookingId;
    private String flightNumber;
    private String passengerName;
    private int seatCount;

    public Booking() {}

    public Booking(String flightNumber, String passengerName, int seatCount) {
        this.bookingId = UUID.randomUUID().toString();
        this.flightNumber = flightNumber;
        this.passengerName = passengerName;
        this.seatCount = seatCount;
    }

    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }
}
