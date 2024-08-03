package uk.anbu.spring.sample.petclinic.api.db.entity;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface VetRepository extends CrudRepository<VetEntity, Integer> {

   @Transactional(readOnly = true)
    @Cacheable("vets")
    Collection<VetEntity> findAll() throws DataAccessException;

    @Transactional(readOnly = true)
    @Cacheable("vets")
    Page<VetEntity> findAll(Pageable pageable) throws DataAccessException;

}
