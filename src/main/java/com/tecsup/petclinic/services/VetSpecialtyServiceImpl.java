package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyAssignedException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

	private final VetSpecialtyRepository vetSpecialtyRepository;

	@Override
	public VetSpecialty assignSpecialtyToVet(Integer vetId, Integer specialtyId)
			throws VetSpecialtyAlreadyAssignedException {

		if (vetSpecialtyRepository.existsByVetIdAndSpecialtyId(vetId, specialtyId))
			throw new VetSpecialtyAlreadyAssignedException(
					"El veterinario " + vetId + " ya tiene la especialidad " + specialtyId);

		VetSpecialty vetSpecialty = VetSpecialty.builder()
				.vetId(vetId)
				.specialtyId(specialtyId)
				.yearsExperience(0)
				.primary(false)
				.build();

		return vetSpecialtyRepository.save(vetSpecialty);
	}

	@Override
	public void removeSpecialtyFromVet(Integer vetId, Integer specialtyId)
			throws VetSpecialtyNotFoundException {

		VetSpecialty vetSpecialty = vetSpecialtyRepository
				.findByVetIdAndSpecialtyId(vetId, specialtyId)
				.orElseThrow(() -> new VetSpecialtyNotFoundException("Record not found...!"));

		vetSpecialtyRepository.delete(vetSpecialty);
	}
}
