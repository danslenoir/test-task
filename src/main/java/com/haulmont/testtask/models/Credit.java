package com.haulmont.testtask.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name="credits")
public class Credit {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @PositiveOrZero
    private BigDecimal creditLimit;
    @PositiveOrZero
    private BigDecimal ratePercent;

    @ManyToMany(mappedBy = "credits")
    List<Bank> bankList = new ArrayList<>();

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setRatePercent(BigDecimal ratePercent) {
        this.ratePercent = ratePercent;
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getRatePercent() {
        return ratePercent;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public List<Bank> getBankList() {
        return bankList;
    }

    public void setBankList(List<Bank> bankList) {
        this.bankList = bankList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return id.equals(credit.id) && creditLimit.equals(credit.creditLimit) && ratePercent.equals(credit.ratePercent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creditLimit, ratePercent);
    }
}