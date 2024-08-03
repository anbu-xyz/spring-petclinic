package uk.anbu.spring.sample.petclinic.api.db.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import uk.anbu.spring.sample.petclinic.api.db.BaseEntity;
import uk.anbu.spring.sample.petclinic.model.Pet;

@Entity
@Table(name = "pets")
@Data
@EqualsAndHashCode(of = "name")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetEntity implements BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "owner_id")
	private Integer ownerId;

	@Column(name = "name")
	private String name;

	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birthDate;

	@Column(name = "type")
	private String type;

	public void setType(Pet.PetType type) {
		this.type = type.code();
	}

	@Column(name = "update_timestamp_utc")
	@NotNull
	private Timestamp updateTimestampUtc;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "pet_id")
	@OrderBy("visit_date ASC")
	private Set<VisitEntity> visits = new LinkedHashSet<>();

	public void addVisit(VisitEntity visit) {
		throw new IllegalArgumentException("To be deleted");
	}
}
