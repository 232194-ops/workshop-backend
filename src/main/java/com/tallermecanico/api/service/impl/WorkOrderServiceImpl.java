package com.tallermecanico.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallermecanico.api.dto.request.WorkOrderRequest;
import com.tallermecanico.api.dto.response.WorkOrderResponse;
import com.tallermecanico.api.entity.Mechanic;
import com.tallermecanico.api.entity.Vehicle;
import com.tallermecanico.api.entity.WorkOrder;
import com.tallermecanico.api.exception.ResourceNotFoundException;
import com.tallermecanico.api.repository.MechanicRepository;
import com.tallermecanico.api.repository.VehicleRepository;
import com.tallermecanico.api.repository.WorkOrderRepository;
import com.tallermecanico.api.service.WorkOrderService;
import com.tallermecanico.api.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderRepository workOrderRepository;
    private final VehicleRepository vehicleRepository;
    private final MechanicRepository mechanicRepository;

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrderResponse> getAll() {
        return workOrderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkOrderResponse getById(String id) {
        WorkOrder workOrder = findWorkOrderOrFail(id);
        return mapToResponse(workOrder);
    }

    @Override
    public WorkOrderResponse create(WorkOrderRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + request.getVehicleId()));

        Mechanic mechanic = getMechanicIfPresent(request.getMechanicId());

        WorkOrder workOrder = new WorkOrder();
        workOrder.setIdWorkOrder(IdGenerator.generate());
        mapFromRequest(workOrder, request, vehicle, mechanic);

        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
        return mapToResponse(savedWorkOrder);
    }

    @Override
    public WorkOrderResponse update(String id, WorkOrderRequest request) {
        WorkOrder workOrder = findWorkOrderOrFail(id);

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vehicle not found with id: " + request.getVehicleId()));

        Mechanic mechanic = getMechanicIfPresent(request.getMechanicId());

        mapFromRequest(workOrder, request, vehicle, mechanic);

        WorkOrder updatedWorkOrder = workOrderRepository.save(workOrder);
        return mapToResponse(updatedWorkOrder);
    }

    @Override
    public void delete(String id) {
        WorkOrder workOrder = findWorkOrderOrFail(id);
        workOrderRepository.delete(workOrder);
    }

    private Mechanic getMechanicIfPresent(String mechanicId) {
        if (mechanicId == null) {
            return null;
        }
        return mechanicRepository.findById(mechanicId)
                .orElseThrow(() -> new ResourceNotFoundException("Mechanic not found with id: " + mechanicId));
    }

    private WorkOrder findWorkOrderOrFail(String id) {
        return workOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work order not found with id: " + id));
    }

    private void mapFromRequest(WorkOrder workOrder, WorkOrderRequest request, Vehicle vehicle, Mechanic mechanic) {
        workOrder.setEntryDate(request.getEntryDate());
        workOrder.setProblemDescription(request.getProblemDescription());
        workOrder.setDiagnosis(request.getDiagnosis());
        workOrder.setStatus(request.getStatus());
        workOrder.setVehicle(vehicle);
        workOrder.setMechanic(mechanic);
    }

    private WorkOrderResponse mapToResponse(WorkOrder workOrder) {
        return WorkOrderResponse.builder()
                .idWorkOrder(workOrder.getIdWorkOrder())
                .entryDate(workOrder.getEntryDate())
                .problemDescription(workOrder.getProblemDescription())
                .diagnosis(workOrder.getDiagnosis())
                .status(workOrder.getStatus())
                .vehicleId(workOrder.getVehicle().getIdVehicle())
                .vehiclePlate(workOrder.getVehicle().getPlate())
                .mechanicId(workOrder.getMechanic() != null ? workOrder.getMechanic().getIdMechanic() : null)
                .mechanicFirstName(workOrder.getMechanic() != null ? workOrder.getMechanic().getFirstName() : null)
                .build();
    }
}
