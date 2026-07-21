package com.tallermecanico.api.service;

import java.util.List;

import com.tallermecanico.api.dto.request.InvoiceRequest;
import com.tallermecanico.api.dto.response.InvoiceResponse;

public interface InvoiceService {

    List<InvoiceResponse> getAll();

    InvoiceResponse getById(String id);

    InvoiceResponse create(InvoiceRequest request);

    InvoiceResponse update(String id, InvoiceRequest request);

    void delete(String id);
}
