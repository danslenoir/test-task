package com.haulmont.testtask.services;

import com.haulmont.testtask.models.Bank;

import java.util.List;
import java.util.UUID;

public interface BankService {
    boolean create(Bank bank);
    boolean delete(Bank bank);
    Bank getById(UUID id);
    List<Bank> getAll();
}
