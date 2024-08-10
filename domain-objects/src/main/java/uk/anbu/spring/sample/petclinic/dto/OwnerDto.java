package uk.anbu.spring.sample.petclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class OwnerDto {
	private Integer eid;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	private String address;

	@NotBlank
	private String city;

	@NotBlank
	@Pattern(regexp = "\\d{10}", message = "Telephone must be a 10-digit number")
	private String telephone;

	@Builder.Default
	private List<PetDto> pets = new ArrayList<>();

	public boolean isNew() {
		// TODO: is this needed?
		return eid == null;
	}
}
