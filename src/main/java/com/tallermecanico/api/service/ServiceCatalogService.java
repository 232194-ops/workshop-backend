package com.tallermecanico.api.service;

import java.util.List;

import com.tallermecanico.api.dto.request.ServiceRequest;
import com.tallermecanico.api.dto.response.ServiceResponse;

/**
 * Service layer for the "service" catalog entity (tservice table),
 * i.e. the workshop services offered (oil change, alignment, etc.).
 * Named ServiceCatalogService to avoid confusion with
 * org.springframework.stereotype.Service.
 */
public interface ServiceCatalogService {

    List<ServiceResponse> getAll();

    ServiceResponse getById(String id);

    ServiceResponse create(ServiceRequest request);

    ServiceResponse update(String id, ServiceRequest request);

    void delete(String id);
}
