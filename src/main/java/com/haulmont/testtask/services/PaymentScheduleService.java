package com.haulmont.testtask.services;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.PaymentRecord;
import com.haulmont.testtask.models.PaymentSchedule;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentScheduleService {
    boolean create(PaymentSchedule paymentSchedule);
    boolean delete(PaymentSchedule paymentSchedule);
    boolean update(PaymentSchedule paymentSchedule);
    List<PaymentRecord> calculatePaymentRecords(Credit credit, BigDecimal creditAmount, Integer creditTerm);
    boolean createScheduleWithPaymentRecords(PaymentSchedule paymentSchedule, List<PaymentRecord> paymentRecords);
}