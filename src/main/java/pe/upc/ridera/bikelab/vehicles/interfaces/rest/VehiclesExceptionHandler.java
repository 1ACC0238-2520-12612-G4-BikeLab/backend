package pe.upc.ridera.bikelab.vehicles.interfaces.rest;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import pe.upc.ridera.bikelab.vehicles.domain.exceptions.VehicleDomainException;
import pe.upc.ridera.bikelab.vehicles.domain.exceptions.VehicleNotFoundException;

/**
 * Esta clase transforma las excepciones de dominio del contexto de vehículos en respuestas HTTP legibles.
 */ 
@RestControllerAdvice(basePackages = "pe.upc.ridera.bikelab.vehicles.interfaces.rest")
public class VehiclesExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(VehicleNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorPayload(exception.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(VehicleDomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomain(VehicleDomainException exception) {
        return ResponseEntity.badRequest().body(errorPayload(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    private Map<String, Object> errorPayload(String message, HttpStatus status) {
        return Map.of(
                "timestamp", Instant.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message);
    }
}
