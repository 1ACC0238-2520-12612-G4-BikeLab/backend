package pe.upc.ridera.bikelab.renting.infrastructure.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "renting.vehicles")
/**
 * Esta clase agrupa las propiedades de configuración para comunicarse con el catálogo de vehículos.
 */
public class VehicleCatalogProperties {

    private List<VehicleSeed> seeds = new ArrayList<>();

    public List<VehicleSeed> getSeeds() {
        return seeds;
    }

    public void setSeeds(List<VehicleSeed> seeds) {
        this.seeds = seeds;
    }

    public static class VehicleSeed {
        private UUID id;
        private Long providerId;
        private BigDecimal hourlyRate = BigDecimal.TEN;

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public Long getProviderId() {
            return providerId;
        }

        public void setProviderId(Long providerId) {
            this.providerId = providerId;
        }

        public BigDecimal getHourlyRate() {
            return hourlyRate;
        }

        public void setHourlyRate(BigDecimal hourlyRate) {
            this.hourlyRate = hourlyRate;
        }
    }
}
