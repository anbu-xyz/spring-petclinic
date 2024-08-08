package uk.anbu.spring.sample.petclinic.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import uk.anbu.spring.sample.petclinic.dto.OwnerDto;
import uk.anbu.spring.sample.petclinic.dto.PetDto;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.lib.PropertySourceBuilder;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.service.internal.PetClinicServiceContext;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.PetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.service.internal.repository.PetRepository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PetClinicService {
	private final AnnotationConfigApplicationContext petClinicContext;

	public PetClinicService(PetClinicServiceContext.Config config) {
		this.petClinicContext = new AnnotationConfigApplicationContext();

		this.petClinicContext.registerBean(GlobalUtcClock.class, config::clock);
		this.petClinicContext.registerBean(DataSource.class, config::dataSource);

		this.petClinicContext.register(PetClinicServiceContext.class);

		this.petClinicContext.getEnvironment().getPropertySources()
			.addLast(PropertySourceBuilder.forService(PetClinicServiceContext.class));

		this.petClinicContext.addBeanFactoryPostProcessor(
			PropertySourceBuilder.propertyConfigurer(PetClinicServiceContext.class));

		this.petClinicContext.refresh();
	}

	public <T> T getBean(Class<T> clazz) {
		return petClinicContext.getBean(clazz);
	}

	public GlobalUtcClock clock() {
		return petClinicContext.getBean(GlobalUtcClock.class);
	}

	public Integer registerNewOwner(OwnerDto dto) {
		var entity = OwnerEntity.builder()
			.firstName(dto.getFirstName())
			.lastName(dto.getLastName())
			.city(dto.getCity())
			.address(dto.getAddress())
			.eid(null)
			.telephone(dto.getTelephone())
			.updateTimestampUtc(clock().sqlTimestamp())
			.pets(List.of())
			.build();
		var savedEntity = petClinicContext.getBean(OwnerRepository.class).save(entity);
		return savedEntity.getEid();
	}

	public void updateOwner(OwnerDto dto) {
		var entity = OwnerEntity.builder()
			.firstName(dto.getFirstName())
			.lastName(dto.getLastName())
			.city(dto.getCity())
			.address(dto.getAddress())
			.telephone(dto.getTelephone())
			.updateTimestampUtc(clock().sqlTimestamp())
			.eid(dto.getEid())
			.pets(List.of())
			.build();
		petClinicContext.getBean(OwnerRepository.class).save(entity);
	}

	public Page<OwnerDto> findOwnerByLastName(String lastname, Pageable pageable) {
		var result = petClinicContext.getBean(OwnerRepository.class).findByLastName(lastname, pageable);
		var dtoList = result.getContent().stream()
			.map(x -> OwnerDto.builder()
				.firstName(x.getFirstName())
				.lastName(x.getLastName())
				.address(x.getAddress())
				.telephone(x.getTelephone())
				.eid(x.getEid())
				.city(x.getCity())
				.pets(x.getPets().stream()
					.map(p -> PetDto.builder()
						.ownerId(p.getOwnerId())
						.name(p.getName())
						.birthDate(p.getBirthDate())
						.type(Pet.PetType.of(p.getType()))
						.build())
					.toList())
				.build())
			.toList();
		return new PageImpl<>(dtoList, pageable, result.getTotalPages());
	}

	public PetDto getPet(Integer ownerEid, String petName, boolean ignoreNew) {
//		if (ignoreNew) return null;
		var owner = petClinicContext.getBean(OwnerRepository.class).findById(ownerEid);
		if (owner.isEmpty()) {
			throw new IllegalArgumentException("Owner with id #" + ownerEid + " is not present");
		}
		var petEntity = petClinicContext.getBean(PetRepository.class).findByPetName(ownerEid, petName);

		if (petEntity.isEmpty()) return null;

		return PetDto.builder()
			.name(petEntity.get(0).getName())
			.type(Pet.PetType.of(petEntity.get(0).getType()))
			.ownerId(ownerEid)
			.birthDate(petEntity.get(0).getBirthDate())
			.eid(petEntity.get(0).getEid())
			.build();
	}

	public Integer registerNewPet(PetDto dto) {
		var entity = PetEntity.builder()
			.birthDate(dto.getBirthDate())
			.name(dto.getName())
			.type(dto.getType().code())
			.ownerId(dto.getOwnerId())
			.updateTimestampUtc(clock().sqlTimestamp())
			.build();
		var savedEntity = petClinicContext.getBean(PetRepository.class).save(entity);
		return savedEntity.getEid();
	}

	public LocalDate currentDate() {
		return clock().currentDate();
	}

	public Optional<Pet> findPet(int petId) {
		var entity = petClinicContext.getBean(PetRepository.class)
			.findById(petId);
		var pet = entity.map(e -> Pet.builder()
			.eid(e.getEid())
			.type(Pet.PetType.of(e.getType()))
			.ownerId(e.getOwnerId())
			.birthDate(e.getBirthDate())
			.name(e.getName())
			.build());

		return pet;
	}

	public void updatePet(Pet pet) {
		var entity = petClinicContext.getBean(PetRepository.class)
			.findById(pet.eid()).orElseThrow();

		var updatedPet = entity.toBuilder()
			.birthDate(pet.birthDate())
			.name(pet.name())
			.ownerId(pet.ownerId())
			.type(pet.type().toString())
			.build();
		petClinicContext.getBean(PetRepository.class).save(updatedPet);
	}
}
