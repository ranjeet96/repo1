package com.flight.repository;

import com.flight.model.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class FlightRepository {
    private final ConcurrentHashMap<String, Flight> store = new ConcurrentHashMap<>();

    public Flight save(Flight flight) {
        store.put(flight.getFlightNumber(), flight);
        return flight;
    }

    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return Optional.ofNullable(store.get(flightNumber));
    }

    public List<Flight> findAll() {
        return new ArrayList<>(store.values());
    }
}
