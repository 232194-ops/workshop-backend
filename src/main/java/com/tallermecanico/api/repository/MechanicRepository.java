package com.tallermecanico.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tallermecanico.api.entity.Mechanic;

@Repository
public interface MechanicRepository extends JpaRepository<Mechanic, String> {
}
