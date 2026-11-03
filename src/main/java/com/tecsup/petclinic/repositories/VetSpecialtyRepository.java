package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.entities.VetSpecialtyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface VetSpecialtyRepository
	extends JpaRepository<VetSpecialty, VetSpecialtyId> {

	// ¿Ya existe la asignación?
	boolean existsByVetIdAndSpecialtyId(Integer vetId, Integer specialtyId);
    List<VetSpecialty> findByVetId(Integer vetId);

	// Buscar una asignación puntual
	Optional<VetSpecialty> findByVetIdAndSpecialtyId(Integer vetId, Integer specialtyId);
    List<VetSpecialty> findBySpecialtyId(Integer specialtyId);
}
