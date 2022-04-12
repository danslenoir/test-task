package com.haulmont.testtask.services;

import com.haulmont.testtask.models.Credit;

import java.util.List;
import java.util.UUID;

public interface CreditService {
    Credit getById(UUID id);
    boolean createCredit(Credit credit);
    boolean deleteCredit(Credit credit);
    List<Credit> getAll();
}
