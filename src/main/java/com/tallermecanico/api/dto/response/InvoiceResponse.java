package com.tallermecanico.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
public class InvoiceResponse {

    private String idInvoice;
    private LocalDateTime issueDate;
    private BigDecimal total;
    private String customerId;
    private String customerFullName;
    private List<InvoiceDetailResponse> details;
}
