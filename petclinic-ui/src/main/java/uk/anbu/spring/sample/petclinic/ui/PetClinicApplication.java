package uk.anbu.spring.sample.petclinic.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import uk.anbu.spring.sample.petclinic.service.PetClinicService;
import uk.anbu.spring.sample.petclinic.service.PetClinicServiceContext;
import uk.anbu.spring.sample.petclinic.lib.DefaultGlobalUtcClock;
import uk.anbu.spring.sample.petclinic.service.db.dao.OwnerDao;
import uk.anbu.spring.sample.petclinic.service.db.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.service.db.repository.VetRepository;

import javax.sql.DataSource;

@SpringBootApplication
@ImportRuntimeHints(PetClinicRuntimeHints.class)
public class PetClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	@Bean
	PetClinicService petClinicService(DataSource dataSource) {
		return new PetClinicService(PetClinicServiceContext.Config.builder()
			.dataSource(dataSource)
			.clock(new DefaultGlobalUtcClock())
			.build());
	}

	@Bean
	OwnerRepository ownerRepository(PetClinicService petClinicService) {
		// TODO: Remove this
		return petClinicService.getBean(OwnerRepository.class);
	}

	@Bean
	VetRepository vetRepository(PetClinicService petClinicService) {
		// TODO: Remove this
		return petClinicService.getBean(VetRepository.class);
	}

	@Bean
	OwnerDao ownerDao(PetClinicService petClinicService) {
		// TODO: Remove this
		return petClinicService.getBean(OwnerDao.class);
	}
}
