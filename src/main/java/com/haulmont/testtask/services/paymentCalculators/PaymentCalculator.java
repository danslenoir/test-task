package com.haulmont.testtask.services.paymentCalculators;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.PaymentRecord;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentCalculator {
    List<PaymentRecord> getFilledPaymentRecordsForCredit(Credit credit, BigDecimal creditAmount, Integer creditTerm);
    BigDecimal calculateTotalInterestsOnTheLoan(BigDecimal creditAmount, List<PaymentRecord> paymentRecords);
}
