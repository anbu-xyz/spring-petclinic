package uk.anbu.spring.sample.petclinic

import spock.lang.Shared
import spock.lang.Specification
import uk.anbu.spring.sample.petclinic.api.PetClinicService
import uk.anbu.spring.sample.petclinic.api.PetClinicServiceContext
import uk.anbu.spring.sample.petclinic.api.db.dao.OwnerDao
import uk.anbu.spring.sample.petclinic.api.db.dao.PetDao
import uk.anbu.spring.sample.petclinic.api.db.dao.VetDao
import uk.anbu.spring.sample.petclinic.api.db.entity.OwnerEntity
import uk.anbu.spring.sample.petclinic.lib.ResettableGlobalUtcClock
import uk.anbu.spring.sample.petclinic.lib.db.DataSourceWrapper
import uk.anbu.spring.sample.petclinic.model.Owner
import uk.anbu.spring.sample.petclinic.model.Pet
import uk.anbu.spring.sample.petclinic.model.Vet
import uk.anbu.spring.sample.petclinic.testutil.DataSourceUtil

import static uk.anbu.spring.sample.petclinic.model.Vet.Type.*

import java.time.LocalDate
import java.time.LocalTime

class DaoSpecification extends Specification {
    @Shared
    static PetClinicService service;
    @Shared
    static OwnerEntity testOwner;

    def setupSpec() {
        def config = PetClinicServiceContext.Config.builder()
                .dataSource(new DataSourceWrapper(DataSourceUtil.dataSource()))
                .clock(new ResettableGlobalUtcClock(
                        LocalDate.of(2020, 3, 4),
                        LocalTime.of(10, 10)))
                .build()
        service = new PetClinicService(config)
    }

    def "Able to save and retrieve owner"() {
        given:
        def dao = service.getBean(OwnerDao)
        def owner = Owner.builder()
                .firstName("first")
                .lastName("last")
                .address("1 honey blvd")
                .city("angel city")
                .telephone("1234567890")
                .build()

        when:
        def saved = dao.register(owner)
        def read = dao.findOwnerById(saved.eid)
        testOwner = read.get()

        then:
        read.isPresent()
        CompareUtil.compareObjects(read.get(), saved, ["eid", "updateTimestampUtc"])

    }

    def "Able to save and retrieve pet"() {
        given:
        def dao = service.getBean(PetDao)
        def pet = Pet.builder()
                .ownerId(testOwner.eid)
                .birthDate(LocalDate.of(2020, 3, 4))
                .name("pet name")
                .type(Pet.PetType.of("cat"))
                .build()

        when:
        def saved = dao.add(pet)
        def read = dao.findById(saved.eid)

        then:
        read.isPresent()
        CompareUtil.compareObjects(read.get(), saved, ["eid", "updateTimestampUtc"])
    }

    def "Able to save and retrieve vet"() {
        given:
        def dao = service.getBean(VetDao)
        def vet = Vet.builder()
                .firstName("vet first")
                .lastName("vet last")
                .vetRegistrationId("RN-101")
                .specialty([RADIOLOGY, SURGERY]*.value())
                .build()

        when:
        def saved = dao.add(vet)
        def read = dao.findById(saved.eid)

        then:
        read.isPresent()
        CompareUtil.compareObjects(read.get(), saved, ["eid", "updateTimestampUtc"])
    }
}
