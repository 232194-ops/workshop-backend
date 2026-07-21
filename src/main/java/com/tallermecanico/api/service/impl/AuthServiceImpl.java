package com.tallermecanico.api.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tallermecanico.api.dto.request.LoginRequest;
import com.tallermecanico.api.dto.request.RegisterRequest;
import com.tallermecanico.api.dto.response.AuthResponse;
import com.tallermecanico.api.entity.User;
import com.tallermecanico.api.exception.DuplicateResourceException;
import com.tallermecanico.api.repository.UserRepository;
import com.tallermecanico.api.security.JwtUtils;
import com.tallermecanico.api.security.UserPrincipal;
import com.tallermecanico.api.service.AuthService;
import com.tallermecanico.api.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("A user with the email " + request.getEmail() + " already exists");
        }

        User user = new User();
        user.setId(IdGenerator.generate());
        user.setFirstName(request.getFirstName());
        user.setSurName(request.getSurName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        UserPrincipal userPrincipal = new UserPrincipal(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userPrincipal, null, userPrincipal.getAuthorities());
        String token = jwtUtils.generateToken(authentication);

        return buildResponse(savedUser, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtUtils.generateToken(authentication);

        return buildResponse(userPrincipal.getUser(), token);
    }

    private AuthResponse buildResponse(User user, String token) {
        return AuthResponse.builder()
        		.idUser(user.getId())
                .firstName(user.getFirstName())
                .surName(user.getSurName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .type("Bearer")
                .build();
    }
}
