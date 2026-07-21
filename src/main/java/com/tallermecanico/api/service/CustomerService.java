package com.tallermecanico.api.service;

import java.util.List;

import com.tallermecanico.api.dto.request.CustomerRequest;
import com.tallermecanico.api.dto.response.CustomerResponse;

public interface CustomerService {

    List<CustomerResponse> getAll();

    CustomerResponse getById(String id);

    CustomerResponse create(CustomerRequest request);

    CustomerResponse update(String id, CustomerRequest request);

    void delete(String id);
}
