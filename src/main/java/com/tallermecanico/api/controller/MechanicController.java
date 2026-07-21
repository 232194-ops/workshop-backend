package com.tallermecanico.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tallermecanico.api.dto.request.MechanicRequest;
import com.tallermecanico.api.dto.response.ApiResponse;
import com.tallermecanico.api.dto.response.MechanicResponse;
import com.tallermecanico.api.service.MechanicService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/mechanics")
@RequiredArgsConstructor
public class MechanicController {

    private final MechanicService mechanicService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<MechanicResponse>>> getAll() {
        List<MechanicResponse> mechanics = mechanicService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Mechanic list retrieved successfully", mechanics));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MechanicResponse>> getById(@PathVariable String id) {
        MechanicResponse mechanic = mechanicService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Mechanic found", mechanic));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MechanicResponse>> create(@Valid @RequestBody MechanicRequest request) {
        MechanicResponse createdMechanic = mechanicService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mechanic created successfully", createdMechanic));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MechanicResponse>> update(
            @PathVariable String id, @Valid @RequestBody MechanicRequest request) {
        MechanicResponse updatedMechanic = mechanicService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Mechanic updated successfully", updatedMechanic));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable String id) {
        mechanicService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Mechanic deleted successfully", null));
    }
}
