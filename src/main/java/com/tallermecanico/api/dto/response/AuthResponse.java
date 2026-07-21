package com.tallermecanico.api.dto.response;

import com.tallermecanico.api.enums.Role;

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
public class AuthResponse {

    private String idUser;
    private String firstName;
    private String surName;
    private String email;
    private Role role;
    private String token;
    private String type;
}
