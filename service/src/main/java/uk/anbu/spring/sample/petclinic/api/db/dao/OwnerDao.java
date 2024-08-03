package uk.anbu.spring.sample.petclinic.api.db.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.api.db.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.api.db.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.model.Owner;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OwnerDao {
	private final OwnerRepository ownerRepository;
	private final GlobalUtcClock clock;

	@Transactional
	public OwnerEntity register(Owner newOwner) {
		var currentTime = java.sql.Timestamp.valueOf(clock.currentTimestamp());

		var ownerToSave = OwnerEntity.builder()
			.firstName(newOwner.firstName())
			.lastName(newOwner.lastName())
			.address(newOwner.address())
			.city(newOwner.city())
			.telephone(newOwner.telephone())
			.updateTimestampUtc(currentTime)
			.pets(List.of())
			.build();

		 var saved = ownerRepository.save(ownerToSave);

		 log.info("New owner id: {}", saved.getEid());

		 return saved;
	}

	@Transactional
	public Optional<OwnerEntity> findOwnerById(Integer id) {
		return ownerRepository.findById(id);
	}

}
