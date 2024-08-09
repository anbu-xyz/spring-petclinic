package uk.anbu.spring.sample.petclinic.service.internal.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VisitEntity;

import java.util.List;
import java.util.Optional;

public interface VisitRepository extends CrudRepository<VisitEntity, Integer> {

	@Query("SELECT v FROM VisitEntity v where v.petId =:petId")
	@Transactional(readOnly = true)
	List<VisitEntity> findByPetId(@Param("petId") Integer petId);
}
