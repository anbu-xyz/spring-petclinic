package uk.anbu.spring.sample.petclinic.service.db.repository;

import org.springframework.data.repository.CrudRepository;
import uk.anbu.spring.sample.petclinic.service.db.entity.PetEntity;

public interface PetRepository extends CrudRepository<PetEntity, Integer> {
}
