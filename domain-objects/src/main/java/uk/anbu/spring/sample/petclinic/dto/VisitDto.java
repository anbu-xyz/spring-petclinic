package uk.anbu.spring.sample.petclinic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitDto {
	private Integer eid;

	// @NotNull
	private Integer vetId;

	@NotNull
	private Integer petId;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	private LocalDate visitDate;

	@NotBlank
	private String description;

	public boolean isNew() {
		// TODO: is this needed?
		return eid == null;
	}
}
