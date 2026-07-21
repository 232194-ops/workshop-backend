package com.tallermecanico.api.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallermecanico.api.dto.request.InvoiceDetailRequest;
import com.tallermecanico.api.dto.request.InvoiceRequest;
import com.tallermecanico.api.dto.response.InvoiceDetailResponse;
import com.tallermecanico.api.dto.response.InvoiceResponse;
import com.tallermecanico.api.entity.Customer;
import com.tallermecanico.api.entity.Invoice;
import com.tallermecanico.api.entity.InvoiceDetail;
import com.tallermecanico.api.entity.ServiceEntity;
import com.tallermecanico.api.entity.SparePart;
import com.tallermecanico.api.entity.WorkOrder;
import com.tallermecanico.api.exception.BadRequestException;
import com.tallermecanico.api.exception.ResourceNotFoundException;
import com.tallermecanico.api.repository.CustomerRepository;
import com.tallermecanico.api.repository.InvoiceRepository;
import com.tallermecanico.api.repository.ServiceRepository;
import com.tallermecanico.api.repository.SparePartRepository;
import com.tallermecanico.api.repository.WorkOrderRepository;
import com.tallermecanico.api.service.InvoiceService;
import com.tallermecanico.api.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ServiceRepository serviceRepository;
    private final SparePartRepository sparePartRepository;
    private final WorkOrderRepository workOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAll() {
        return invoiceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getById(String id) {
        Invoice invoice = findInvoiceOrFail(id);
        return mapToResponse(invoice);
    }

    @Override
    public InvoiceResponse create(InvoiceRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()));

        Invoice invoice = new Invoice();
        invoice.setIdInvoice(IdGenerator.generate());
        invoice.setIssueDate(request.getIssueDate());
        invoice.setCustomer(customer);
        invoice.setTotal(BigDecimal.ZERO);

        buildDetails(invoice, request.getDetails());

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return mapToResponse(savedInvoice);
    }

    @Override
    public InvoiceResponse update(String id, InvoiceRequest request) {
        Invoice invoice = findInvoiceOrFail(id);

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with id: " + request.getCustomerId()));

        invoice.setIssueDate(request.getIssueDate());
        invoice.setCustomer(customer);

        invoice.getDetails().clear();
        buildDetails(invoice, request.getDetails());

        Invoice updatedInvoice = invoiceRepository.save(invoice);
        return mapToResponse(updatedInvoice);
    }

    @Override
    public void delete(String id) {
        Invoice invoice = findInvoiceOrFail(id);
        invoiceRepository.delete(invoice);
    }

    private void buildDetails(Invoice invoice, List<InvoiceDetailRequest> detailsRequest) {
        List<InvoiceDetail> details = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (InvoiceDetailRequest detailRequest : detailsRequest) {
            boolean hasService = detailRequest.getServiceId() != null;
            boolean hasSparePart = detailRequest.getSparePartId() != null;

            if (hasService && hasSparePart) {
                throw new BadRequestException(
                        "An invoice detail cannot reference a service and a spare part at the same time");
            }

            BigDecimal unitPrice;
            ServiceEntity service = null;
            SparePart sparePart = null;

            if (hasService) {
                service = serviceRepository.findById(detailRequest.getServiceId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Service not found with id: " + detailRequest.getServiceId()));
                unitPrice = service.getPrice();
            } else if (hasSparePart) {
                sparePart = sparePartRepository.findById(detailRequest.getSparePartId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Spare part not found with id: " + detailRequest.getSparePartId()));

                if (sparePart.getStock() < detailRequest.getQuantity()) {
                    throw new BadRequestException(
                            "Insufficient stock for spare part: " + sparePart.getName()
                                    + " (available stock: " + sparePart.getStock() + ")");
                }
                sparePart.setStock(sparePart.getStock() - detailRequest.getQuantity());
                sparePartRepository.save(sparePart);

                unitPrice = sparePart.getPrice();
            } else {
                throw new BadRequestException("Each detail must reference a service or a spare part");
            }

            WorkOrder workOrder = null;
            if (detailRequest.getWorkOrderId() != null) {
                workOrder = workOrderRepository.findById(detailRequest.getWorkOrderId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Work order not found with id: " + detailRequest.getWorkOrderId()));
            }

            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(detailRequest.getQuantity()));

            InvoiceDetail detail = new InvoiceDetail();
            detail.setIdInvoiceDetail(IdGenerator.generate());
            detail.setConcept(detailRequest.getConcept());
            detail.setQuantity(detailRequest.getQuantity());
            detail.setUnitPrice(unitPrice);
            detail.setSubtotal(subtotal);
            detail.setService(service);
            detail.setSparePart(sparePart);
            detail.setWorkOrder(workOrder);
            detail.setInvoice(invoice);

            details.add(detail);
            total = total.add(subtotal);
        }

        invoice.getDetails().addAll(details);
        invoice.setTotal(total);
    }

    private Invoice findInvoiceOrFail(String id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found with id: " + id));
    }

    private InvoiceResponse mapToResponse(Invoice invoice) {
        List<InvoiceDetailResponse> details = invoice.getDetails().stream()
                .map(detail -> InvoiceDetailResponse.builder()
                        .idInvoiceDetail(detail.getIdInvoiceDetail())
                        .concept(detail.getConcept())
                        .quantity(detail.getQuantity())
                        .unitPrice(detail.getUnitPrice())
                        .subtotal(detail.getSubtotal())
                        .serviceId(detail.getService() != null ? detail.getService().getIdService() : null)
                        .sparePartId(detail.getSparePart() != null ? detail.getSparePart().getIdSparePart() : null)
                        .workOrderId(detail.getWorkOrder() != null ? detail.getWorkOrder().getIdWorkOrder() : null)
                        .build())
                .toList();

        return InvoiceResponse.builder()
                .idInvoice(invoice.getIdInvoice())
                .issueDate(invoice.getIssueDate())
                .total(invoice.getTotal())
                .customerId(invoice.getCustomer().getIdCustomer())
                .customerFullName(invoice.getCustomer().getFirstName() + " " + invoice.getCustomer().getSurName())
                .details(details)
                .build();
    }
}
