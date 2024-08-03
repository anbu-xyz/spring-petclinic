package uk.anbu.spring.sample.petclinic.api.db.repository;

import org.springframework.data.repository.CrudRepository;
import uk.anbu.spring.sample.petclinic.api.db.entity.VisitEntity;

public interface VisitRepository extends CrudRepository<VisitEntity, Integer> {

}
