package pe.upc.ridera.bikelab.vehicles.application.commands;

import java.util.UUID;

/**
 * Esta clase indica la intención de liberar un bloqueo previamente registrado sobre un vehículo.
 */ 
public record UnblockAvailabilityCommand(
        UUID requesterId,
        UUID vehicleId,
        UUID slotId) {
}
