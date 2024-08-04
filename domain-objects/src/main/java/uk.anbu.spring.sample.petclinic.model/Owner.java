package uk.anbu.spring.sample.petclinic.model;

import lombok.Builder;

@Builder
public record Owner(Integer ownerId, String firstName, String lastName, String address, String city,
					String telephone) {
}
