package com.flight.controller;

import com.flight.exception.BookingNotFoundException;
import com.flight.exception.FlightNotFoundException;
import com.flight.exception.InsufficientSeatsException;
import com.flight.model.Booking;
import com.flight.model.Flight;
import com.flight.repository.BookingRepository;
import com.flight.repository.FlightRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody BookingRequest request) {
        Flight flight = flightRepository.findByFlightNumber(request.getFlightNumber())
                .orElseThrow(() -> new FlightNotFoundException(request.getFlightNumber()));

        synchronized (flight) {
            if (flight.getAvailableSeats() < request.getSeatCount()) {
                throw new InsufficientSeatsException(request.getSeatCount(), flight.getAvailableSeats());
            }
            flight.setAvailableSeats(flight.getAvailableSeats() - request.getSeatCount());
        }

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
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        flightRepository.findByFlightNumber(booking.getFlightNumber()).ifPresent(flight -> {
            flight.setAvailableSeats(flight.getAvailableSeats() + booking.getSeatCount());
            flightRepository.save(flight);
        });

        bookingRepository.deleteById(bookingId);
        return ResponseEntity.noContent().build();
    }
}
