package uk.anbu.spring.sample.petclinic.service.internal.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.anbu.spring.sample.petclinic.service.internal.BaseEntity;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "vets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VetEntity implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer eid;

	@Column(name = "first_name")
	@NotBlank
	private String firstName;

	@Column(name = "last_name")
	@NotBlank
	private String lastName;

	@Column(name = "email")
	@NotBlank
	private String email;

	@Column(name = "phone")
	@NotBlank
	private String phone;

	@Column(name = "license_number")
	@NotBlank
	private String licenseNumber;

	@Column(name = "update_timestamp_utc")
	@NotNull
	private Timestamp updateTimestampUtc;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"))
	@Column(name="specialty")
	private Set<String> specialties;

    public int getNrOfSpecialties() {
		throw new IllegalArgumentException("Not implemented");
    }

    public void addSpecialty(String speciality) {
		throw new IllegalArgumentException("Not implemented");
    }

}
