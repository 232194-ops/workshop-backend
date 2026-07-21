package com.tallermecanico.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {

    private String idVehicle;
    private String brand;
    private String model;
    private Integer year;
    private String plate;
    private String color;
    private String customerId;
    private String customerFullName;
}
