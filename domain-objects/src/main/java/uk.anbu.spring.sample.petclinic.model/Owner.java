package uk.anbu.spring.sample.petclinic.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Owner(Integer ownerId, String firstName, String lastName, String address, String city,
					String telephone, List<Pet> pets) {

}
