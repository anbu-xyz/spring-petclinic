package uk.anbu.spring.sample.petclinic.service.internal.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.service.internal.entity.PetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.PetRepository;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.model.Pet;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PetDao {
	private final PetRepository petRepository;
	private final GlobalUtcClock clock;

	@Transactional
	public PetEntity add(Pet newPet) {
		var currentTime = java.sql.Timestamp.valueOf(clock.currentTimestamp());

		var petToSave = PetEntity.builder()
			.name(newPet.name())
			.birthDate(newPet.birthDate())
			.type(newPet.type().code())
			.updateTimestampUtc(currentTime)
			.build();

		var saved = petRepository.save(petToSave);

		log.info("New pet id: {}", saved.getEid());

		return saved;
	}

	@Transactional
	public Optional<PetEntity> findById(Integer id) {
		return petRepository.findById(id);
	}
}
