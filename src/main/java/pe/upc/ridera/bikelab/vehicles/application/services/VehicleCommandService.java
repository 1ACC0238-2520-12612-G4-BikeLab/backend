package pe.upc.ridera.bikelab.vehicles.application.services;

import pe.upc.ridera.bikelab.renting.domain.model.events.BookingCancelledEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.BookingCreatedEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.RentalFinishedEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.RentalStartedEvent;
import pe.upc.ridera.bikelab.vehicles.application.commands.BlockAvailabilityCommand;
import pe.upc.ridera.bikelab.vehicles.application.commands.CreateVehicleCommand;
import pe.upc.ridera.bikelab.vehicles.application.commands.UnblockAvailabilityCommand;
import pe.upc.ridera.bikelab.vehicles.application.commands.UpdateVehicleCommand;
import pe.upc.ridera.bikelab.vehicles.application.dto.AvailabilitySlotResource;
import pe.upc.ridera.bikelab.vehicles.application.dto.VehicleResource;

/**
 * Esta interfaz describe los casos de uso de escritura del contexto de vehículos, incluyendo la reacción a eventos externos.
 */ 
public interface VehicleCommandService {

    VehicleResource createVehicle(CreateVehicleCommand command);

    VehicleResource updateVehicle(UpdateVehicleCommand command);

    AvailabilitySlotResource blockAvailability(BlockAvailabilityCommand command);

    void unblockAvailability(UnblockAvailabilityCommand command);

    void handleBookingCreated(BookingCreatedEvent event);

    void handleBookingCancelled(BookingCancelledEvent event);

    void handleRentalStarted(RentalStartedEvent event);

    void handleRentalFinished(RentalFinishedEvent event);
}
