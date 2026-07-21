package com.tallermecanico.api.service;

import java.util.List;

import com.tallermecanico.api.dto.request.MechanicRequest;
import com.tallermecanico.api.dto.response.MechanicResponse;

public interface MechanicService {

    List<MechanicResponse> getAll();

    MechanicResponse getById(String id);

    MechanicResponse create(MechanicRequest request);

    MechanicResponse update(String id, MechanicRequest request);

    void delete(String id);
}
