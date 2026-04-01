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

---

## Manual Improvements Made (Step 2)

The following issues were identified in the AI-generated code and fixed manually:

| # | Area | Issue | Fix Applied |
|---|------|-------|-------------|
| 1 | **Race condition** | `BookingController` used a non-atomic check-then-act pattern for seat deduction — two concurrent requests could both pass the availability check and both book, causing overbooking | Wrapped the check and deduction in a `synchronized (flight)` block |
| 2 | **Dead custom exceptions** | `FlightNotFoundException`, `BookingNotFoundException`, `InsufficientSeatsException` were defined but never thrown — controllers used `ResponseStatusException` directly, bypassing `GlobalExceptionHandler` entirely | Controllers now throw the correct custom exceptions so the global handler activates |
| 3 | **No input validation** | `BookingRequest` had no constraints — null `flightNumber`, blank `passengerName`, or `seatCount=0/-1` were silently accepted | Added `@NotBlank` on string fields, `@Min(1)` on `seatCount`, `@Valid` on `@RequestBody` |
| 4 | **Unhandled validation errors** | `MethodArgumentNotValidException` from `@Valid` had no handler — Spring would return a generic 400 with no useful message | Added a handler in `GlobalExceptionHandler` that collects all field-level errors into a readable response |
| 5 | **Inconsistent 404 format** | `FlightController.getFlightByNumber` returned a bare 404 with no body, while every other error returned structured JSON | Now throws `FlightNotFoundException` so all 404s go through the same structured handler |
| 6 | **Unused import** | `FlightRepository` imported `java.util.Collection` but never used it | Removed |

---

## Further Improvements (Given More Time)

- **Service layer** — Business logic (seat check, booking creation, cancellation) lives in the controller, violating SRP. Extract a `BookingService` to separate concerns and make logic unit-testable without Spring context.
- **Immutable domain models / DTOs** — `Flight` and `Booking` expose public setters on all fields (including `totalSeats` which should never change). Introduce separate request/response DTOs and tighten model mutability.
- **Persistent storage** — All data is lost on restart. Replace `ConcurrentHashMap` with Spring Data JPA + H2 (embedded) for development and PostgreSQL for production.
- **Proper concurrency** — The `synchronized (flight)` fix works for a single JVM but is fragile; the same `Flight` object must always be referenced. A real fix uses database-level optimistic locking (`@Version`) or atomic compare-and-swap.
- **Unit and integration tests** — No tests exist. Add `@WebMvcTest` slice tests for controllers and a full integration test with `@SpringBootTest` to cover the booking flow end-to-end.
- **Pagination** — `GET /flights` returns all flights as a single list. Add `Pageable` support for scalability.
