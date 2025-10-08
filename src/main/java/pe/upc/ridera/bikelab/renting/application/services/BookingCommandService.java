package pe.upc.ridera.bikelab.renting.application.services;

import pe.upc.ridera.bikelab.renting.application.commands.CancelBookingCommand;
import pe.upc.ridera.bikelab.renting.application.commands.CreateBookingCommand;
import pe.upc.ridera.bikelab.renting.application.commands.FinishRentalCommand;
import pe.upc.ridera.bikelab.renting.application.commands.StartRentalCommand;
import pe.upc.ridera.bikelab.renting.domain.model.aggregates.Booking;

/**
 * Esta interfaz agrupa los casos de uso que alteran el estado de una reserva.
 */
public interface BookingCommandService {

    Booking handle(CreateBookingCommand command);

    Booking handle(CancelBookingCommand command);

    Booking handle(StartRentalCommand command);

    Booking handle(FinishRentalCommand command);
}
