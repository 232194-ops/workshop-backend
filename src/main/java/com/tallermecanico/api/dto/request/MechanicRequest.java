package com.tallermecanico.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MechanicRequest {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Specialty is required")
    private String specialty;

    private String phone;
}
