package pe.upc.ridera.bikelab.vehicles.infrastructure.persistence.jpa.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pe.upc.ridera.bikelab.vehicles.infrastructure.persistence.jpa.entities.VehicleEntity;

/**
 * Esta interfaz expone consultas Spring Data JPA para la tabla de vehículos.
 */ 
public interface VehicleJpaRepository extends JpaRepository<VehicleEntity, UUID> {

    List<VehicleEntity> findByOwnerId(UUID ownerId);

    @Query("select v from VehicleEntity v where v.latitude between :minLat and :maxLat and v.longitude between :minLng and :maxLng")
    List<VehicleEntity> searchByBoundingBox(@Param("minLat") double minLat, @Param("maxLat") double maxLat,
            @Param("minLng") double minLng, @Param("maxLng") double maxLng);
}
