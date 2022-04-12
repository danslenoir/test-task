package com.haulmont.testtask.views.banks;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.services.BankService;
import com.haulmont.testtask.views.MainViewLayout;
import com.haulmont.testtask.views.necessaryTools.GridInfoFiller;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.List;
import java.util.UUID;

@Route(value = "bankDetails", layout = MainViewLayout.class)
public class BankDetails extends AppLayout implements HasUrlParameter<String> {
    Label bankNameLabel;
    Details customers;
    Grid<Customer> customerGrid = new Grid<>();
    Details credits;
    Grid<Credit> creditGrid = new Grid<>();
    VerticalLayout layout;
    RouterLink linkEdit;


    final BankService bankService;

    public BankDetails(BankService bankService) {
        this.bankService = bankService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String uuid) {
        Bank bank = bankService.getById(UUID.fromString(uuid));
        bankNameLabel = new Label("Название банка: "+bank.getName());
        fillCustomerDetails(bank.getCustomers());
        fillCreditDetails(bank.getCredits());
        layout = new VerticalLayout();
        linkEdit = new RouterLink("Редактировать банк", BankManager.class, uuid);
        layout.add(linkEdit);
        layout.add(bankNameLabel);
        layout.add(customers);
        layout.add(credits);
        setContent(layout);
    }

    private void fillCustomerDetails(List<Customer> customersList){
        UnorderedList content = new UnorderedList();
        fillCustomerGrid(customersList);
        content.add(customerGrid);
        customers = new Details("Показать клиентов банка", content);
        customers.setOpened(false);
        customerGrid.setWidthFull();
        customers.setWidthFull();
    }

    private void fillCustomerGrid(List<Customer> customerList){
        if(!customerList.isEmpty()){
            GridInfoFiller.fillCustomerInfoGrid(customerGrid);
        }
        customerGrid.setItems(customerList);
    }

    private void fillCreditDetails(List<Credit> creditsList){
        UnorderedList content = new UnorderedList();
        fillCreditGrid(creditsList);
        content.add(creditGrid);
        credits = new Details("Показать кредиты банка", content);
        credits.setOpened(false);
        creditGrid.setWidthFull();
        credits.setWidthFull();
    }

    private void fillCreditGrid(List<Credit> creditList){
        if(!creditList.isEmpty()){
            GridInfoFiller.fillCreditInfoGrid(creditGrid);
        }
        creditGrid.setItems(creditList);
    }
}