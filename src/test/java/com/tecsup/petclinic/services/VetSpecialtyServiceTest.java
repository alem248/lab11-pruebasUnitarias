package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotAssignedException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @MockitoBean
    private VetRepository vetRepository;

    @MockitoBean
    private SpecialtyRepository specialtyRepository;

    @MockitoBean
    private VetSpecialtyRepository vetSpecialtyRepository;

    @Test
    public void testFindSpecialtiesByVet() {

        Integer vetId = 1;

        Vet vet = new Vet(1, "James", "Carter", "jcarter@petclinic.com", "608555102", true);
        Mockito.when(this.vetRepository.findById(vetId))
                .thenReturn(Optional.of(vet));

        List<VetSpecialty> vetSpecialties = new ArrayList<>();
        vetSpecialties.add(new VetSpecialty(1, 1, null, 5, true, null));
        vetSpecialties.add(new VetSpecialty(1, 2, null, 3, false, null));
        Mockito.when(this.vetSpecialtyRepository.findByVetId(vetId))
                .thenReturn(vetSpecialties);

        List<Specialty> specialties = new ArrayList<>();
        specialties.add(new Specialty(1, "radiology", "Room 1", 9, 17));
        specialties.add(new Specialty(2, "surgery", "Room 2", 9, 18));
        Mockito.when(this.specialtyRepository.findAllById(Mockito.anyList()))
                .thenReturn(specialties);

        List<SpecialtyDTO> specialtiesDTO = null;
        try {
            specialtiesDTO = this.vetSpecialtyService.findSpecialtiesByVet(vetId);
        } catch (VetNotFoundException e) {
            fail(e.getMessage());
        }

        assertNotNull(specialtiesDTO);
        assertEquals(2, specialtiesDTO.size());
    }

    @Test
    public void testFindVetsBySpecialty() {

        Integer specialtyId = 1;

        List<VetSpecialty> vetSpecialties = new ArrayList<>();
        vetSpecialties.add(new VetSpecialty(1, 1, null, 5, true, null));
        vetSpecialties.add(new VetSpecialty(2, 1, null, 2, true, null));
        Mockito.when(this.vetSpecialtyRepository.findBySpecialtyId(specialtyId))
                .thenReturn(vetSpecialties);

        List<Vet> vets = new ArrayList<>();
        vets.add(new Vet(1, "James", "Carter", "jcarter@petclinic.com", "608555102", true));
        vets.add(new Vet(2, "Helen", "Leary", "hleary@petclinic.com", "608555173", true));
        Mockito.when(this.vetRepository.findAllById(Mockito.anyList()))
                .thenReturn(vets);

        List<VetDTO> vetsDTO = this.vetSpecialtyService.findVetsBySpecialty(specialtyId);

        assertNotNull(vetsDTO);
        assertEquals(2, vetsDTO.size());
    }

    @Test
    public void testFindSpecialtiesByVet_Empty() {

        Integer vetId = 1;

        Vet vet = new Vet(1, "James", "Carter", "jcarter@petclinic.com", "608555102", true);
        Mockito.when(this.vetRepository.findById(vetId))
                .thenReturn(Optional.of(vet));

        Mockito.when(this.vetSpecialtyRepository.findByVetId(vetId))
                .thenReturn(Collections.emptyList());

        List<SpecialtyDTO> specialtiesDTO = null;
        try {
            specialtiesDTO = this.vetSpecialtyService.findSpecialtiesByVet(vetId);
        } catch (VetNotFoundException e) {
            fail(e.getMessage());
        }

        assertNotNull(specialtiesDTO);
        assertTrue(specialtiesDTO.isEmpty());
    }

    // ------------------------------------------------------------------
    // Helper: asignacion veterinario - especialidad
    // ------------------------------------------------------------------
    private static final Integer VET_ID = 1;

    private VetSpecialty assignment(Integer vetId, Integer specialtyId, boolean primary) {
        return VetSpecialty.builder()
                .vetId(vetId)
                .specialtyId(specialtyId)
                .yearsExperience(0)
                .isPrimary(primary)
                .build();
    }

    // ------------------------------------------------------------------
    // Integrante C - Especialidad principal
    // ------------------------------------------------------------------
    @Test
    public void testSetPrimarySpecialty() {

        VetSpecialty cirugia = assignment(VET_ID, 10, false);
        VetSpecialty dermatologia = assignment(VET_ID, 20, false);

        Mockito.when(this.vetSpecialtyRepository.findByVetIdAndSpecialtyId(VET_ID, 20))
                .thenReturn(Optional.of(dermatologia));
        Mockito.when(this.vetSpecialtyRepository.findByVetId(VET_ID))
                .thenReturn(List.of(cirugia, dermatologia));

        VetSpecialty result = this.vetSpecialtyService.setPrimarySpecialty(VET_ID, 20);

        assertTrue(result.getIsPrimary());
        assertTrue(dermatologia.getIsPrimary());
        assertFalse(cirugia.getIsPrimary());
        Mockito.verify(this.vetSpecialtyRepository).saveAll(ArgumentMatchers.anyList());
    }

    @Test
    public void testSetPrimarySpecialty_OnlyOnePrimary() {

        VetSpecialty cirugia = assignment(VET_ID, 10, true);
        VetSpecialty dermatologia = assignment(VET_ID, 20, false);

        Mockito.when(this.vetSpecialtyRepository.findByVetIdAndSpecialtyId(VET_ID, 20))
                .thenReturn(Optional.of(dermatologia));
        Mockito.when(this.vetSpecialtyRepository.findByVetId(VET_ID))
                .thenReturn(List.of(cirugia, dermatologia));

        this.vetSpecialtyService.setPrimarySpecialty(VET_ID, 20);

        long primaries = List.of(cirugia, dermatologia).stream()
                .filter(VetSpecialty::getIsPrimary)
                .count();
        assertEquals(1, primaries);
        assertFalse(cirugia.getIsPrimary());
        assertTrue(dermatologia.getIsPrimary());
    }

    @Test
    public void testSetPrimarySpecialty_NotAssigned() {

        Mockito.when(this.vetSpecialtyRepository.findByVetIdAndSpecialtyId(VET_ID, 99))
                .thenReturn(Optional.empty());

        assertThrows(SpecialtyNotAssignedException.class,
                () -> this.vetSpecialtyService.setPrimarySpecialty(VET_ID, 99));

        Mockito.verify(this.vetSpecialtyRepository, Mockito.never()).saveAll(ArgumentMatchers.anyList());
    }

    // ------------------------------------------------------------------
    // Nikolai - Experiencia y certificaciones
    // ------------------------------------------------------------------
    @Test
    public void testFindVetsByMinExperience() {

        Integer specialtyId = 10;
        Integer minExperience = 5;

        VetSpecialty vet1 = assignment(1, specialtyId, false);
        vet1.setYearsExperience(10);
        VetSpecialty vet2 = assignment(2, specialtyId, true);
        vet2.setYearsExperience(7);

        Mockito.when(this.vetSpecialtyRepository
                        .findBySpecialtyIdAndYearsExperienceGreaterThanEqual(specialtyId, minExperience))
                .thenReturn(Arrays.asList(vet1, vet2));

        List<VetSpecialty> result = this.vetSpecialtyService.findVetsByMinExperience(specialtyId, minExperience);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(vs -> vs.getYearsExperience() >= minExperience));
        Mockito.verify(this.vetSpecialtyRepository)
                .findBySpecialtyIdAndYearsExperienceGreaterThanEqual(specialtyId, minExperience);
    }

    @Test
    public void testUpdateYearsExperience() {

        Integer specialtyId = 10;
        Integer newYearsExperience = 12;

        VetSpecialty assignment = assignment(VET_ID, specialtyId, false);
        assignment.setYearsExperience(5);

        Mockito.when(this.vetSpecialtyRepository.findByVetIdAndSpecialtyId(VET_ID, specialtyId))
                .thenReturn(Optional.of(assignment));
        Mockito.when(this.vetSpecialtyRepository.save(ArgumentMatchers.any(VetSpecialty.class)))
                .thenReturn(assignment);

        VetSpecialty result = this.vetSpecialtyService.updateYearsExperience(VET_ID, specialtyId, newYearsExperience);

        assertNotNull(result);
        assertEquals(newYearsExperience, assignment.getYearsExperience());
        Mockito.verify(this.vetSpecialtyRepository).findByVetIdAndSpecialtyId(VET_ID, specialtyId);
        Mockito.verify(this.vetSpecialtyRepository).save(assignment);
    }

    @Test
    public void testUpdateCertificationDate() {

        Integer specialtyId = 10;
        LocalDate certificationDate = LocalDate.of(2024, 6, 15);

        VetSpecialty assignment = assignment(VET_ID, specialtyId, false);
        assignment.setCertificationDate(LocalDate.of(2020, 1, 1));

        Mockito.when(this.vetSpecialtyRepository.findByVetIdAndSpecialtyId(VET_ID, specialtyId))
                .thenReturn(Optional.of(assignment));
        Mockito.when(this.vetSpecialtyRepository.save(ArgumentMatchers.any(VetSpecialty.class)))
                .thenReturn(assignment);

        VetSpecialty result = this.vetSpecialtyService.updateCertificationDate(VET_ID, specialtyId, certificationDate);

        assertNotNull(result);
        assertEquals(certificationDate, assignment.getCertificationDate());
        Mockito.verify(this.vetSpecialtyRepository).findByVetIdAndSpecialtyId(VET_ID, specialtyId);
        Mockito.verify(this.vetSpecialtyRepository).save(assignment);
    }
}
