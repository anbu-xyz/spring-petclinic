package uk.anbu.spring.sample.petclinic.ui.system.controller;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.anbu.spring.sample.petclinic.dto.PetDto;

public class PetValidator implements Validator {

    private static final String REQUIRED = "required";

    @Override
    public void validate(Object obj, Errors errors) {
        PetDto pet = (PetDto) obj;
        String name = pet.getName();
        // name validation
        if (!StringUtils.hasText(name)) {
        	errors.rejectValue("name", REQUIRED, REQUIRED);
		}

		// type validation
		if (pet.getEid() == null && pet.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}

		// birth date validation
		if (pet.getBirthDate() == null) {
			errors.rejectValue("birthDate", REQUIRED, REQUIRED);
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return PetDto.class.isAssignableFrom(clazz);
	}

}
