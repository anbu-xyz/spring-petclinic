package uk.anbu.spring.sample.petclinic.ui.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import uk.anbu.spring.sample.petclinic.service.db.dao.OwnerDao;
import uk.anbu.spring.sample.petclinic.lib.GlobalUtcClock;
import uk.anbu.spring.sample.petclinic.model.Owner;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OwnerApi {
	private final OwnerDao ownerDao;
	private GlobalUtcClock clock;

	@RequestMapping(value = "/currentTimestamp", method = RequestMethod.GET)
	public LocalDateTime currentTimestamp() {
		return clock.currentTimestamp();
	}

	@PostMapping(value = "/owner/add")
	public void addOwner(@RequestBody Owner owner) {
		ownerDao.register(owner);
	}
}
