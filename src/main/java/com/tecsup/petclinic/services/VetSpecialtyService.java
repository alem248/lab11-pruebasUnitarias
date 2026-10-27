package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;

public interface VetSpecialtyService {

	/**
	 * Marca una especialidad ya asignada como principal del veterinario.
	 * Desmarca cualquier otra principal del mismo veterinario.
	 */
	VetSpecialty setPrimarySpecialty(Integer vetId, Integer specialtyId);
}
