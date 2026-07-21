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
public class CustomerResponse {

    private String idCustomer;
    private String firstName;
    private String surName;
    private String documentNumber;
    private String phone;
    private String email;
    private String address;
}
