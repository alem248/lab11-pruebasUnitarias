package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyAssignedException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;

public interface VetSpecialtyService {

	/**
	 * Asigna una especialidad a un veterinario
	 */
	VetSpecialty assignSpecialtyToVet(Integer vetId, Integer specialtyId)
			throws VetSpecialtyAlreadyAssignedException;

	/**
	 * Quita una especialidad de un veterinario
	 */
	void removeSpecialtyFromVet(Integer vetId, Integer specialtyId)
			throws VetSpecialtyNotFoundException;
}
