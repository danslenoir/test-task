package com.haulmont.testtask.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "payment_records")
public class PaymentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private UUID id;

    private Date date;

    private BigDecimal payment;

    private BigDecimal repaymentAmountOfTheLoanBody;

    private BigDecimal interestRepaymentAmount;

    @ManyToOne
    @JoinColumn(name = "payment_schedule_id")
    private PaymentSchedule paymentSchedule;

    public PaymentSchedule getPaymentSchedule() {
        return paymentSchedule;
    }

    public void setPaymentSchedule(PaymentSchedule paymentSchedule) {
        this.paymentSchedule = paymentSchedule;
    }

    public PaymentRecord(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public BigDecimal getPayment() {
        return payment;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public BigDecimal getRepaymentAmountOfTheLoanBody() {
        return repaymentAmountOfTheLoanBody;
    }

    public void setRepaymentAmountOfTheLoanBody(BigDecimal repaymentAmountOfTheLoanBody) {
        this.repaymentAmountOfTheLoanBody = repaymentAmountOfTheLoanBody;
    }

    public BigDecimal getInterestRepaymentAmount() {
        return interestRepaymentAmount;
    }

    public void setInterestRepaymentAmount(BigDecimal interestRepaymentAmount) {
        this.interestRepaymentAmount = interestRepaymentAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRecord that = (PaymentRecord) o;
        return id.equals(that.id) && date.equals(that.date) && payment.equals(that.payment) && repaymentAmountOfTheLoanBody.equals(that.repaymentAmountOfTheLoanBody) && interestRepaymentAmount.equals(that.interestRepaymentAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, payment, repaymentAmountOfTheLoanBody, interestRepaymentAmount);
    }

    @Override
    public String toString() {
        return "PaymentRecord{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", payment=" + payment +
                ", repaymentAmountOfTheLoanBody=" + repaymentAmountOfTheLoanBody +
                ", interestRepaymentAmount=" + interestRepaymentAmount +
                ", paymentSchedule=" + paymentSchedule +
                '}';
    }
}
