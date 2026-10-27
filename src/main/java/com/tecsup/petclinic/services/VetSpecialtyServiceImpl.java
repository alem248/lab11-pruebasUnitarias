package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotAssignedException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

	private final VetSpecialtyRepository vetSpecialtyRepository;

	@Override
	@Transactional
	public VetSpecialty setPrimarySpecialty(Integer vetId, Integer specialtyId) {

		VetSpecialty target = vetSpecialtyRepository.findByVetIdAndSpecialtyId(vetId, specialtyId)
				.orElseThrow(() -> new SpecialtyNotAssignedException(vetId, specialtyId));

		List<VetSpecialty> assignments = vetSpecialtyRepository.findByVetId(vetId);
		for (VetSpecialty assignment : assignments) {
			assignment.setPrimary(assignment.getSpecialty().getId().equals(specialtyId));
		}
		vetSpecialtyRepository.saveAll(assignments);

		return target;
	}
}
