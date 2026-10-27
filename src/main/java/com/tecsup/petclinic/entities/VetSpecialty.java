package com.tecsup.petclinic.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "vet_specialties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VetSpecialty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "vet_id")
	private Vet vet;

	@ManyToOne(optional = false)
	@JoinColumn(name = "specialty_id")
	private Specialty specialty;

	@Column(name = "is_primary")
	private boolean primary;
}
