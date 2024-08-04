package uk.anbu.spring.sample.petclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import uk.anbu.spring.sample.petclinic.model.Pet;

import java.time.LocalDate;

@Builder
public class PetDto {
	Integer eid;

	@NotNull
	Integer ownerId;

	@NotBlank
	String name;

	@NotNull
	LocalDate birthDate;

	@NotNull
	Pet.PetType type;
}
