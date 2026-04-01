package com.flight.exception;

public class InsufficientSeatsException extends RuntimeException {
    public InsufficientSeatsException(int requested, int available) {
        super("Not enough seats. Requested: " + requested + ", Available: " + available);
    }
}
