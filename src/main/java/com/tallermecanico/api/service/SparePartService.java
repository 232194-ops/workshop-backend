package com.tallermecanico.api.service;

import java.util.List;

import com.tallermecanico.api.dto.request.SparePartRequest;
import com.tallermecanico.api.dto.response.SparePartResponse;

public interface SparePartService {

    List<SparePartResponse> getAll();

    SparePartResponse getById(String id);

    SparePartResponse create(SparePartRequest request);

    SparePartResponse update(String id, SparePartRequest request);

    void delete(String id);
}
