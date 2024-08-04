package uk.anbu.spring.sample.petclinic.service.internal.repository;

import org.springframework.data.repository.CrudRepository;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VisitEntity;

public interface VisitRepository extends CrudRepository<VisitEntity, Integer> {

}
