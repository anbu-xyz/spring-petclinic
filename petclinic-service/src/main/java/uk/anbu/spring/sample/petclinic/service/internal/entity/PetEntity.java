package uk.anbu.spring.sample.petclinic.service.internal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import uk.anbu.spring.sample.petclinic.model.Pet;
import uk.anbu.spring.sample.petclinic.service.internal.BaseEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pets")
@Data
@EqualsAndHashCode(of = "name")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PetEntity implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer eid;

	@Column(name = "owner_id")
	private Integer ownerId;

	@Column(name = "name")
	private String name;

	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@Setter
	@Column(name = "type")
	private String type;

	public void setType(Pet.PetType type) {
		this.type = type.code();
	}

	@Column(name = "update_timestamp_utc")
	@NotNull
	private Timestamp updateTimestampUtc;

	@Transient
	private List<VisitEntity> visits = new ArrayList<>();

}
