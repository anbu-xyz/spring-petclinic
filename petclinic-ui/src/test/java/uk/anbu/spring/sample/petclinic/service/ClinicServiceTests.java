/*
 * Copyright 2012-2019 the original author or authors.
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

package uk.anbu.spring.sample.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.anbu.spring.sample.petclinic.service.internal.entity.OwnerEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.service.internal.entity.PetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.PetTypeEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VisitEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.model.Pet;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following services provided
 * by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary set up
 * time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning that we
 * don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code> </code> instance variable, which uses
 * autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is executed in
 * its own transaction, which is automatically rolled back by default. Thus, even if tests
 * insert or otherwise change database state, there is no need for a teardown or cleanup
 * script.
 * <li>An {@link org.springframework.context.ApplicationContext ApplicationContext} is
 * also inherited and can be used for explicit bean lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
// Ensure that if the mysql profile is active we connect to the real database:
@AutoConfigureTestDatabase(replace = Replace.NONE)
// @TestPropertySource("/application-postgres.properties")
@Disabled
class ClinicServiceTests {

    @Autowired
    protected OwnerRepository owners;

    @Autowired
    protected VetRepository vets;

    Pageable pageable;

    @Test
	@Sql("classpath:db/h2/data.sql")
    void shouldFindOwnersByLastName() {
        Page<OwnerEntity> owners = this.owners.findByLastName("Davis", pageable);
        assertThat(owners).hasSize(2);

        owners = this.owners.findByLastName("Daviss", pageable);
        assertThat(owners).isEmpty();
    }

    @Test
	@Sql("classpath:db/h2/data.sql")
    void shouldFindSingleOwnerWithPet() {
        OwnerEntity owner = this.owners.findById(1).get();
        assertThat(owner.getLastName()).startsWith("Franklin");
        assertThat(owner.getPets()).hasSize(1);
        assertThat(owner.getPets().get(0).getType()).isNotNull();
        assertThat(owner.getPets().get(0).getType()).isEqualTo("cat");
    }

    @Test
    @Transactional
	@Sql("classpath:db/h2/data.sql")
    void shouldInsertOwner() {
        Page<OwnerEntity> owners = this.owners.findByLastName("Schultz", pageable);
        int found = (int) owners.getTotalElements();

        OwnerEntity owner = new OwnerEntity();
        owner.setFirstName("Sam");
        owner.setLastName("Schultz");
        owner.setAddress("4, Evans Street");
        owner.setCity("Wollongong");
        owner.setTelephone("4444444444");
        this.owners.save(owner);
        assertThat(owner.getEid()).isNotZero();

        owners = this.owners.findByLastName("Schultz", pageable);
        assertThat(owners.getTotalElements()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
	@Sql("classpath:db/h2/data.sql")
    void shouldUpdateOwner() {
        OwnerEntity owner = this.owners.findById(1).get();
        String oldLastName = owner.getLastName();
        String newLastName = oldLastName + "X";

        owner.setLastName(newLastName);
        this.owners.save(owner);

        // retrieving new name from database
        owner = this.owners.findById(1).get();
        assertThat(owner.getLastName()).isEqualTo(newLastName);
    }

    @Test
	@Sql("classpath:db/h2/data.sql")
    void shouldFindAllPetTypes() {
        Collection<PetTypeEntity> petTypes = this.owners.findPetTypes();

        PetTypeEntity petType1 = EntityUtils.getById(petTypes, PetTypeEntity.class, 1);
        assertThat(petType1.getName()).isEqualTo("cat");
        PetTypeEntity petType4 = EntityUtils.getById(petTypes, PetTypeEntity.class, 4);
        assertThat(petType4.getName()).isEqualTo("snake");
    }

    @Test
    @Transactional
	@Sql("classpath:db/h2/data.sql")
    void shouldInsertPetIntoDatabaseAndGenerateId() {
        OwnerEntity owner6 = this.owners.findById(6).get();
        int found = owner6.getPets().size();

        PetEntity pet = new PetEntity();
        pet.setName("bowser");
        Collection<PetTypeEntity> types = this.owners.findPetTypes();
        pet.setType(Pet.PetType.of("dog"));
        pet.setBirthDate(LocalDate.now());
        owner6.addPet(pet);
        assertThat(owner6.getPets()).hasSize(found + 1);

        this.owners.save(owner6);

        owner6 = this.owners.findById(6).get();
        assertThat(owner6.getPets()).hasSize(found + 1);
        // checks that id has been generated
        pet = owner6.getPet(3);
        assertThat(pet.getEid()).isNotNull();
    }

    @Test
    @Transactional
	@Sql("classpath:db/h2/data.sql")
    void shouldUpdatePetName() {
        OwnerEntity owner6 = this.owners.findById(6).get();
        PetEntity pet7 = owner6.getPet(7);
        String oldName = pet7.getName();

        String newName = oldName + "X";
        pet7.setName(newName);
        this.owners.save(owner6);

        owner6 = this.owners.findById(6).get();
        pet7 = owner6.getPet(7);
        assertThat(pet7.getName()).isEqualTo(newName);
    }

    @Test
	@Sql("classpath:db/h2/data.sql")
	void shouldFindVets() {
        Collection<VetEntity> vets = this.vets.findAll();

        VetEntity vet = EntityUtils.getById(vets, VetEntity.class, 3);
        assertThat(vet.getLastName()).isEqualTo("Douglas");
        assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
        assertThat(vet.getSpecialties()).containsAll(List.of("dentistry"));
    }

    @Test
    @Transactional
	@Sql("classpath:db/h2/data.sql")
    void shouldAddNewVisitForPet() {
        OwnerEntity owner6 = this.owners.findById(6).get();
        PetEntity pet7 = owner6.getPet(7);
        int found = pet7.getVisits().size();
        VisitEntity visit = new VisitEntity();
    	visit.setDescription("test");

		owner6.addVisit(pet7.getEid(), visit);
		this.owners.save(owner6);

		owner6 = this.owners.findById(6).get();

		assertThat(pet7.getVisits()) //
			.hasSize(found + 1) //
			.allMatch(value -> value.getEid() != null);
	}

	@Test
	@Sql("classpath:db/h2/data.sql")
	void shouldFindVisitsByPetId() {
		OwnerEntity owner6 = this.owners.findById(6).get();
		PetEntity pet7 = owner6.getPet(7);
		Collection<VisitEntity> visits = pet7.getVisits();

		assertThat(visits) //
			.hasSize(2) //
			.element(0)
			.extracting(VisitEntity::getDate)
			.isNotNull();
	}

}
