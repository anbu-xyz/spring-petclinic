package uk.anbu.spring.sample.petclinic.service.internal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;

public interface OwnerRepository extends CrudRepository<OwnerEntity, Integer> {

    @Query("SELECT DISTINCT owner FROM OwnerEntity owner left join  owner.pets WHERE owner.lastName LIKE :lastName% ")
    @Transactional(readOnly = true)
    Page<OwnerEntity> findByLastName(@Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT owner FROM OwnerEntity owner left join fetch owner.pets WHERE owner.eid =:id")
    @Transactional(readOnly = true)
	@Override
	Optional<OwnerEntity> findById(@Param("id") Integer id);

    @Query("SELECT owner FROM OwnerEntity owner")
    @Transactional(readOnly = true)
    Page<OwnerEntity> findAll(Pageable pageable);

}
