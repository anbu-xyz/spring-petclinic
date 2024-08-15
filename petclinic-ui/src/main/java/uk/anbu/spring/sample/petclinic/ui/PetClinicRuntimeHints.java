package uk.anbu.spring.sample.petclinic.ui;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import uk.anbu.spring.sample.petclinic.service.internal.BaseEntity;
import uk.anbu.spring.sample.petclinic.service.internal.entity.VetEntity;

public class PetClinicRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.resources().registerPattern("db/*"); // https://github.com/spring-projects/spring-boot/issues/32654
        hints.resources().registerPattern("messages/*");
        hints.resources().registerPattern("META-INF/resources/webjars/*");
        hints.resources().registerPattern("mysql-default-conf");
        hints.serialization().registerType(BaseEntity.class);
        hints.serialization().registerType(VetEntity.class);
    }

}
