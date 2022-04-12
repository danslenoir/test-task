package com.haulmont.testtask.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "BANKS")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @NotNull
    private String name;

    @ManyToMany
    @JoinTable(name = "banks_customers",
            joinColumns = @JoinColumn(name = "bank_id"),
            inverseJoinColumns = @JoinColumn(name="customers_id"))
    private List<Customer> customers = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "banks_credits",
            joinColumns = @JoinColumn(name = "bank_id"),
            inverseJoinColumns = @JoinColumn(name="credits_id"))
    private List<Credit> credits = new ArrayList<>();

    @OneToMany(mappedBy="bank")
    private List<CreditProposition> creditPropositions = new ArrayList<>();

    public List<Credit> getCredits() {
        return credits;
    }

    public void setCredits(List<Credit> credits) {
        this.credits = credits;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CreditProposition> getCreditPropositions() {
        return creditPropositions;
    }

    public void setCreditPropositions(List<CreditProposition> creditPropositions) {
        this.creditPropositions = creditPropositions;
    }

    public Bank(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return id.equals(bank.id) && name.equals(bank.name) && Objects.equals(customers, bank.customers) && Objects.equals(credits, bank.credits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, customers, credits);
    }
}