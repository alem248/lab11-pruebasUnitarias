package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyAssignedException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @Autowired
    private VetSpecialtyRepository repository;

    @Test
    public void testAssignSpecialtyToVet() {
        Integer VET_ID = 1;        // James Carter (según data.sql)
        Integer SPECIALTY_ID = 3;  // dentistry

        try {
            VetSpecialty result = this.vetSpecialtyService.assignSpecialtyToVet(VET_ID, SPECIALTY_ID);
            log.info("Asignación creada en H2: {}", result);

            assertNotNull(result);
            assertEquals(VET_ID, result.getVetId());
            assertEquals(SPECIALTY_ID, result.getSpecialtyId());
            assertFalse(result.getPrimary());

        } catch (VetSpecialtyAlreadyAssignedException e) {
            fail("No debería lanzar excepción al asignar una especialidad válida: " + e.getMessage());
        }
    }

    @Test
    public void testAssignDuplicate_ShouldFail() {
        Integer VET_ID = 2;        // Helen Leary
        Integer SPECIALTY_ID = 1;  // radiology (ya asignada en data.sql)

        // Act + Assert: Debe lanzar la excepción porque ya existe en la BD
        VetSpecialtyAlreadyAssignedException ex = assertThrows(
                VetSpecialtyAlreadyAssignedException.class,
                () -> this.vetSpecialtyService.assignSpecialtyToVet(VET_ID, SPECIALTY_ID));

        log.info("Excepción esperada: {}", ex.getMessage());
    }

    /**
     * Integrante A: quitar especialidad
     */
    @Test
    public void testRemoveSpecialtyFromVet() {
        Integer VET_ID = 3;        // Linda Douglas
        Integer SPECIALTY_ID = 3;  // dentistry (existe en data.sql para el veterinario 3)

        // Act: Intentar remover la especialidad asignada
        try {
            this.vetSpecialtyService.removeSpecialtyFromVet(VET_ID, SPECIALTY_ID);
            log.info("Especialidad {} removida exitosamente del veterinario {}", SPECIALTY_ID, VET_ID);
        } catch (VetSpecialtyNotFoundException e) {
            fail("No debería fallar la eliminación de una especialidad existente: " + e.getMessage());
        }
    }

}