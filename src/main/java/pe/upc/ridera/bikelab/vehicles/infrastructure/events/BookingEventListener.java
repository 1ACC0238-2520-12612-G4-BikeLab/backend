package pe.upc.ridera.bikelab.vehicles.infrastructure.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.renting.domain.model.events.BookingCancelledEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.BookingCreatedEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.RentalFinishedEvent;
import pe.upc.ridera.bikelab.renting.domain.model.events.RentalStartedEvent;
import pe.upc.ridera.bikelab.vehicles.application.services.VehicleCommandService;

/**
 * Esta clase escucha los eventos del contexto de renting para mantener sincronizado el estado de los vehículos.
 */ 
@Component
public class BookingEventListener {

    private final VehicleCommandService vehicleCommandService;

    public BookingEventListener(VehicleCommandService vehicleCommandService) {
        this.vehicleCommandService = vehicleCommandService;
    }

    @EventListener
    public void onBookingCreated(BookingCreatedEvent event) {
        vehicleCommandService.handleBookingCreated(event);
    }

    @EventListener
    public void onBookingCancelled(BookingCancelledEvent event) {
        vehicleCommandService.handleBookingCancelled(event);
    }

    @EventListener
    public void onRentalStarted(RentalStartedEvent event) {
        vehicleCommandService.handleRentalStarted(event);
    }

    @EventListener
    public void onRentalFinished(RentalFinishedEvent event) {
        vehicleCommandService.handleRentalFinished(event);
    }
}
