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
public class SparePartResponse {

    private String idSparePart;
    private String name;
    private String brand;
    private BigDecimal price;
    private Integer stock;
}
