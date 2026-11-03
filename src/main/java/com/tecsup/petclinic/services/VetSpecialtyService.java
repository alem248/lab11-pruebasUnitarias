package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyAssignedException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.exceptions.SpecialtyNotAssignedException;

import java.time.LocalDate;
import java.util.List;

public interface VetSpecialtyService {

	/**
	 * Asigna una especialidad a un veterinario
	 */
	VetSpecialty assignSpecialtyToVet(Integer vetId, Integer specialtyId)
			throws VetSpecialtyAlreadyAssignedException;
    List<SpecialtyDTO> findSpecialtiesByVet(Integer vetId) throws VetNotFoundException;

    List<VetDTO> findVetsBySpecialty(Integer specialtyId);

	/**
	 * Quita una especialidad de un veterinario
	 */
	void removeSpecialtyFromVet(Integer vetId, Integer specialtyId)
			throws VetSpecialtyNotFoundException;
    /**
     * Marca una especialidad ya asignada como principal y desmarca las demás del veterinario.
     */
    VetSpecialty setPrimarySpecialty(Integer vetId, Integer specialtyId);

    /**
     * Lista las asignaciones de una especialidad con al menos minExperience anios de experiencia.
     * Si minExperience es null devuelve todas las asignaciones de la especialidad.
     */
    List<VetSpecialty> findVetsByMinExperience(Integer specialtyId, Integer minExperience);

    /**
     * Actualiza los anios de experiencia de un veterinario en una especialidad asignada.
     */
    VetSpecialty updateYearsExperience(Integer vetId, Integer specialtyId, Integer yearsExperience);

    /**
     * Actualiza la fecha de certificacion de un veterinario en una especialidad asignada.
     */
    VetSpecialty updateCertificationDate(Integer vetId, Integer specialtyId, LocalDate certificationDate);
}
