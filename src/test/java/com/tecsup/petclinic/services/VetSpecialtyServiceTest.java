package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import com.tecsup.petclinic.mappers.VetMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @Autowired
    private VetMapper vetMapper;

    @Autowired
    private SpecialtyMapper specialtyMapper;

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
}
