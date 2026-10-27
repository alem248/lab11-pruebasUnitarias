package com.tecsup.petclinic.exceptions;

public class SpecialtyNotAssignedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SpecialtyNotAssignedException(Integer vetId, Integer specialtyId) {
		super("La especialidad " + specialtyId + " no está asignada al veterinario " + vetId);
	}
}
