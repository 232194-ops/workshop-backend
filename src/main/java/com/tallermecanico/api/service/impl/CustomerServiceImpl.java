package com.tallermecanico.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tallermecanico.api.dto.request.CustomerRequest;
import com.tallermecanico.api.dto.response.CustomerResponse;
import com.tallermecanico.api.entity.Customer;
import com.tallermecanico.api.exception.DuplicateResourceException;
import com.tallermecanico.api.exception.ResourceNotFoundException;
import com.tallermecanico.api.repository.CustomerRepository;
import com.tallermecanico.api.service.CustomerService;
import com.tallermecanico.api.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAll() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getById(String id) {
        Customer customer = findCustomerOrFail(id);
        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        if (customerRepository.existsByDocumentNumber(request.getDocumentNumber())) {
            throw new DuplicateResourceException(
                    "A customer with document number " + request.getDocumentNumber() + " already exists");
        }

        Customer customer = new Customer();
        customer.setIdCustomer(IdGenerator.generate());
        mapFromRequest(customer, request);

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    @Override
    public CustomerResponse update(String id, CustomerRequest request) {
        Customer customer = findCustomerOrFail(id);

        if (customerRepository.existsByDocumentNumberAndIdCustomerNot(request.getDocumentNumber(), id)) {
            throw new DuplicateResourceException(
                    "Another customer with document number " + request.getDocumentNumber() + " already exists");
        }

        mapFromRequest(customer, request);

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }

    @Override
    public void delete(String id) {
        Customer customer = findCustomerOrFail(id);
        customerRepository.delete(customer);
    }

    private Customer findCustomerOrFail(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
    }

    private void mapFromRequest(Customer customer, CustomerRequest request) {
        customer.setFirstName(request.getFirstName());
        customer.setSurName(request.getSurName());
        customer.setDocumentNumber(request.getDocumentNumber());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .idCustomer(customer.getIdCustomer())
                .firstName(customer.getFirstName())
                .surName(customer.getSurName())
                .documentNumber(customer.getDocumentNumber())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .build();
    }
}
