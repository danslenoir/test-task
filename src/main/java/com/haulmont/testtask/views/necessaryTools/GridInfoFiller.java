package com.haulmont.testtask.views.necessaryTools;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.models.PaymentRecord;
import com.vaadin.flow.component.grid.Grid;

public class GridInfoFiller {
    public static void fillCustomerInfoGrid(Grid<Customer> customerGrid){
        customerGrid.addColumn(Customer::getSurname).setHeader("Фамилия").setSortable(true);
        customerGrid.addColumn(Customer::getName).setHeader("Имя").setSortable(true);
        customerGrid.addColumn(Customer::getPatronymic).setHeader("Отчество").setSortable(true);
        customerGrid.addColumn(Customer::getPassportNumber).setHeader("Номер паспорта").setSortable(true);
        customerGrid.addColumn(Customer::getEmail).setHeader("E-mail").setSortable(true);
    }

    public static void fillCreditInfoGrid(Grid<Credit> creditGrid){
        creditGrid.addColumn(Credit::getCreditLimit).setHeader("Лимит по кредиту").setSortable(true);
        creditGrid.addColumn(Credit::getRatePercent).setHeader("Процент по кредиту").setSortable(true);
    }

    public static void fillPaymentRecordGrid(Grid<PaymentRecord> paymentRecordGrid){
        paymentRecordGrid.addColumn(PaymentRecord::getDate).setHeader("Дата платежа").setSortable(true);
        paymentRecordGrid.addColumn(PaymentRecord::getPayment).setHeader("Сумма платежа").setSortable(true);
        paymentRecordGrid.addColumn(PaymentRecord::getRepaymentAmountOfTheLoanBody)
                .setHeader("Сумма гашения тела кредита").setSortable(true);
        paymentRecordGrid.addColumn(PaymentRecord::getInterestRepaymentAmount)
                .setHeader("Сумма гашения процентов").setSortable(true);
    }
}