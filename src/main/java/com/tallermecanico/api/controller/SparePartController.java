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

import com.tallermecanico.api.dto.request.SparePartRequest;
import com.tallermecanico.api.dto.response.ApiResponse;
import com.tallermecanico.api.dto.response.SparePartResponse;
import com.tallermecanico.api.service.SparePartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/spare-parts")
@RequiredArgsConstructor
public class SparePartController {

    private final SparePartService sparePartService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SparePartResponse>>> getAll() {
        List<SparePartResponse> spareParts = sparePartService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Spare part list retrieved successfully", spareParts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SparePartResponse>> getById(@PathVariable String id) {
        SparePartResponse sparePart = sparePartService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Spare part found", sparePart));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SparePartResponse>> create(@Valid @RequestBody SparePartRequest request) {
        SparePartResponse createdSparePart = sparePartService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Spare part created successfully", createdSparePart));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<SparePartResponse>> update(
            @PathVariable String id, @Valid @RequestBody SparePartRequest request) {
        SparePartResponse updatedSparePart = sparePartService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Spare part updated successfully", updatedSparePart));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable String id) {
        sparePartService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Spare part deleted successfully", null));
    }
}
