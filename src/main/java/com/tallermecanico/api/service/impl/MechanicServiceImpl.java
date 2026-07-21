package com.tallermecanico.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallermecanico.api.dto.request.MechanicRequest;
import com.tallermecanico.api.dto.response.MechanicResponse;
import com.tallermecanico.api.entity.Mechanic;
import com.tallermecanico.api.exception.ResourceNotFoundException;
import com.tallermecanico.api.repository.MechanicRepository;
import com.tallermecanico.api.service.MechanicService;
import com.tallermecanico.api.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class MechanicServiceImpl implements MechanicService {

    private final MechanicRepository mechanicRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MechanicResponse> getAll() {
        return mechanicRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MechanicResponse getById(String id) {
        Mechanic mechanic = findMechanicOrFail(id);
        return mapToResponse(mechanic);
    }

    @Override
    public MechanicResponse create(MechanicRequest request) {
        Mechanic mechanic = new Mechanic();
        mechanic.setIdMechanic(IdGenerator.generate());
        mapFromRequest(mechanic, request);

        Mechanic savedMechanic = mechanicRepository.save(mechanic);
        return mapToResponse(savedMechanic);
    }

    @Override
    public MechanicResponse update(String id, MechanicRequest request) {
        Mechanic mechanic = findMechanicOrFail(id);
        mapFromRequest(mechanic, request);

        Mechanic updatedMechanic = mechanicRepository.save(mechanic);
        return mapToResponse(updatedMechanic);
    }

    @Override
    public void delete(String id) {
        Mechanic mechanic = findMechanicOrFail(id);
        mechanicRepository.delete(mechanic);
    }

    private Mechanic findMechanicOrFail(String id) {
        return mechanicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mechanic not found with id: " + id));
    }

    private void mapFromRequest(Mechanic mechanic, MechanicRequest request) {
        mechanic.setFirstName(request.getFirstName());
        mechanic.setSpecialty(request.getSpecialty());
        mechanic.setPhone(request.getPhone());
    }

    private MechanicResponse mapToResponse(Mechanic mechanic) {
        return MechanicResponse.builder()
                .idMechanic(mechanic.getIdMechanic())
                .firstName(mechanic.getFirstName())
                .specialty(mechanic.getSpecialty())
                .phone(mechanic.getPhone())
                .build();
    }
}
