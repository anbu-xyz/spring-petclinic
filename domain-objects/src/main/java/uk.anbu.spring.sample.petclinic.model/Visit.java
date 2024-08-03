package uk.anbu.spring.sample.petclinic.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record Visit(Integer visitId, LocalDate visitDate, Integer petId, Integer vetId,
					String description) {
}
