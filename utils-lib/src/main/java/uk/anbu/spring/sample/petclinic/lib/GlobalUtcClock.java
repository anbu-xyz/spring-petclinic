package uk.anbu.spring.sample.petclinic.lib;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface GlobalUtcClock {
	Instant instant();

	LocalDate currentDate();

	LocalDateTime currentTimestamp();
}
