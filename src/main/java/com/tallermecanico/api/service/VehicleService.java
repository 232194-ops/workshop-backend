package com.tallermecanico.api.service;

import java.util.List;

import com.tallermecanico.api.dto.request.VehicleRequest;
import com.tallermecanico.api.dto.response.VehicleResponse;

public interface VehicleService {

    List<VehicleResponse> getAll();

    VehicleResponse getById(String id);

    VehicleResponse create(VehicleRequest request);

    VehicleResponse update(String id, VehicleRequest request);

    void delete(String id);
}
