package com.tallermecanico.api.service;

import java.util.List;

import com.tallermecanico.api.dto.request.WorkOrderRequest;
import com.tallermecanico.api.dto.response.WorkOrderResponse;

public interface WorkOrderService {

    List<WorkOrderResponse> getAll();

    WorkOrderResponse getById(String id);

    WorkOrderResponse create(WorkOrderRequest request);

    WorkOrderResponse update(String id, WorkOrderRequest request);

    void delete(String id);
}
