package com.haulmont.testtask.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="credit_proposition")
public class CreditProposition {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @NotNull
    private BigDecimal creditAmount;

    @NotNull
    private Integer creditTerm;

    @NotNull
    private BigDecimal totalInterests;

    @OneToOne
    @JoinColumn(name = "payment_schedule_id")
    private PaymentSchedule paymentSchedule;

    @ManyToOne
    @JoinColumn(name="bank_id", nullable=false)
    private Bank bank;

    public CreditProposition(){

    }

    public PaymentSchedule getPaymentSchedule() {
        return paymentSchedule;
    }

    public Credit getCredit() {
        return credit;
    }

    public Customer getCustomer() {
        return customer;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public Integer getCreditTerm() {
        return creditTerm;
    }

    public void setCreditTerm(Integer creditTerm) {
        this.creditTerm = creditTerm;
    }

    public void setPaymentSchedule(PaymentSchedule paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }

    public BigDecimal getTotalInterests() {
        return totalInterests;
    }

    public void setTotalInterests(BigDecimal totalInterests) {
        this.totalInterests = totalInterests;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreditProposition that = (CreditProposition) o;
        return id.equals(that.id) && customer.equals(that.customer) && credit.equals(that.credit)
                && creditAmount.equals(that.creditAmount)
                && creditTerm.equals(that.creditTerm) && totalInterests.equals(that.totalInterests)
                && Objects.equals(paymentSchedule, that.paymentSchedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer, credit, creditAmount, creditTerm, paymentSchedule);
    }
}