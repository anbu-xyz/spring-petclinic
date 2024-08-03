package uk.anbu.spring.sample.petclinic.model;

import java.time.LocalDate;
import java.util.Optional;

public record Pet(String petId, PetType type, String name, LocalDate birthDate) {
	public enum Type {
		CAT,
		DOG,
		LIZARD,
		SNAKE,
		BIRD,
		HAMSTER,
		OTHER
	}

	public record PetType(Type type, String code) {
		public static PetType of(Type type) {
			return new PetType(type, type.name());
		}

		public static PetType of(String code) {
			try {
				var type = Type.valueOf(Optional.ofNullable((code)).orElse("").toUpperCase());
				return new PetType(type, type.name());
			} catch (IllegalArgumentException e) {
				return new PetType(Type.OTHER, Optional.ofNullable(code).orElse(""));
			}
		}
	}
}
