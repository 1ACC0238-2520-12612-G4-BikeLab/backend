package pe.upc.ridera.bikelab.renting.interfaces.rest;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import pe.upc.ridera.bikelab.configuration.OpenApiConfig;
import pe.upc.ridera.bikelab.renting.application.commands.CancelBookingCommand;
import pe.upc.ridera.bikelab.renting.application.commands.CreateBookingCommand;
import pe.upc.ridera.bikelab.renting.application.commands.FinishRentalCommand;
import pe.upc.ridera.bikelab.renting.application.commands.StartRentalCommand;
import pe.upc.ridera.bikelab.renting.application.dto.BookingResource;
import pe.upc.ridera.bikelab.renting.application.services.BookingCommandService;
import pe.upc.ridera.bikelab.renting.application.services.BookingQueryService;
import pe.upc.ridera.bikelab.renting.interfaces.rest.resources.BookingRepresentationAssembler;
import pe.upc.ridera.bikelab.renting.interfaces.rest.resources.CreateBookingRequest;

@RestController
@RequestMapping("/api/renting/bookings")
@Validated
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@Tag(name = "BC: Renting")
/**
 * Esta clase expone el API REST para que clientes y proveedores gestionen sus reservas.
 */
public class BookingController {

    private final BookingCommandService bookingCommandService;
    private final BookingQueryService bookingQueryService;
    private final BookingRepresentationAssembler assembler;

    public BookingController(BookingCommandService bookingCommandService,
                             BookingQueryService bookingQueryService,
                             BookingRepresentationAssembler assembler) {
        this.bookingCommandService = bookingCommandService;
        this.bookingQueryService = bookingQueryService;
        this.assembler = assembler;
    }

    @Operation(summary = "Crear una nueva reserva",
            description = "Permite a un cliente generar una reserva indicando el vehículo y el periodo deseado.")
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<BookingResource> create(@Valid @RequestBody CreateBookingRequest request) {
        Long customerId = currentUserId();
        var command = new CreateBookingCommand(customerId, request.vehicleId(), request.startAt(), request.endAt());
        var booking = bookingCommandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toResource(booking));
    }

    @Operation(summary = "Cancelar una reserva existente",
            description = "El cliente autenticado anula una reserva activa antes de que comience el alquiler.")
    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<Void> cancel(@PathVariable UUID bookingId) {
        Long customerId = currentUserId();
        bookingCommandService.handle(new CancelBookingCommand(customerId, bookingId));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Iniciar el alquiler de una reserva",
            description = "El proveedor confirma el inicio del alquiler para habilitar la entrega del vehículo.")
    @PostMapping("/{bookingId}/start")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    public ResponseEntity<BookingResource> start(@PathVariable UUID bookingId) {
        Long providerId = currentUserId();
        var booking = bookingCommandService.handle(new StartRentalCommand(providerId, bookingId));
        return ResponseEntity.ok(assembler.toResource(booking));
    }

    @Operation(summary = "Finalizar el alquiler de una reserva",
            description = "El proveedor registra la devolución del vehículo y cierra la reserva.")
    @PostMapping("/{bookingId}/finish")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    public ResponseEntity<BookingResource> finish(@PathVariable UUID bookingId) {
        Long providerId = currentUserId();
        var booking = bookingCommandService.handle(new FinishRentalCommand(providerId, bookingId));
        return ResponseEntity.ok(assembler.toResource(booking));
    }

    @Operation(summary = "Listar las reservas del cliente actual",
            description = "Obtiene el historial de reservas perteneciente al cliente autenticado.")
    @GetMapping("/mine")
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public ResponseEntity<List<BookingResource>> mine() {
        Long customerId = currentUserId();
        var bookings = bookingQueryService.getCustomerBookings(customerId);
        return ResponseEntity.ok(assembler.toResource(bookings));
    }

    @Operation(summary = "Listar reservas de mis vehículos",
            description = "Permite al proveedor revisar las reservas asociadas a su flota registrada en la plataforma.")
    @GetMapping("/own")
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    public ResponseEntity<List<BookingResource>> own() {
        Long providerId = currentUserId();
        var bookings = bookingQueryService.getProviderBookings(providerId);
        return ResponseEntity.ok(assembler.toResource(bookings));
    }

    private Long currentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(authentication.getName());
    }
}
