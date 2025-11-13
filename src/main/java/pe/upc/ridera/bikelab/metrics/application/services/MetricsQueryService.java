package pe.upc.ridera.bikelab.metrics.application.services;

import pe.upc.ridera.bikelab.metrics.application.dto.MetricsResource;

/**
 * Caso de uso de lectura que entrega las métricas generales del sistema.
 */
public interface MetricsQueryService {

    MetricsResource getOverview();
}
