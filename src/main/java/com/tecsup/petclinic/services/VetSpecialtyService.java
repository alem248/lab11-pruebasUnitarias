package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;

import java.time.LocalDate;
import java.util.List;

public interface VetSpecialtyService {

	/**
	 * Marca una especialidad ya asignada como principal del veterinario.
	 * Desmarca cualquier otra principal del mismo veterinario.
	 */
	VetSpecialty setPrimarySpecialty(Integer vetId, Integer specialtyId);

	List<VetSpecialty> findVetsByMinExperience(Integer specialtyId, Integer minExperience);

	VetSpecialty updateYearsExperience(Integer vetId, Integer specialtyId, Integer yearsExperience);

	VetSpecialty updateCertificationDate(Integer vetId, Integer specialtyId, LocalDate certificationDate);
}
