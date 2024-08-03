package uk.anbu.spring.sample.petclinic.lib;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class DefaultGlobalUtcClock implements GlobalUtcClock {
	private final Clock clock;

	public DefaultGlobalUtcClock() {
		this.clock = Clock.systemUTC();
	}

	@Override
	public Instant instant() {
		return clock.instant();
	}

	@Override
	public LocalDate currentDate() {
		return LocalDate.ofInstant(clock.instant(), clock.getZone());
	}

	@Override
	public LocalDateTime currentTimestamp() {
		return LocalDateTime.ofInstant(clock.instant(), clock.getZone());
	}
}
