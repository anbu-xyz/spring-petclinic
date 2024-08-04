package uk.anbu.spring.sample.petclinic.service;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.anbu.spring.sample.petclinic.dto.NewOwnerDto;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.lib.PropertySourceBuilder;
import uk.anbu.spring.sample.petclinic.service.internal.PetClinicServiceContext;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;

import javax.sql.DataSource;
import java.util.List;

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

	public Integer registerNewOwner(NewOwnerDto dto) {
		var x = OwnerEntity.builder()
			.firstName(dto.getFirstName())
			.lastName(dto.getLastName())
			.city(dto.getCity())
			.address(dto.getAddress())
			.telephone(dto.getTelephone())
			.updateTimestampUtc(clock().sqlTimestamp())
			.pets(List.of())
			.build();
		var savedEntity = petClinicContext.getBean(OwnerRepository.class).save(x);
		return savedEntity.getEid();
	}
}
