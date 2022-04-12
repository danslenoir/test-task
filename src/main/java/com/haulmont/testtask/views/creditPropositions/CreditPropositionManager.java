package com.haulmont.testtask.views.creditPropositions;

import com.haulmont.testtask.models.*;
import com.haulmont.testtask.services.BankService;
import com.haulmont.testtask.services.CreditPropositionService;
import com.haulmont.testtask.services.PaymentRecordService;
import com.haulmont.testtask.services.PaymentScheduleService;
import com.haulmont.testtask.views.necessaryTools.Constants;
import com.haulmont.testtask.views.MainViewLayout;
import com.haulmont.testtask.views.necessaryTools.GridInfoFiller;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.UUID;

import static com.haulmont.testtask.views.necessaryTools.Constants.DEFAULT_UUID;

@Route(value = "manageCreditProposition", layout = MainViewLayout.class)
public class CreditPropositionManager extends AppLayout implements HasUrlParameter<String> {
    final CreditPropositionService creditPropositionService;
    final PaymentScheduleService paymentScheduleService;
    final PaymentRecordService paymentRecordService;
    final BankService bankService;

    UUID id;
    CreditProposition editableCreditProposition;
    Label creatingLabel = new Label("Создание кредитного предложения");
    Label editingLabel = new Label("Редактирование кредитного предложения");
    Label bankSelectionLabel = new Label("Сначала выберите банк");
    Label messageForEditing = new Label("При изменении кредита, срока, на который выдаётся кредит, или суммы кредита" +
            " произойдёт пересоздание графика платежей. Сбросить изменения, вернув всё к исходному состоянию," +
            " можно до нажатия кнопки \"Сохранить\". Для этого нажмите на кнопку сброса изменений.");
    VerticalLayout layout;
    FormLayout creditPropositionForm;
    ComboBox<Bank> bankComboBox;
    ComboBox<Customer> customerComboBox;
    ComboBox<Credit> creditComboBox;
    BigDecimalField creditAmountTextField;
    IntegerField creditTermTextField;
    Grid<PaymentRecord> paymentRecordGrid;

    Button cancelEdit;

    Button saveButton;

    private boolean generatePaymentRecords = false;
    private boolean isPaymentGridFilled = false;

    public CreditPropositionManager(CreditPropositionService creditPropositionService, PaymentScheduleService paymentScheduleService, PaymentRecordService paymentRecordService, BankService bankService) {
        this.creditPropositionService = creditPropositionService;
        this.paymentScheduleService = paymentScheduleService;
        this.paymentRecordService = paymentRecordService;
        this.bankService = bankService;

        layout = new VerticalLayout();

        creditPropositionForm = new FormLayout();
        creditPropositionForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        bankComboBox = new ComboBox<>("Выбрать банк");
        customerComboBox = new ComboBox<>("Выбрать клиента");
        creditComboBox = new ComboBox<>("Выбрать кредит");
        creditAmountTextField = new BigDecimalField("Сумма кредита");
        creditTermTextField = new IntegerField("Срок, на который будет выдан кредит (количество месяцев)");
        paymentRecordGrid = new Grid<>();

        cancelEdit = new Button("Сбросить изменения");
        saveButton = new Button("Сохранить");

        creditPropositionForm.addComponentAtIndex(0, bankComboBox);
        creditPropositionForm.addComponentAtIndex(1, customerComboBox);
        creditPropositionForm.addComponentAtIndex(2, creditComboBox);
        creditPropositionForm.addComponentAtIndex(3, creditAmountTextField);
        creditPropositionForm.addComponentAtIndex(4, creditTermTextField);
        creditPropositionForm.addComponentAtIndex(5, paymentRecordGrid);
        creditPropositionForm.addComponentAtIndex(6, saveButton);

        layout.add(creditPropositionForm);
        setContent(layout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String stringUUID) {
        id = UUID.fromString(stringUUID);
        if(!id.equals(DEFAULT_UUID)){
            layout.addComponentAtIndex(0, editingLabel);
            layout.addComponentAtIndex(1, messageForEditing);
        } else {
            layout.addComponentAtIndex(0, creatingLabel);
            layout.addComponentAtIndex(1, bankSelectionLabel);
            creditPropositionForm.remove(paymentRecordGrid);
            generatePaymentRecords = true;
        }
        fillForm();
    }

    private void fillFieldsFromEditable(){
        bankComboBox.setValue(editableCreditProposition.getBank());
        customerComboBox.setValue(editableCreditProposition.getCustomer());
        creditComboBox.setValue(editableCreditProposition.getCredit());
        creditAmountTextField.setValue(editableCreditProposition.getCreditAmount());
        creditTermTextField.setValue(editableCreditProposition.getCreditTerm());
        if(!isPaymentGridFilled) {
            fillPaymentRecordGrid();
        }
        paymentRecordGrid.setItems(editableCreditProposition
                .getPaymentSchedule()
                .getPaymentRecords());

    }

    private void fillForm(){

        Notification generateNewPaymentRecordsNotification = new Notification("Будет составлен новый график платежей", 1000);
        generateNewPaymentRecordsNotification.setPosition(Notification.Position.MIDDLE);
        generateNewPaymentRecordsNotification.addDetachListener(detachEvent -> {
            generatePaymentRecords = true;
            creditPropositionForm.remove(paymentRecordGrid);
        });

        ComboBox.ItemFilter<Bank> bankFilter = (bank, filterString) ->
                bank.getName().toLowerCase().contains(filterString);
        bankComboBox.setItems(bankFilter, bankService.getAll());
        bankComboBox.setItemLabelGenerator(Bank::getName);

        ComboBox.ItemFilter<Customer> customerItemFilter = (customer, filterString) ->
                (customer.getSurname() + " " + customer.getName() + " "
                        + customer.getPatronymic() + " " + customer.getPassportNumber())
                        .toLowerCase().contains(filterString);
        customerComboBox.setItemLabelGenerator(customer ->
                customer.getSurname() + " " + customer.getName() + " " + customer.getPatronymic()
                        + " " + customer.getPassportNumber());
        customerComboBox.setItems(customerItemFilter, new ArrayList<>());

        ComboBox.ItemFilter<Credit> creditItemFilter = (credit, filterString) -> (credit.getCreditLimit().toString()
        + " " + credit.getRatePercent().toString()).toLowerCase().contains(filterString);
        creditComboBox.setItemLabelGenerator(credit ->
                credit.getCreditLimit().toString() + " " + credit.getRatePercent().toString());
        creditComboBox.setItems(creditItemFilter, new ArrayList<>());

        bankComboBox.addValueChangeListener(comboBoxCustomValueChangeEvent -> {
            customerComboBox.setItems(customerItemFilter, bankComboBox.getValue().getCustomers());
            creditComboBox.setItems(creditItemFilter, bankComboBox.getValue().getCredits());
        });

        if(!id.equals(DEFAULT_UUID)){
            editableCreditProposition = creditPropositionService.getById(id);
            fillFieldsFromEditable();
            layout.addComponentAtIndex(1, cancelEdit);
            cancelEdit.addClickListener(buttonClickEvent -> {
                fillFieldsFromEditable();
                paymentRecordGrid.setItems(editableCreditProposition.getPaymentSchedule().getPaymentRecords());
                creditPropositionForm.addComponentAtIndex(5, paymentRecordGrid);
            });
        }

        creditComboBox.addValueChangeListener(comboBoxCreditComponentValueChangeEvent -> {
            if(editableCreditProposition!=null){
                if(!editableCreditProposition.getCredit().equals(creditComboBox.getValue())){
                    generateNewPaymentRecordsNotification.open();
                }
            }
        });

        creditAmountTextField.addValueChangeListener(bigDecimalFieldBigDecimalComponentValueChangeEvent -> {
            if(editableCreditProposition!=null){
                if(!editableCreditProposition.getCreditAmount().equals(creditAmountTextField.getValue())){
                    generateNewPaymentRecordsNotification.open();
                }
            }
        });

        creditTermTextField.addValueChangeListener(integerFieldIntegerComponentValueChangeEvent -> {
            if(editableCreditProposition!=null){
                if(!editableCreditProposition.getCreditTerm().equals(creditTermTextField.getValue())){
                    generateNewPaymentRecordsNotification.open();
                }
            }
        });

        saveButton.addClickListener(buttonClickEvent -> {
           CreditProposition creditProposition = new CreditProposition();
           if(!id.equals(DEFAULT_UUID)){
               creditProposition.setId(id);
           }
           creditProposition.setCustomer(customerComboBox.getValue());
           creditProposition.setCredit(creditComboBox.getValue());
           creditProposition.setCreditAmount(creditAmountTextField.getValue());
           creditProposition.setCreditTerm(creditTermTextField.getValue());
           creditProposition.setBank(bankComboBox.getValue());
           if(generatePaymentRecords) {
               creditPropositionService.createWithScheduleAndPaymentRecords(creditProposition);
               if(!id.equals(DEFAULT_UUID)) {
                   if (editableCreditProposition != null) {
                       for (PaymentRecord pR : editableCreditProposition.getPaymentSchedule().getPaymentRecords()) {
                           paymentRecordService.delete(pR);
                       }
                       paymentScheduleService.delete(editableCreditProposition.getPaymentSchedule());
                   }
               }
           } else {
               creditProposition.setTotalInterests(editableCreditProposition.getTotalInterests());
               creditProposition.setPaymentSchedule(editableCreditProposition.getPaymentSchedule());
               creditPropositionService.create(creditProposition);
           }
           Notification notification = new Notification(id.equals(Constants.DEFAULT_UUID) ?
                    "Кредитное предложение успешно создано" : "Кредитное предложение было изменено", 1000);
           notification.setPosition(Notification.Position.MIDDLE);
           notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(CreditPropositionList.class);
           });
           notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
           notification.open();
           generatePaymentRecords = false;
           isPaymentGridFilled = false;
           layout.remove(messageForEditing);
        });
    }

    private void fillPaymentRecordGrid(){
        GridInfoFiller.fillPaymentRecordGrid(paymentRecordGrid);
        isPaymentGridFilled = true;
    }
}