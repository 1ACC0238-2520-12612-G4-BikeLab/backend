package pe.upc.ridera.bikelab.vehicles.interfaces.rest.resources;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Esta clase representa la carga útil necesaria para registrar un vehículo mediante la API.
 */ 
public record CreateVehicleRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull @DecimalMin("0.1") BigDecimal hourlyPrice,
        double latitude,
        double longitude) {
}
