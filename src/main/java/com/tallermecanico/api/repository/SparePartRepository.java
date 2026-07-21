package com.tallermecanico.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tallermecanico.api.entity.SparePart;

@Repository
public interface SparePartRepository extends JpaRepository<SparePart, String> {
}
