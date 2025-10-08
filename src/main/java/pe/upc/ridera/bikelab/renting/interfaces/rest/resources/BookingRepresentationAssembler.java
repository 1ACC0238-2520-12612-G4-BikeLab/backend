package pe.upc.ridera.bikelab.renting.interfaces.rest.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import pe.upc.ridera.bikelab.renting.application.dto.BookingResource;
import pe.upc.ridera.bikelab.renting.domain.model.aggregates.Booking;

@Component
/**
 * Esta clase transforma las entidades de dominio de reservas a recursos REST.
 */
public class BookingRepresentationAssembler {

    public BookingResource toResource(Booking booking) {
        return new BookingResource(booking.getId(), booking.getCustomerId(), booking.getProviderId(), booking.getVehicleId(),
                booking.getStartAt(), booking.getEndAt(), booking.getActivatedAt(), booking.getFinishedAt(),
                booking.getState(), booking.getAuthorizedAmount(), booking.getCapturedAmount(), booking.getPenaltyAmount());
    }

    public List<BookingResource> toResource(List<Booking> bookings) {
        return bookings.stream().map(this::toResource).collect(Collectors.toList());
    }
}
