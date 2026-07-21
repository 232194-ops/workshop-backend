package com.tallermecanico.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallermecanico.api.dto.request.SparePartRequest;
import com.tallermecanico.api.dto.response.SparePartResponse;
import com.tallermecanico.api.entity.SparePart;
import com.tallermecanico.api.exception.ResourceNotFoundException;
import com.tallermecanico.api.repository.SparePartRepository;
import com.tallermecanico.api.service.SparePartService;
import com.tallermecanico.api.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SparePartServiceImpl implements SparePartService {

    private final SparePartRepository sparePartRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SparePartResponse> getAll() {
        return sparePartRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SparePartResponse getById(String id) {
        SparePart sparePart = findSparePartOrFail(id);
        return mapToResponse(sparePart);
    }

    @Override
    public SparePartResponse create(SparePartRequest request) {
        SparePart sparePart = new SparePart();
        sparePart.setIdSparePart(IdGenerator.generate());
        mapFromRequest(sparePart, request);

        SparePart savedSparePart = sparePartRepository.save(sparePart);
        return mapToResponse(savedSparePart);
    }

    @Override
    public SparePartResponse update(String id, SparePartRequest request) {
        SparePart sparePart = findSparePartOrFail(id);
        mapFromRequest(sparePart, request);

        SparePart updatedSparePart = sparePartRepository.save(sparePart);
        return mapToResponse(updatedSparePart);
    }

    @Override
    public void delete(String id) {
        SparePart sparePart = findSparePartOrFail(id);
        sparePartRepository.delete(sparePart);
    }

    private SparePart findSparePartOrFail(String id) {
        return sparePartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spare part not found with id: " + id));
    }

    private void mapFromRequest(SparePart sparePart, SparePartRequest request) {
        sparePart.setName(request.getName());
        sparePart.setBrand(request.getBrand());
        sparePart.setPrice(request.getPrice());
        sparePart.setStock(request.getStock());
    }

    private SparePartResponse mapToResponse(SparePart sparePart) {
        return SparePartResponse.builder()
                .idSparePart(sparePart.getIdSparePart())
                .name(sparePart.getName())
                .brand(sparePart.getBrand())
                .price(sparePart.getPrice())
                .stock(sparePart.getStock())
                .build();
    }
}
