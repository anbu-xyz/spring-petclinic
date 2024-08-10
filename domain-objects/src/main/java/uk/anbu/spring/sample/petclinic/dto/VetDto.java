package uk.anbu.spring.sample.petclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.anbu.spring.sample.petclinic.model.Vet;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VetDto {
	Integer eid;

	@NotNull
	String firstName;

	@NotBlank
	String lastName;

	@NotBlank
	String registrationId;

	@NotNull
	@NotEmpty
	Set<Vet.SpecialtyType> specialities;

	public boolean isNew() {
		// TODO: is this needed?
		return eid == null;
	}

}
