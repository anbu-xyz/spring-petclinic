package uk.anbu.spring.sample.petclinic.api.db.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.api.db.entity.VisitEntity;
import uk.anbu.spring.sample.petclinic.api.db.repository.VisitRepository;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.model.Visit;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class VisitDao {
	private final VisitRepository visitRepository;
	private final GlobalUtcClock clock;

	@Transactional
	public VisitEntity add(Visit newVisit) {
		var currentTime = java.sql.Timestamp.valueOf(clock.currentTimestamp());

		var vetToSave = VisitEntity.builder()
			.vetId(newVisit.vetId())
			.petId(newVisit.petId())
			.date(newVisit.visitDate())
			.description(newVisit.description())
			.updateTimestampUtc(currentTime)
			.build();

		var saved = visitRepository.save(vetToSave);
		log.info("New visit id: {}", saved.getEid());

		return saved;
	}

	@Transactional
	public Optional<VisitEntity> findById(Integer id) {
		return visitRepository.findById(id);
	}
}
