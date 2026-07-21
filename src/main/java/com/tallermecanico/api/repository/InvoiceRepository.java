package com.tallermecanico.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tallermecanico.api.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, String> {
}
