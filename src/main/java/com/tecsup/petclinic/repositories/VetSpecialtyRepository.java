package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.VetSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VetSpecialtyRepository extends JpaRepository<VetSpecialty, Integer> {

	List<VetSpecialty> findByVetId(Integer vetId);

	Optional<VetSpecialty> findByVetIdAndSpecialtyId(Integer vetId, Integer specialtyId);
}
