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

    @Query("SELECT DISTINCT owner FROM OwnerEntity owner left join fetch PetEntity pet ON pet.ownerId = owner.eid WHERE owner.lastName LIKE :lastName% ")
    @Transactional(readOnly = true)
    Page<OwnerEntity> findByLastName(@Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT owner FROM OwnerEntity owner")
    @Transactional(readOnly = true)
    Page<OwnerEntity> findAll(Pageable pageable);

}
