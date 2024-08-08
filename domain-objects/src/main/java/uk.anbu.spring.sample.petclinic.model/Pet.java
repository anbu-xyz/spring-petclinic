package uk.anbu.spring.sample.petclinic.model;

import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Optional;

@Builder
public record Pet(Integer eid, Integer ownerId, PetType type, String name, @DateTimeFormat(pattern = "yyyy-MM-dd")
				  LocalDate birthDate) {
	public Pet {
		if (ownerId == null) {
			throw new IllegalArgumentException("OwnerId cannot be null");
		}
	}
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

		@Override
		public String toString() {
			return code;
		}
	}

	public boolean isNew() {
		return eid == null;
	}
}
