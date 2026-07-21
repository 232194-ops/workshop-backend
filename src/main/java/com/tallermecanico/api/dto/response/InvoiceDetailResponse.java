package com.tallermecanico.api.dto.response;

import java.math.BigDecimal;

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
public class InvoiceDetailResponse {

    private String idInvoiceDetail;
    private String concept;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String serviceId;
    private String sparePartId;
    private String workOrderId;
}
