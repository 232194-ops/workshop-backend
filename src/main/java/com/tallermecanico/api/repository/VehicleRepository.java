package com.tallermecanico.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tallermecanico.api.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {

    List<Vehicle> findByCustomerIdCustomer(String idCustomer);

    boolean existsByPlate(String plate);

    boolean existsByPlateAndIdVehicleNot(String plate, String idVehicle);
}
