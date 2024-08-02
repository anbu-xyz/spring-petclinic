package uk.anbu.spring.sample.petclinic.model;

import java.util.List;
import java.util.Optional;

public record Vet(String registrationId, String firstName, String lastName, List<SpecialtyType> specialty) {
	public enum Type {
		RADIOLOGY,
		SURGERY,
		DENTISTRY,
		OTHER
	}

	public record SpecialtyType(Type type, String code) {
		public static SpecialtyType of(Type type) {
			return new SpecialtyType(type, type.name());
		}
		public static SpecialtyType of(String code) {
			try {
				var type = Type.valueOf(Optional.ofNullable((code)).orElse("").toUpperCase());
				return new SpecialtyType(type, type.name());
			} catch (IllegalArgumentException e) {
				return new SpecialtyType(Type.OTHER, Optional.ofNullable(code).orElse(""));
			}
		}
	}
}
