package com.haulmont.testtask.services.paymentCalculators.impl;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.PaymentRecord;
import com.haulmont.testtask.services.paymentCalculators.PaymentCalculator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Component
public class DifferentialPaymentCalculator implements PaymentCalculator {
    static BigDecimal TWELVE = BigDecimal.valueOf(12L, 0);
    static BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);
    static BigDecimal TWELVE_HUNDRED = TWELVE.multiply(HUNDRED);

    public DifferentialPaymentCalculator(){

    }

    @Override
    public List<PaymentRecord> getFilledPaymentRecordsForCredit(Credit credit, BigDecimal creditAmount, Integer creditTerm) {
        List<PaymentRecord> paymentRecords = new ArrayList<>(creditTerm);
        BigDecimal loanPrincipalBalance = creditAmount;
        BigDecimal amountOfLoanBody = calculateAmountOfLoanBody(creditAmount, creditTerm);
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        Date date = Date.valueOf(LocalDate.now().plusMonths(1));
        for (int month = creditTerm; month > 0; month--){
            PaymentRecord paymentRecord = new PaymentRecord();
            if(!date.toLocalDate().getMonth().equals(Month.FEBRUARY)){
                date = Date.valueOf(date.toLocalDate().withDayOfMonth(dayOfMonth));
            }
            paymentRecord.setDate(date);
            paymentRecord.setRepaymentAmountOfTheLoanBody(amountOfLoanBody);
            BigDecimal interestRepayment = calculateInterestRepaymentAmount(loanPrincipalBalance, credit.getRatePercent());
            paymentRecord.setInterestRepaymentAmount(interestRepayment);
            paymentRecord.setPayment(amountOfLoanBody.add(interestRepayment));
            loanPrincipalBalance = loanPrincipalBalance.subtract(amountOfLoanBody);
            paymentRecords.add(paymentRecord);
            date = Date.valueOf(date.toLocalDate().plusMonths(1));
        }

        if(loanPrincipalBalance.doubleValue()>0){
            System.out.println("CALCULATE ERROR! Balance: " + loanPrincipalBalance);
        }
        return paymentRecords;
    }

    private BigDecimal calculateAmountOfLoanBody(BigDecimal creditAmount, Integer creditTerm){
            return creditAmount.divide(BigDecimal.valueOf(creditTerm), RoundingMode.CEILING);
    }

    private BigDecimal calculateInterestRepaymentAmount(BigDecimal loanPrincipalBalance, BigDecimal ratePercent){
        return loanPrincipalBalance.multiply(ratePercent.divide(TWELVE_HUNDRED, RoundingMode.CEILING));
    }

    public BigDecimal calculateTotalInterestsOnTheLoan(BigDecimal creditAmount, List<PaymentRecord> paymentRecords){
        BigDecimal sumOfInterestRepayments = BigDecimal.ZERO;
        for(PaymentRecord paymentRecord: paymentRecords){
            sumOfInterestRepayments = sumOfInterestRepayments.add(paymentRecord.getInterestRepaymentAmount());
        }
        return sumOfInterestRepayments.divide(creditAmount).multiply(HUNDRED);
    }
}