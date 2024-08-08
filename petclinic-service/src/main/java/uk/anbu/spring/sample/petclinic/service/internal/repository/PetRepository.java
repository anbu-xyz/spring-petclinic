package uk.anbu.spring.sample.petclinic.service.internal.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.service.internal.entity.PetEntity;

import java.util.List;

public interface PetRepository extends CrudRepository<PetEntity, Integer> {

	@Query("SELECT p FROM PetEntity p where upper(p.name) = upper(:name) and p.ownerId =:id")
	@Transactional(readOnly = true)
	List<PetEntity> findByPetName(@Param("id") Integer ownerId, @Param("name") String petName);

	@Query("SELECT p FROM PetEntity p where p.ownerId =:id")
	@Transactional(readOnly = true)
	List<PetEntity> findByOwnerId(@Param("id") Integer id);
}
