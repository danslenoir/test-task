package com.haulmont.testtask.services;

import com.haulmont.testtask.models.PaymentRecord;

import java.util.List;
import java.util.UUID;

public interface PaymentRecordService {
    boolean create(PaymentRecord paymentRecord);
    PaymentRecord getById(UUID id);
    boolean delete(PaymentRecord paymentRecord);
    boolean createAll(List<PaymentRecord> paymentRecordList);
}
