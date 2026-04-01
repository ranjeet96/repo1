# Flight Ticket Booking API

A simple REST API for flight ticket booking built with Spring Boot and Java 17.

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+

### Steps

```bash
git clone https://github.com/ranjeet96/repo1.git
cd repo1
mvn spring-boot:run
```

The server starts on `http://localhost:8080`.

---

## Example Requests

### List all flights
```bash
curl http://localhost:8080/flights
```

### Get a specific flight
```bash
curl http://localhost:8080/flights/AI101
```

### Create a booking
```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{"flightNumber": "AI101", "passengerName": "John Doe", "seatCount": 2}'
```

### Get a booking
```bash
curl http://localhost:8080/bookings/{bookingId}
```

### Cancel a booking
```bash
curl -X DELETE http://localhost:8080/bookings/{bookingId}
```
