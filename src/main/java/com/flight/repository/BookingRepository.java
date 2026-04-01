package com.flight.repository;

import com.flight.model.Booking;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class BookingRepository {
    private final ConcurrentHashMap<String, Booking> store = new ConcurrentHashMap<>();

    public Booking save(Booking booking) {
        store.put(booking.getBookingId(), booking);
        return booking;
    }

    public Optional<Booking> findById(String bookingId) {
        return Optional.ofNullable(store.get(bookingId));
    }

    public boolean deleteById(String bookingId) {
        return store.remove(bookingId) != null;
    }

    public List<Booking> findAll() {
        return new ArrayList<>(store.values());
    }
}
