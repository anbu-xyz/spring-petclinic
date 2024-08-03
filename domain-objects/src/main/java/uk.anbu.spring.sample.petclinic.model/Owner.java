package uk.anbu.spring.sample.petclinic.model;

import java.util.List;

public record Owner(Integer ownerId, String firstName, String lastName, String address, String city,
					String telephone, List<Pet> pets) {

}
