package uk.anbu.spring.sample.petclinic.model;

import java.util.List;

public record Owner(String ownerId, String firstName, String lastName, String address, String city,
					String telephone, List<Pet> pets) {

}
