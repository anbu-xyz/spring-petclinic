package uk.anbu.spring.sample.petclinic.service.internal.model;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.dto.OwnerDto;
import uk.anbu.spring.sample.petclinic.dto.PetDto;
import uk.anbu.spring.sample.petclinic.dto.VisitDto;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.model.Owner;
import uk.anbu.spring.sample.petclinic.service.internal.repository.PetRepository;
import uk.anbu.spring.sample.petclinic.service.internal.repository.VisitRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class OwnerModel {
	private final OwnerRepository ownerRepository;
	private final PetRepository petRepository;
	private final VisitRepository visitRepository;
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

	public Page<OwnerDto> findOwnerByLastName(String lastname, Pageable pageable) {
		var result = ownerRepository.findByLastName(lastname, pageable);
		var dtoList = result.getContent().stream()
			.map(OwnerModel::toDto)
			.toList();
		return new PageImpl<>(dtoList, pageable, result.getTotalPages());
	}

	private static OwnerDto toDto(OwnerEntity x) {
		return OwnerDto.builder()
			.firstName(x.getFirstName())
			.lastName(x.getLastName())
			.address(x.getAddress())
			.telephone(x.getTelephone())
			.eid(x.getEid())
			.city(x.getCity())
			.pets(x.getPets().stream()
				.map(p -> PetDto.builder()
					.name(p.getName())
					.eid(p.getEid())
					.birthDate(p.getBirthDate())
					.ownerId(p.getOwnerId())
					.type(Pet.PetType.of(p.getType()))
					.visits(p.getVisits().stream().map(v -> VisitDto.builder()
						.petId(v.getPetId())
						.vetId(v.getVetId())
						.description(v.getDescription())
						.visitDate(v.getVisitDate())
						.eid(v.getEid())
						.build()).toList())
					.build())
				.toList())
			.build();
	}

	@Transactional
	public Optional<OwnerDto> findOwnerById(Integer id) {
		var owner = ownerRepository.findById(id);
		owner.ifPresent(ownerEntity -> ownerEntity.setPets(petRepository.findByOwnerId(id)));
		var pets = owner.map(OwnerEntity::getPets);
		pets.ifPresent(petEntities ->
			petEntities.forEach(pet -> pet.setVisits(visitRepository.findByPetId(pet.getEid())))
		);

		return owner.map(OwnerModel::toDto);
	}

}
