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
public class MechanicResponse {

    private String idMechanic;
    private String firstName;
    private String specialty;
    private String phone;
}
