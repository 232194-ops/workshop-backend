package com.tallermecanico.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Each detail must reference either a service OR a spare part (serviceId xor sparePartId).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailRequest {

    @NotBlank(message = "Concept is required")
    private String concept;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    private String serviceId;

    private String sparePartId;

    private String workOrderId;
}
