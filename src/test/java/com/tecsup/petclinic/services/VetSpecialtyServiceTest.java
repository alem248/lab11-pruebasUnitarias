package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotAssignedException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Integrante C — Especialidad principal (Grupo 5).
 */
@ExtendWith(MockitoExtension.class)
public class VetSpecialtyServiceTest {

	private static final Integer VET_ID = 1;

	@Mock
	private VetSpecialtyRepository vetSpecialtyRepository;

	@InjectMocks
	private VetSpecialtyServiceImpl vetSpecialtyService;

	private VetSpecialty assignment(Integer id, Integer specialtyId, boolean primary) {
		Vet vet = new Vet(VET_ID, "Dr. Pérez", true);
		Specialty specialty = new Specialty(specialtyId, "Especialidad " + specialtyId);
		return new VetSpecialty(id, vet, specialty, primary);
	}

	@Test
	public void testSetPrimarySpecialty() {

		VetSpecialty cirugia = assignment(1, 10, false);
		VetSpecialty dermatologia = assignment(2, 20, false);

		when(vetSpecialtyRepository.findByVetIdAndSpecialtyId(VET_ID, 20))
				.thenReturn(Optional.of(dermatologia));
		when(vetSpecialtyRepository.findByVetId(VET_ID))
				.thenReturn(List.of(cirugia, dermatologia));

		VetSpecialty result = vetSpecialtyService.setPrimarySpecialty(VET_ID, 20);

		assertTrue(result.isPrimary());
		assertTrue(dermatologia.isPrimary());
		assertFalse(cirugia.isPrimary());
		verify(vetSpecialtyRepository).saveAll(anyList());
	}

	@Test
	public void testSetPrimarySpecialty_OnlyOnePrimary() {

		VetSpecialty cirugia = assignment(1, 10, true);
		VetSpecialty dermatologia = assignment(2, 20, false);

		when(vetSpecialtyRepository.findByVetIdAndSpecialtyId(VET_ID, 20))
				.thenReturn(Optional.of(dermatologia));
		when(vetSpecialtyRepository.findByVetId(VET_ID))
				.thenReturn(List.of(cirugia, dermatologia));

		vetSpecialtyService.setPrimarySpecialty(VET_ID, 20);

		long primaries = List.of(cirugia, dermatologia).stream().filter(VetSpecialty::isPrimary).count();
		assertEquals(1, primaries);
		assertFalse(cirugia.isPrimary());
		assertTrue(dermatologia.isPrimary());
	}

	@Test
	public void testSetPrimarySpecialty_NotAssigned() {

		when(vetSpecialtyRepository.findByVetIdAndSpecialtyId(VET_ID, 99))
				.thenReturn(Optional.empty());

		assertThrows(SpecialtyNotAssignedException.class,
				() -> vetSpecialtyService.setPrimarySpecialty(VET_ID, 99));

		verify(vetSpecialtyRepository, never()).saveAll(anyList());
	}
}
