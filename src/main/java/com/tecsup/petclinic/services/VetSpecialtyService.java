package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

import java.util.List;

public interface VetSpecialtyService {

    List<SpecialtyDTO> findSpecialtiesByVet(Integer vetId) throws VetNotFoundException;

    List<VetDTO> findVetsBySpecialty(Integer specialtyId);

}
