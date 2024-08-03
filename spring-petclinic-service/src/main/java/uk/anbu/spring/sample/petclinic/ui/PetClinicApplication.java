/*
 * Copyright 2012-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.anbu.spring.sample.petclinic.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportRuntimeHints;
import uk.anbu.spring.sample.petclinic.api.PetClinicContext;
import uk.anbu.spring.sample.petclinic.api.PetClinicService;
import uk.anbu.spring.sample.petclinic.api.db.repository.OwnerRepository;

import javax.sql.DataSource;

@SpringBootApplication
@ImportRuntimeHints(PetClinicRuntimeHints.class)
public class PetClinicApplication {

	public static void main(String[] args) {
		SpringApplication.run(PetClinicApplication.class, args);
	}

	@Bean
	PetClinicService petClinicService(DataSource dataSource) {
		return new PetClinicService(PetClinicContext.Config.builder()
			.dataSource(dataSource)
			.build());
	}

	@Bean
	OwnerRepository ownerRepository(PetClinicService petClinicService) {
		return petClinicService.getBean(OwnerRepository.class);
	}
}
