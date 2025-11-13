package pe.upc.ridera.bikelab.metrics.interfaces.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.upc.ridera.bikelab.metrics.application.dto.MetricsResource;
import pe.upc.ridera.bikelab.metrics.application.services.MetricsQueryService;

/**
 * Controlador público que entrega el resumen de métricas para tableros del frontend.
 */
@RestController
@RequestMapping("/api/metrics")
@Tag(name = "BC: Metrics")
public class MetricsController {

    private final MetricsQueryService metricsQueryService;

    public MetricsController(MetricsQueryService metricsQueryService) {
        this.metricsQueryService = metricsQueryService;
    }

    @Operation(summary = "Obtener métricas globales", description = "Devuelve contadores agregados de usuarios, proveedores, vehículos, reservas y pagos.")
    @GetMapping("/overview")
    public MetricsResource overview() {
        return metricsQueryService.getOverview();
    }
}
