package uk.anbu.spring.sample.petclinic.api.db.repository;

import org.springframework.data.repository.CrudRepository;
import uk.anbu.spring.sample.petclinic.api.db.entity.PetEntity;

public interface PetRepository extends CrudRepository<PetEntity, Integer> {
}
