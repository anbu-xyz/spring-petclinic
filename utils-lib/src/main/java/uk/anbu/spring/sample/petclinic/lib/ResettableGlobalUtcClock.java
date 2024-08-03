package uk.anbu.spring.sample.petclinic.lib;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class ResettableGlobalUtcClock implements GlobalUtcClock {
	private Clock clock;

	public ResettableGlobalUtcClock(Clock clock) {
		setClock(clock);
	}

	public ResettableGlobalUtcClock(LocalDate date, LocalTime time) {
		LocalDateTime localDateTime = LocalDateTime.of(date, time);
		Instant fixedInstant = localDateTime.toInstant(ZoneOffset.UTC);
		setClock(Clock.fixed(fixedInstant, ZoneOffset.UTC));
	}

	private void setClock(Clock clock) {
		if ("UTC".equals(clock.getZone().getId()) || "Z".equals(clock.getZone().getId())) {
			this.clock = clock;
		} else {
			throw new IllegalArgumentException("Cock has to be UTC zone Id. Found " + clock.getZone().getId());
		}
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
