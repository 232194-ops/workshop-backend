package com.tallermecanico.api.dto.response;

import java.time.LocalDateTime;

import com.tallermecanico.api.enums.WorkOrderStatus;

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
public class WorkOrderResponse {

    private String idWorkOrder;
    private LocalDateTime entryDate;
    private String problemDescription;
    private String diagnosis;
    private WorkOrderStatus status;
    private String vehicleId;
    private String vehiclePlate;
    private String mechanicId;
    private String mechanicFirstName;
}
