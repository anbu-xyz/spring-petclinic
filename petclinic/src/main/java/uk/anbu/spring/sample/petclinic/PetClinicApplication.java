package uk.anbu.spring.sample.petclinic;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.context.annotation.Scope;
import uk.anbu.spring.sample.petclinic.api.PetClinicService;
import uk.anbu.spring.sample.petclinic.api.PetClinicServiceContext;
import uk.anbu.spring.sample.petclinic.api.db.dao.OwnerDao;
import uk.anbu.spring.sample.petclinic.api.db.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.lib.DefaultGlobalUtcClock;
import uk.anbu.spring.sample.petclinic.ui.PetClinicRuntimeHints;

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
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	OwnerRepository ownerRepository(PetClinicService petClinicService) {
		return petClinicService.getBean(OwnerRepository.class);
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	OwnerDao ownerDao(PetClinicService petClinicService) {
		return petClinicService.getBean(OwnerDao.class);
	}
}
