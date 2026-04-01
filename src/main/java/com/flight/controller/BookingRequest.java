package com.flight.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class BookingRequest {
    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Passenger name is required")
    private String passengerName;

    @Min(value = 1, message = "Seat count must be at least 1")
    private int seatCount;

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }
}
