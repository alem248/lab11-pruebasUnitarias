package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyAssignedException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

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
}
