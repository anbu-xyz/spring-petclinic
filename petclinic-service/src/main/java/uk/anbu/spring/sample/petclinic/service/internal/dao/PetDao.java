package uk.anbu.spring.sample.petclinic.service.internal.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.service.internal.entity.PetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VisitEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.PetRepository;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.service.internal.repository.VisitRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PetDao {
	private final PetRepository petRepository;
	private final VisitRepository visitRepository;
	private final GlobalUtcClock clock;

	@Transactional
	public PetEntity add(Pet newPet) {
		var currentTime = java.sql.Timestamp.valueOf(clock.currentTimestamp());

		var petToSave = PetEntity.builder()
			.name(newPet.name())
			.birthDate(newPet.birthDate())
			.type(newPet.type().code())
			.ownerId(newPet.ownerId())
			.updateTimestampUtc(currentTime)
			.build();

		var saved = petRepository.save(petToSave);

		log.info("New pet id: {}", saved.getEid());

		return saved;
	}

	@Transactional
	public Optional<PetEntity> findById(Integer petId) {
		var visits = visitRepository.findByPetId(petId);
		var petEntity = petRepository.findById(petId);
		petEntity.ifPresent(p -> p.setVisits(visits));
		return petEntity;
	}
}
