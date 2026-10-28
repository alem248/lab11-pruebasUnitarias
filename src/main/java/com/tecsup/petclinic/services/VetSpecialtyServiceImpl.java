package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotAssignedException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

	@Override
	public List<VetSpecialty> findVetsByMinExperience(Integer specialtyId, Integer minExperience) {
		if (minExperience == null) {
			return vetSpecialtyRepository.findBySpecialtyId(specialtyId);
		}
		return vetSpecialtyRepository.findBySpecialtyIdAndYearsExperienceGreaterThanEqual(specialtyId, minExperience);
	}

	@Override
	@Transactional
	public VetSpecialty updateYearsExperience(Integer vetId, Integer specialtyId, Integer yearsExperience) {
		VetSpecialty target = vetSpecialtyRepository.findByVetIdAndSpecialtyId(vetId, specialtyId)
				.orElseThrow(() -> new SpecialtyNotAssignedException(vetId, specialtyId));
		target.setYearsExperience(yearsExperience);
		return vetSpecialtyRepository.save(target);
	}

	@Override
	@Transactional
	public VetSpecialty updateCertificationDate(Integer vetId, Integer specialtyId, LocalDate certificationDate) {
		VetSpecialty target = vetSpecialtyRepository.findByVetIdAndSpecialtyId(vetId, specialtyId)
				.orElseThrow(() -> new SpecialtyNotAssignedException(vetId, specialtyId));
		target.setCertificationDate(certificationDate);
		return vetSpecialtyRepository.save(target);
	}
}
