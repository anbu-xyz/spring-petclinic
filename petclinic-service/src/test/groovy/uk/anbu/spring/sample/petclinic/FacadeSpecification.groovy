package uk.anbu.spring.sample.petclinic

import spock.lang.Shared
import spock.lang.Specification
import uk.anbu.spring.sample.petclinic.dto.OwnerDto
import uk.anbu.spring.sample.petclinic.dto.PetDto
import uk.anbu.spring.sample.petclinic.dto.VetDto
import uk.anbu.spring.sample.petclinic.dto.VisitDto
import uk.anbu.spring.sample.petclinic.lib.ResettableGlobalUtcClock
import uk.anbu.spring.sample.petclinic.lib.db.DataSourceWrapper
import uk.anbu.spring.sample.petclinic.model.Pet
import uk.anbu.spring.sample.petclinic.model.Vet
import uk.anbu.spring.sample.petclinic.service.PetClinicServiceFacade
import uk.anbu.spring.sample.petclinic.service.internal.PetClinicServiceContext
import uk.anbu.spring.sample.petclinic.testutil.DataSourceUtil

import java.time.LocalDate
import java.time.LocalTime

import static uk.anbu.spring.sample.petclinic.model.Vet.Type.RADIOLOGY
import static uk.anbu.spring.sample.petclinic.model.Vet.Type.SURGERY

class FacadeSpecification extends Specification {
    @Shared
    PetClinicServiceFacade facade

    @Shared
    OwnerDto testOwner
    @Shared
    PetDto testPet
    @Shared
    VetDto testVet

    def setupSpec() {
        def config = PetClinicServiceContext.Config.builder()
                .dataSource(new DataSourceWrapper(DataSourceUtil.dataSource()))
                .clock(new ResettableGlobalUtcClock(
                        LocalDate.of(2020, 3, 4),
                        LocalTime.of(10, 10)))
                .build()
        facade = new PetClinicServiceFacade(config)
    }

    def "Able to save and retrieve owner"() {
        given:
        def owner = OwnerDto.builder()
                .firstName("first")
                .lastName("last")
                .address("1 honey blvd")
                .city("angel city")
                .telephone("1234567890")
                .build()
        Integer ownerEid = facade.registerNewOwner(owner)

        when:
        def readBack = facade.findOwnerById(ownerEid).get()
        testOwner = readBack

        then:
        CompareUtil.compareObjects(owner, readBack, ["eid", "updateTimestampUtc"])

    }

    def "Able to save and retrieve pet"() {
        given:
        def pet = PetDto.builder()
                .ownerId(testOwner.eid)
                .birthDate(LocalDate.of(2020, 3, 4))
                .name("pet name")
                .type(Pet.PetType.of("cat"))
                .build()
        def petId = facade.registerNewPet(pet)

        when:
        def readBack = facade.findPetById(petId).get()
        testPet = readBack

        then:
        CompareUtil.compareObjects(pet, readBack, ["eid", "updateTimestampUtc"])
    }

    def "Able to save and retrieve vet"() {
        given:
        def vet = VetDto.builder()
                .firstName("vet first")
                .lastName("vet last")
                .registrationId("RN-101")
                .specialities([RADIOLOGY, SURGERY].collect{Vet.SpecialtyType.of(it)}.toSet())
                .build()
        def vetId = facade.registerVet(vet)

        when:
        def readBack = facade.findVetById(vetId).get()
        testVet = readBack

        then:
        CompareUtil.compareObjects(vet, readBack, ["eid", "updateTimestampUtc"])
    }

    def "Able to save and retrieve visits"() {
        given:
        def visit = VisitDto.builder()
                .vetId(testVet.eid)
                .petId(testPet.eid)
                .visitDate(LocalDate.of(2020, 5, 7))
                .description("my pet was sick")
                .build()
        def visitId = facade.registerVisit(visit)

        when:
        def readBack = facade.findVisitById(visitId).get()

        then:
        CompareUtil.compareObjects(visit, readBack, ["eid", "updateTimestampUtc"])
    }
}
