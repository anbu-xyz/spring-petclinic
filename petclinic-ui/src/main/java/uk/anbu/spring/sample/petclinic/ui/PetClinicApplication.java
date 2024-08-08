package uk.anbu.spring.sample.petclinic.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import uk.anbu.spring.sample.petclinic.service.PetClinicServiceFacade;
import uk.anbu.spring.sample.petclinic.service.internal.PetClinicServiceContext;
import uk.anbu.spring.sample.petclinic.lib.DefaultGlobalUtcClock;
import uk.anbu.spring.sample.petclinic.service.internal.dao.OwnerDao;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.service.internal.repository.VetRepository;

import javax.sql.DataSource;

@SpringBootApplication
@ImportRuntimeHints(PetClinicRuntimeHints.class)
public class PetClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	@Bean
	PetClinicServiceFacade petClinicService(DataSource dataSource) {
		return new PetClinicServiceFacade(PetClinicServiceContext.Config.builder()
			.dataSource(dataSource)
			.clock(new DefaultGlobalUtcClock())
			.build());
	}

	@Bean
	OwnerRepository ownerRepository(PetClinicServiceFacade petClinicService) {
		// TODO: Remove this
		return petClinicService.getBean(OwnerRepository.class);
	}

	@Bean
	VetRepository vetRepository(PetClinicServiceFacade petClinicService) {
		// TODO: Remove this
		return petClinicService.getBean(VetRepository.class);
	}

	@Bean
	OwnerDao ownerDao(PetClinicServiceFacade petClinicService) {
		// TODO: Remove this
		return petClinicService.getBean(OwnerDao.class);
	}
}
