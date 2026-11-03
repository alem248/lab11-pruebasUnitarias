package com.tecsup.petclinic.exceptions;

public class VetSpecialtyAlreadyAssignedException extends Exception {

	private static final long serialVersionUID = 1L;

	public VetSpecialtyAlreadyAssignedException(String message) {
		super(message);
	}
}
