package com.example.carins.model;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "insuranceclaim")
public class InsuranceClaim {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Car car;

    @NotNull(message = "claimDate is required")
    private LocalDate claimDate;

    @NotNull(message = "description is required")
    private String description;

    @NotNull(message = "amount is required")
    private BigDecimal amount;

    public InsuranceClaim() {}
    public InsuranceClaim(Car car, LocalDate claimDate, String description, BigDecimal amount) {
        this.car = car; this.claimDate = claimDate; this.description = description; this.amount = amount;
    }

    public Long getId() { return id; }
    public Car getCar() { return car; }
    public void setCar(Car car) { this.car = car; }
    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
