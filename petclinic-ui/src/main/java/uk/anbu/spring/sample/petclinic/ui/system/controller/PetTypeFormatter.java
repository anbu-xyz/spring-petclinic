package uk.anbu.spring.sample.petclinic.ui.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.service.internal.repository.OwnerRepository;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

@Component
public class PetTypeFormatter implements Formatter<Pet.PetType> {

    private final OwnerRepository owners;

    @Autowired
    public PetTypeFormatter(OwnerRepository owners) {
        this.owners = owners;
    }

    @Override
    public String print(Pet.PetType petType, Locale locale) {
        return petType.code();
    }

    @Override
    public Pet.PetType parse(String text, Locale locale) throws ParseException {
        Collection<Pet.PetType> findPetTypes = PetController.populatePetTypes();
        for (Pet.PetType type : findPetTypes) {
            if (type.code().equals(text)) {
                return type;
            }
        }
        throw new ParseException("type not found: " + text, 0);
    }

}
