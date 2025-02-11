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
import uk.anbu.spring.sample.petclinic.service.internal.entity.VisitEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VetEntity;
import uk.anbu.spring.sample.petclinic.service.internal.repository.PetRepository;
import uk.anbu.spring.sample.petclinic.service.internal.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.ui.system.controller.PetController;

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

	@Autowired
	protected PetRepository pets;

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
    @Transactional
	@Sql("classpath:db/h2/data.sql")
    void shouldInsertPetIntoDatabaseAndGenerateId() {
        OwnerEntity owner6 = this.owners.findById(6).get();
        int found = owner6.getPets().size();

        PetEntity pet = new PetEntity();
        pet.setName("bowser");
        Collection<Pet.PetType> types = PetController.populatePetTypes();
        pet.setType(Pet.PetType.of("dog"));
        pet.setBirthDate(LocalDate.now());
//        owner6.addPet(pet);
        assertThat(owner6.getPets()).hasSize(found + 1);

        this.owners.save(owner6);

        owner6 = this.owners.findById(6).get();
        assertThat(owner6.getPets()).hasSize(found + 1);
        // checks that id has been generated
        pet = pets.findById(3).get();
        assertThat(pet.getEid()).isNotNull();
    }

    @Test
    @Transactional
	@Sql("classpath:db/h2/data.sql")
    void shouldUpdatePetName() {
        OwnerEntity owner6 = this.owners.findById(6).get();
        PetEntity pet7 = pets.findById(7).get();
        String oldName = pet7.getName();

        String newName = oldName + "X";
        pet7.setName(newName);
        this.owners.save(owner6);

        owner6 = this.owners.findById(6).get();
        pet7 = pets.findById(7).get();
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
        PetEntity pet7 = pets.findById(7).get();
        int found = pet7.getVisits().size();
        VisitEntity visit = new VisitEntity();
    	visit.setDescription("test");

//		owner6.addVisit(pet7.getEid(), visit);
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
		PetEntity pet7 = pets.findById(7).get();
		Collection<VisitEntity> visits = pet7.getVisits();

		assertThat(visits) //
			.hasSize(2) //
			.element(0)
			.extracting(VisitEntity::getVisitDate)
			.isNotNull();
	}

}
