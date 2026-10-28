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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

    private final VetRepository vetRepository;
    private final SpecialtyRepository specialtyRepository;
    private final VetSpecialtyRepository vetSpecialtyRepository;
    private final VetMapper vetMapper;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public List<SpecialtyDTO> findSpecialtiesByVet(Integer vetId) throws VetNotFoundException {

        Optional<Vet> vet = vetRepository.findById(vetId);
        if (!vet.isPresent()) {
            throw new VetNotFoundException("Record not found...!");
        }

        List<VetSpecialty> vetSpecialties = vetSpecialtyRepository.findByVetId(vetId);
        if (vetSpecialties == null || vetSpecialties.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> specialtyIds = vetSpecialties.stream()
                .map(VetSpecialty::getSpecialtyId)
                .collect(Collectors.toList());

        List<Specialty> specialties = specialtyRepository.findAllById(specialtyIds);

        return specialties.stream()
                .map(this.specialtyMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VetDTO> findVetsBySpecialty(Integer specialtyId) {

        List<VetSpecialty> vetSpecialties = vetSpecialtyRepository.findBySpecialtyId(specialtyId);
        if (vetSpecialties == null || vetSpecialties.isEmpty()) {
            return new ArrayList<>();
        }

        List<Integer> vetIds = vetSpecialties.stream()
                .map(VetSpecialty::getVetId)
                .collect(Collectors.toList());

        List<Vet> vets = vetRepository.findAllById(vetIds);

        return vets.stream()
                .map(this.vetMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
