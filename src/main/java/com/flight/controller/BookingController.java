package com.flight.controller;

import com.flight.model.Booking;
import com.flight.model.Flight;
import com.flight.repository.BookingRepository;
import com.flight.repository.FlightRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;

    public BookingController(BookingRepository bookingRepository, FlightRepository flightRepository) {
        this.bookingRepository = bookingRepository;
        this.flightRepository = flightRepository;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingRequest request) {
        Flight flight = flightRepository.findByFlightNumber(request.getFlightNumber())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Flight not found: " + request.getFlightNumber()));

        if (flight.getAvailableSeats() < request.getSeatCount()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Not enough seats available. Requested: " + request.getSeatCount()
                    + ", Available: " + flight.getAvailableSeats());
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - request.getSeatCount());
        flightRepository.save(flight);

        Booking booking = new Booking(request.getFlightNumber(), request.getPassengerName(), request.getSeatCount());
        bookingRepository.save(booking);

        return ResponseEntity.status(HttpStatus.CREATED).body(booking);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable String bookingId) {
        return bookingRepository.findById(bookingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> cancelBooking(@PathVariable String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Booking not found: " + bookingId));

        flightRepository.findByFlightNumber(booking.getFlightNumber()).ifPresent(flight -> {
            flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatCount());
            flightRepository.save(flight);
        });

        bookingRepository.deleteById(bookingId);
        return ResponseEntity.noContent().build();
    }
}
