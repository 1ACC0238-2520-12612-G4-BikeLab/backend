package pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pe.upc.ridera.bikelab.providing.domain.model.aggregates.Provider;
import pe.upc.ridera.bikelab.providing.domain.model.valueobjects.ProviderStatus;
import pe.upc.ridera.bikelab.providing.domain.persistence.ProviderRepository;
import pe.upc.ridera.bikelab.providing.domain.persistence.criteria.ProviderSearchCriteria;
import pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.entities.ProviderEntity;
import pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.mappers.ProviderJpaMapper;
import pe.upc.ridera.bikelab.providing.infrastructure.persistence.jpa.repositories.SpringDataProviderRepository;

@Repository
public class JpaProviderRepository implements ProviderRepository {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "createdAt");

    private final SpringDataProviderRepository repository;
    private final ProviderJpaMapper mapper;

    public JpaProviderRepository(SpringDataProviderRepository repository, ProviderJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Provider save(Provider provider) {
        ProviderEntity entity = mapper.toEntity(provider);
        ProviderEntity saved = repository.save(entity);
        return mapper.toAggregate(saved);
    }

    @Override
    public Optional<Provider> findById(UUID providerId) {
        return repository.findById(providerId)
                .map(mapper::toAggregate);
    }

    @Override
    public Optional<Provider> findByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .map(mapper::toAggregate);
    }

    @Override
    public List<Provider> findAll(ProviderSearchCriteria criteria) {
        Specification<ProviderEntity> specification = Specification.allOf();
        if (criteria.status().isPresent()) {
            specification = specification.and((root, query, cb) -> cb.equal(root.get("status"),
                    criteria.status().get()));
        }
        if (criteria.search().isPresent()) {
            String term = "%" + criteria.search().get().trim().toLowerCase() + "%";
            specification = specification.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("displayName")), term),
                    cb.like(cb.lower(root.get("docNumber")), term),
                    cb.like(cb.lower(root.get("phone")), term)));
        }
        return repository.findAll(specification, DEFAULT_SORT).stream()
                .map(mapper::toAggregate)
                .toList();
    }

    @Override
    public long countByStatus(ProviderStatus status) {
        return repository.countByStatus(status);
    }
}
