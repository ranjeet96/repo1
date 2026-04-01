package com.flight;

import com.flight.model.Flight;
import com.flight.repository.FlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final FlightRepository flightRepository;

    public DataInitializer(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void run(String... args) {
        flightRepository.save(new Flight("AI101", "Delhi", "Mumbai", 180));
        flightRepository.save(new Flight("AI202", "Mumbai", "Bangalore", 150));
        flightRepository.save(new Flight("AI303", "Bangalore", "Chennai", 120));
        flightRepository.save(new Flight("AI404", "Chennai", "Hyderabad", 200));
        flightRepository.save(new Flight("AI505", "Hyderabad", "Delhi", 160));
    }
}
