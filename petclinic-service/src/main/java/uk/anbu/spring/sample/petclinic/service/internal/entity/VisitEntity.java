package uk.anbu.spring.sample.petclinic.service.internal.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import uk.anbu.spring.sample.petclinic.service.internal.BaseEntity;

@Entity
@Table(name = "visits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitEntity implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer eid;

	@Column(name = "vet_id")
	private Integer vetId;

	@NotNull
	@Column(name = "pet_id")
	private Integer petId;

    @Column(name = "visit_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
    private LocalDate date;

    @NotBlank
	@Column(name = "description")
    private String description;

	@Column(name = "update_timestamp_utc")
	private Timestamp updateTimestampUtc;
}
