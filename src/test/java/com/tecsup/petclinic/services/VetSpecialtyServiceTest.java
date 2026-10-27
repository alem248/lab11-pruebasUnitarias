package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.VetSpecialty;
import com.tecsup.petclinic.exceptions.VetSpecialtyAlreadyAssignedException;
import com.tecsup.petclinic.exceptions.VetSpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.VetSpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@SpringBootTest
public class VetSpecialtyServiceTest {

    @Autowired
    private VetSpecialtyService vetSpecialtyService;

    @MockitoBean
    private VetSpecialtyRepository repository;


    @Test
    public void testAssignSpecialtyToVet() {

        Integer VET_ID = 1;        // James Carter (no tiene especialidades)
        Integer SPECIALTY_ID = 3;  // dentistry

        VetSpecialty saved = new VetSpecialty(VET_ID, SPECIALTY_ID, null, 0, false, null);

        // Arrange: la asignación NO existe y save() devuelve el registro guardado
        Mockito.when(this.repository.existsByVetIdAndSpecialtyId(VET_ID, SPECIALTY_ID))
                .thenReturn(false);
        Mockito.when(this.repository.save(any(VetSpecialty.class)))
                .thenReturn(saved);

        // Act
        VetSpecialty result = null;
        try {
            result = this.vetSpecialtyService.assignSpecialtyToVet(VET_ID, SPECIALTY_ID);
        } catch (VetSpecialtyAlreadyAssignedException e) {
            fail(e.getMessage());
        }
        log.info("Asignación creada: {}", result);

        // Assert
        assertNotNull(result);
        assertEquals(VET_ID, result.getVetId());
        assertEquals(SPECIALTY_ID, result.getSpecialtyId());

        // Revisar qué objeto se mandó a save()
        ArgumentCaptor<VetSpecialty> captor = ArgumentCaptor.forClass(VetSpecialty.class);
        Mockito.verify(this.repository, Mockito.times(1)).save(captor.capture());
        assertEquals(VET_ID, captor.getValue().getVetId());
        assertEquals(SPECIALTY_ID, captor.getValue().getSpecialtyId());
        assertFalse(captor.getValue().getPrimary());
    }

    /**
     * Integrante A: quitar especialidad
     */
    @Test
    public void testRemoveSpecialtyFromVet() {

        Integer VET_ID = 3;        // Linda Douglas
        Integer SPECIALTY_ID = 3;  // dentistry

        VetSpecialty existing = new VetSpecialty(VET_ID, SPECIALTY_ID, null, 5, false, null);

        // Arrange: la asignación existe
        Mockito.when(this.repository.findByVetIdAndSpecialtyId(VET_ID, SPECIALTY_ID))
                .thenReturn(Optional.of(existing));
        Mockito.doNothing().when(this.repository).delete(existing);

        // Act
        try {
            this.vetSpecialtyService.removeSpecialtyFromVet(VET_ID, SPECIALTY_ID);
        } catch (VetSpecialtyNotFoundException e) {
            fail(e.getMessage());
        }

        // Assert: se llamó a delete() una vez
        Mockito.verify(this.repository, Mockito.times(1)).delete(existing);

        // Validar: si se intenta quitar otra vez, ya no existe
        Mockito.when(this.repository.findByVetIdAndSpecialtyId(VET_ID, SPECIALTY_ID))
                .thenReturn(Optional.empty());
        assertThrows(VetSpecialtyNotFoundException.class,
                () -> this.vetSpecialtyService.removeSpecialtyFromVet(VET_ID, SPECIALTY_ID));
    }

    /**
     * Integrante A: no permitir asignar dos veces la misma especialidad
     */
    @Test
    public void testAssignDuplicate_ShouldFail() {

        Integer VET_ID = 2;        // Helen Leary
        Integer SPECIALTY_ID = 1;  // radiology (ya asignada en data.sql)

        // Arrange: la asignación YA existe
        Mockito.when(this.repository.existsByVetIdAndSpecialtyId(VET_ID, SPECIALTY_ID))
                .thenReturn(true);

        // Act + Assert: debe lanzar la excepción
        VetSpecialtyAlreadyAssignedException ex = assertThrows(
                VetSpecialtyAlreadyAssignedException.class,
                () -> this.vetSpecialtyService.assignSpecialtyToVet(VET_ID, SPECIALTY_ID));
        log.info("Excepción esperada: {}", ex.getMessage());

        // Nunca debe llegar a guardar
        Mockito.verify(this.repository, Mockito.never()).save(any(VetSpecialty.class));
    }
}