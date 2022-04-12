package com.haulmont.testtask.services;

import com.haulmont.testtask.models.CreditProposition;

import java.util.List;
import java.util.UUID;

public interface CreditPropositionService {
    List<CreditProposition> getAll();
    CreditProposition getById(UUID id);
    boolean createWithScheduleAndPaymentRecords(CreditProposition creditProposition);
    boolean create(CreditProposition creditProposition);
    boolean delete(CreditProposition creditProposition);
}
