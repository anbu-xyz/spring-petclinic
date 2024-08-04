package uk.anbu.spring.sample.petclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import uk.anbu.spring.sample.petclinic.service.PetClinicService;
import uk.anbu.spring.sample.petclinic.service.PetClinicServiceContext;
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
}
