package com.tallermecanico.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tallermecanico.api.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    boolean existsByDocumentNumber(String documentNumber);

    boolean existsByDocumentNumberAndIdCustomerNot(String documentNumber, String idCustomer);
}
