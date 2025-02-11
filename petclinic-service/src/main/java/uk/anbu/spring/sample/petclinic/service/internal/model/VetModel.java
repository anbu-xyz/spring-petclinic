package uk.anbu.spring.sample.petclinic.service.internal.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.VetRepository;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.model.Vet;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class VetModel {
	private final VetRepository vetRepository;
	private final GlobalUtcClock clock;

	@Transactional
	public VetEntity add(Vet newVet) {
		var currentTime = java.sql.Timestamp.valueOf(clock.currentTimestamp());

		var specialties = newVet.specialty().stream().map(Vet.SpecialtyType::code).collect(Collectors.toSet());
		var vetToSave = VetEntity.builder()
			.firstName(newVet.firstName())
			.lastName(newVet.lastName())
			.licenseNumber(newVet.licenseNumber())
			.specialties(specialties)
			.updateTimestampUtc(currentTime)
			.build();

		var saved = vetRepository.save(vetToSave);
		log.info("New vet id: {}", saved.getEid());

		return saved;
	}

	@Transactional
	public Optional<VetEntity> findById(Integer id) {
		return vetRepository.findById(id);
	}
}
