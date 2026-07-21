package com.tallermecanico.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallermecanico.api.dto.request.VehicleRequest;
import com.tallermecanico.api.dto.response.VehicleResponse;
import com.tallermecanico.api.entity.Customer;
import com.tallermecanico.api.entity.Vehicle;
import com.tallermecanico.api.exception.DuplicateResourceException;
import com.tallermecanico.api.exception.ResourceNotFoundException;
import com.tallermecanico.api.repository.CustomerRepository;
import com.tallermecanico.api.repository.VehicleRepository;
import com.tallermecanico.api.service.VehicleService;
import com.tallermecanico.api.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponse> getAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponse getById(String id) {
        Vehicle vehicle = findVehicleOrFail(id);
        return mapToResponse(vehicle);
    }

    @Override
    public VehicleResponse create(VehicleRequest request) {
        if (vehicleRepository.existsByPlate(request.getPlate())) {
            throw new DuplicateResourceException("A vehicle with plate " + request.getPlate() + " already exists");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()));

        Vehicle vehicle = new Vehicle();
        vehicle.setIdVehicle(IdGenerator.generate());
        mapFromRequest(vehicle, request, customer);

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return mapToResponse(savedVehicle);
    }

    @Override
    public VehicleResponse update(String id, VehicleRequest request) {
        Vehicle vehicle = findVehicleOrFail(id);

        if (vehicleRepository.existsByPlateAndIdVehicleNot(request.getPlate(), id)) {
            throw new DuplicateResourceException(
                    "Another vehicle with plate " + request.getPlate() + " already exists");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()));

        mapFromRequest(vehicle, request, customer);

        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return mapToResponse(updatedVehicle);
    }

    @Override
    public void delete(String id) {
        Vehicle vehicle = findVehicleOrFail(id);
        vehicleRepository.delete(vehicle);
    }

    private Vehicle findVehicleOrFail(String id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
    }

    private void mapFromRequest(Vehicle vehicle, VehicleRequest request, Customer customer) {
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setYear(request.getYear());
        vehicle.setPlate(request.getPlate());
        vehicle.setColor(request.getColor());
        vehicle.setCustomer(customer);
    }

    private VehicleResponse mapToResponse(Vehicle vehicle) {
        return VehicleResponse.builder()
                .idVehicle(vehicle.getIdVehicle())
                .brand(vehicle.getBrand())
                .model(vehicle.getModel())
                .year(vehicle.getYear())
                .plate(vehicle.getPlate())
                .color(vehicle.getColor())
                .customerId(vehicle.getCustomer().getIdCustomer())
                .customerFullName(vehicle.getCustomer().getFirstName() + " " + vehicle.getCustomer().getSurName())
                .build();
    }
}
