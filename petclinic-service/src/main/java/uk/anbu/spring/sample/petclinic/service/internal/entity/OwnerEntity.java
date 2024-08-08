package uk.anbu.spring.sample.petclinic.service.internal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import uk.anbu.spring.sample.petclinic.service.internal.BaseEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "owners")
@EqualsAndHashCode(of = {"id"})
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(of={"lastName", "firstName", "address", "city", "telephone"})
public class OwnerEntity implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer eid;

	@Column(name = "first_name")
	@NotBlank
	private String firstName;

	@Column(name = "last_name")
	@NotBlank
	private String lastName;

	@Column(name = "address")
	@NotBlank
	private String address;

	@Column(name = "city")
	@NotBlank
	private String city;

	@Column(name = "update_timestamp_utc")
	@NotNull
	private Timestamp updateTimestampUtc;

	@Column(name = "telephone")
	@NotBlank
	@Pattern(regexp = "\\d{10}", message = "Telephone must be a 10-digit number")
	private String telephone;

	@Transient
	private List<PetEntity> pets = new ArrayList<>();

}
