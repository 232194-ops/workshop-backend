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

import com.tallermecanico.api.dto.request.WorkOrderRequest;
import com.tallermecanico.api.dto.response.ApiResponse;
import com.tallermecanico.api.dto.response.WorkOrderResponse;
import com.tallermecanico.api.service.WorkOrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkOrderResponse>>> getAll() {
        List<WorkOrderResponse> workOrders = workOrderService.getAll();
        return ResponseEntity.ok(ApiResponse.success("Work order list retrieved successfully", workOrders));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> getById(@PathVariable String id) {
        WorkOrderResponse workOrder = workOrderService.getById(id);
        return ResponseEntity.ok(ApiResponse.success("Work order found", workOrder));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> create(@Valid @RequestBody WorkOrderRequest request) {
        WorkOrderResponse createdWorkOrder = workOrderService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Work order created successfully", createdWorkOrder));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MECHANIC', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<WorkOrderResponse>> update(
            @PathVariable String id, @Valid @RequestBody WorkOrderRequest request) {
        WorkOrderResponse updatedWorkOrder = workOrderService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success("Work order updated successfully", updatedWorkOrder));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Object>> delete(@PathVariable String id) {
        workOrderService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Work order deleted successfully", null));
    }
}
