package pe.upc.ridera.bikelab.renting.infrastructure.config;

import java.time.Clock;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(VehicleCatalogProperties.class)
/**
 * Esta clase configura los componentes principales del contexto de renting dentro de Spring.
 */
public class RentingConfiguration {

    @Bean
    public Clock systemClock() {
        return Clock.systemUTC();
    }
}
