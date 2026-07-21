package com.tallermecanico.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallermecanico.api.dto.request.ServiceRequest;
import com.tallermecanico.api.dto.response.ServiceResponse;
import com.tallermecanico.api.entity.ServiceEntity;
import com.tallermecanico.api.exception.ResourceNotFoundException;
import com.tallermecanico.api.repository.ServiceRepository;
import com.tallermecanico.api.service.ServiceCatalogService;
import com.tallermecanico.api.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceCatalogServiceImpl implements ServiceCatalogService {

    private final ServiceRepository serviceRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ServiceResponse> getAll() {
        return serviceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResponse getById(String id) {
        ServiceEntity service = findServiceOrFail(id);
        return mapToResponse(service);
    }

    @Override
    public ServiceResponse create(ServiceRequest request) {
        ServiceEntity service = new ServiceEntity();
        service.setIdService(IdGenerator.generate());
        mapFromRequest(service, request);

        ServiceEntity savedService = serviceRepository.save(service);
        return mapToResponse(savedService);
    }

    @Override
    public ServiceResponse update(String id, ServiceRequest request) {
        ServiceEntity service = findServiceOrFail(id);
        mapFromRequest(service, request);

        ServiceEntity updatedService = serviceRepository.save(service);
        return mapToResponse(updatedService);
    }

    @Override
    public void delete(String id) {
        ServiceEntity service = findServiceOrFail(id);
        serviceRepository.delete(service);
    }

    private ServiceEntity findServiceOrFail(String id) {
        return serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
    }

    private void mapFromRequest(ServiceEntity service, ServiceRequest request) {
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setPrice(request.getPrice());
    }

    private ServiceResponse mapToResponse(ServiceEntity service) {
        return ServiceResponse.builder()
                .idService(service.getIdService())
                .name(service.getName())
                .description(service.getDescription())
                .price(service.getPrice())
                .build();
    }
}
