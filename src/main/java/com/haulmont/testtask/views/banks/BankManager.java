package com.haulmont.testtask.views.banks;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.services.BankService;
import com.haulmont.testtask.services.CreditService;
import com.haulmont.testtask.services.CustomerService;
import com.haulmont.testtask.views.MainViewLayout;
import com.haulmont.testtask.views.necessaryTools.GridInfoFiller;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.haulmont.testtask.views.necessaryTools.Constants.DEFAULT_UUID;

@Route(value = "manageBank", layout = MainViewLayout.class)
public class BankManager extends AppLayout implements HasUrlParameter<String> {
    private VerticalLayout layout;
    private FormLayout bankForm;
    private TextField nameTextField;
    private Button saveBank;
    private Label creating = new Label("Создание банка");
    private Label editing = new Label("Редактирование банка");

    private UUID id;

    private Button addCustomerButton;
    private ComboBox<Customer> customerComboBox;
    private Grid<Customer> customerGrid;
    private List<Customer> customerList;

    private Button addCreditButton;
    private ComboBox<Credit> creditComboBox;
    private Grid<Credit> creditGrid;
    private List<Credit> creditList;

    private final BankService bankService;
    private final CustomerService customerService;
    private final CreditService creditService;

    boolean isCustomerGridFilled;
    boolean isCreditGridFilled;

    List<Customer> proposedToAdditionCustomers;
    List<Credit> proposedToAdditionCredits;

    public BankManager(BankService bankService, CustomerService customerService, CreditService creditService){
        this.bankService = bankService;
        this.customerService = customerService;
        this.creditService = creditService;

        isCustomerGridFilled = false;
        isCreditGridFilled = false;

        this.layout = new VerticalLayout();
        this.bankForm = new FormLayout();
        this.nameTextField = new TextField("Название банка");
        nameTextField.setRequired(true);

        customerComboBox = new ComboBox<>("Выбрать клиента");
        customerGrid = new Grid<>();
        addCustomerButton = new Button("Добавить выбранного клиента");
        customerList = new ArrayList<>();

        creditGrid = new Grid<>();
        creditComboBox = new ComboBox<>("Выбрать кредит");
        addCreditButton = new Button("Добавить выбранный кредит");
        creditList = new ArrayList<>();

        this.saveBank = new Button("Сохранить банк");
        bankForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));
        bankForm.addComponentAtIndex(0, nameTextField);
        bankForm.addComponentAtIndex(1, customerGrid);
        bankForm.addComponentAtIndex(2, customerComboBox);
        bankForm.addComponentAtIndex(3, addCustomerButton);
        bankForm.addComponentAtIndex(4, creditGrid);
        bankForm.addComponentAtIndex(5, creditComboBox);
        bankForm.addComponentAtIndex(6, addCreditButton);
        bankForm.addComponentAtIndex(7,saveBank);
        layout.add(bankForm);
        setContent(layout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String stringUUID) {
        id = UUID.fromString(stringUUID);
        if(!id.equals(DEFAULT_UUID)){
            layout.addComponentAtIndex(0, editing);
        } else {
            layout.addComponentAtIndex(0, creating);
        }
        fillForm();
    }

    private void fillForm(){
        proposedToAdditionCustomers = new ArrayList<>(customerService.getAll());
        proposedToAdditionCredits = new ArrayList<>(creditService.getAll());

        if(!id.equals(DEFAULT_UUID)) {
            Bank bank = bankService.getById(id);
            nameTextField.setValue(bank.getName());
            customerList = new ArrayList<>(bank.getCustomers());
            if (!customerList.isEmpty()) {
                if(!isCustomerGridFilled) {
                    fillCustomerGrid();
                }
            }
            customerGrid.setItems(customerList);
            removeFromFirstCustomerListElementsOfSecondList(proposedToAdditionCustomers, customerList);

            creditList = new ArrayList<>(bank.getCredits());
            if (!creditList.isEmpty()) {
                if(!isCreditGridFilled) {
                    fillCreditGrid();
                }
            }
            creditGrid.setItems(creditList);
            removeFromFirstCreditListElementsOfSecondList(proposedToAdditionCredits, creditList);
        }

        ComboBox.ItemFilter<Customer> customerFilter = (customer, filterString) -> (customer.getSurname() +
                " " + customer.getName() + " " + customer.getPatronymic() + " "
                + customer.getPassportNumber()).toLowerCase().contains(filterString.toLowerCase());
        customerComboBox.setItems(customerFilter, proposedToAdditionCustomers);
        customerComboBox.setItemLabelGenerator(customer ->
            customer.getSurname() + " " + customer.getName() + " " + customer.getPatronymic() + " "
                    + customer.getPassportNumber()
        );

        addCustomerButton.addClickListener(buttonClickEvent -> {
            customerList.add(customerComboBox.getValue());
            if(!isCustomerGridFilled) {
                fillCustomerGrid();
            }
            customerGrid.setItems(customerList);
            proposedToAdditionCustomers.remove(customerComboBox.getValue());
            customerComboBox.setItems(proposedToAdditionCustomers);
        });

        ComboBox.ItemFilter<Credit> creditItemFilter = (credit, filterString) ->(
                credit.getCreditLimit().toString() + " " + credit.getRatePercent().toString())
                .toLowerCase().contains(filterString);
        creditComboBox.setItems(creditItemFilter, proposedToAdditionCredits);
        creditComboBox.setItemLabelGenerator(credit ->
                credit.getCreditLimit().toString() + " " + credit.getRatePercent().toString());

        addCreditButton.addClickListener(buttonClickEvent -> {
            creditList.add(creditComboBox.getValue());
            if(!isCreditGridFilled) {
                fillCreditGrid();
            }
            creditGrid.setItems(creditList);
            proposedToAdditionCredits.remove(creditComboBox.getValue());
            creditComboBox.setItems(proposedToAdditionCredits);
        });

        addClickListenerToSaveBankButton();
    }

    private void fillCustomerGrid(){
        GridInfoFiller.fillCustomerInfoGrid(customerGrid);
        customerGrid.addColumn(new NativeButtonRenderer<>("Удалить", customer -> {
            Dialog dialog = new Dialog();
            Button confirm = new Button("Удалить");
            Button cancel = new Button("Отмена");
            dialog.add("Вы уверены что хотите удалить клиента из списка?");
            dialog.add(confirm);
            dialog.add(cancel);

            confirm.addClickListener(clickEvent -> {
                proposedToAdditionCustomers.add(customer);
                customerList.remove(customer);
                dialog.close();
                Notification notification = new Notification("Клиент удален из списка", 1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();

                customerGrid.setItems(customerList);
                customerComboBox.setItems(proposedToAdditionCustomers);
            });
            cancel.addClickListener(clickEvent -> {
                dialog.close();
            });
            dialog.open();
        }));

        isCustomerGridFilled = true;
    }

    private void fillCreditGrid(){
        GridInfoFiller.fillCreditInfoGrid(creditGrid);
        creditGrid.addColumn(new NativeButtonRenderer<>("Удалить", credit -> {
            Dialog dialog = new Dialog();
            Button confirm = new Button("Удалить");
            Button cancel = new Button("Отмена");
            dialog.add("Вы уверены что хотите удалить кредит из списка?");
            dialog.add(confirm);
            dialog.add(cancel);

            confirm.addClickListener(clickEvent -> {
                proposedToAdditionCredits.add(credit);
                creditList.remove(credit);
                dialog.close();
                Notification notification = new Notification("Кредит удален из списка", 1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();

                creditGrid.setItems(creditList);
                creditComboBox.setItems(proposedToAdditionCredits);
            });
            cancel.addClickListener(clickEvent -> {
                dialog.close();
            });
            dialog.open();
        }));

        isCreditGridFilled = true;
    }

    private void addClickListenerToSaveBankButton(){
        saveBank.addClickListener(clickEvent -> {
            Bank bank = new Bank();
            if(!id.equals(DEFAULT_UUID)){
                bank.setId(id);
            }
            bank.setName(nameTextField.getValue());
            bank.setCustomers(customerList);
            bank.setCredits(creditList);
            bankService.create(bank);

            clearListsGridsComboBoxes();

            Notification notification = new Notification( id.equals(DEFAULT_UUID) ?
                    "Банк успешно создан" : "Банк успешно отредактирован", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(BankList.class);
            });
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            bankForm.setEnabled(false);
            notification.open();
        });
    }

    private void clearListsGridsComboBoxes(){
        customerComboBox.clear();
        creditComboBox.clear();
        customerList.clear();
        creditList.clear();
        customerGrid.setItems(customerList);
        creditGrid.setItems(creditList);
        isCustomerGridFilled = false;
        isCreditGridFilled = false;
    }

    private void removeFromFirstCustomerListElementsOfSecondList(List<Customer> first, List<Customer> second){
        for(Customer customer: second){
            first.remove(customer);
        }
    }

    private void removeFromFirstCreditListElementsOfSecondList(List<Credit> first, List<Credit> second){
        for(Credit credit: second){
            first.remove(credit);
        }
    }
}