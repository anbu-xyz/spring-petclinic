package uk.anbu.spring.sample.petclinic

import spock.lang.Shared
import spock.lang.Specification
import uk.anbu.spring.sample.petclinic.api.PetClinicService
import uk.anbu.spring.sample.petclinic.api.PetClinicServiceContext
import uk.anbu.spring.sample.petclinic.api.db.dao.OwnerDao
import uk.anbu.spring.sample.petclinic.lib.ResettableGlobalUtcClock
import uk.anbu.spring.sample.petclinic.lib.db.DataSourceWrapper
import uk.anbu.spring.sample.petclinic.model.Owner

import java.time.LocalDate
import java.time.LocalTime

class OwnerDaoSpecification extends Specification {
    @Shared
    static PetClinicService service;

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
        def read = dao.findOwnerById(saved.id)

        then:
        read.isPresent()
        saved.firstName == read.get().firstName
        saved.lastName == read.get().lastName
    }
}
