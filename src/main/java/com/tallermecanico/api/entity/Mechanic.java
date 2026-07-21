package com.tallermecanico.api.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tmechanic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mechanic {

    @Id
    @Column(name = "idMechanic", length = 36)
    private String idMechanic;

    @Column(name = "firstName", nullable = false, length = 70)
    private String firstName;

    @Column(name = "specialty", nullable = false, length = 80)
    private String specialty;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "mechanic", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<WorkOrder> workOrders = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
