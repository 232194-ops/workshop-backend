package com.tallermecanico.api.dto.request;

import java.time.LocalDateTime;

import com.tallermecanico.api.enums.WorkOrderStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderRequest {

    @NotNull(message = "Entry date is required")
    private LocalDateTime entryDate;

    @NotBlank(message = "Problem description is required")
    private String problemDescription;

    private String diagnosis;

    @NotNull(message = "Status is required")
    private WorkOrderStatus status;

    @NotNull(message = "Vehicle id is required")
    private String vehicleId;

    private String mechanicId;
}
