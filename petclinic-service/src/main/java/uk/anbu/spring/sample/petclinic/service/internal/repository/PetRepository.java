package uk.anbu.spring.sample.petclinic.service.internal.repository;

import org.springframework.data.repository.CrudRepository;
import uk.anbu.spring.sample.petclinic.service.internal.entity.PetEntity;

public interface PetRepository extends CrudRepository<PetEntity, Integer> {
}
