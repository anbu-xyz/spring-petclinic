package uk.anbu.spring.sample.petclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.anbu.spring.sample.petclinic.model.Pet;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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

	public boolean isNew() {
		return eid == null;
	}
}
