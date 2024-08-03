package uk.anbu.spring.sample.petclinic.api.db;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.anbu.spring.sample.petclinic.api.db.repository.OwnerRepository;
import uk.anbu.spring.sample.petclinic.model.Owner;

@Service
@RequiredArgsConstructor
public class DatabaseFacade {
	private final OwnerRepository ownerRepository;

    public void registerNewOwner(Owner newOwner) {

    }
}
