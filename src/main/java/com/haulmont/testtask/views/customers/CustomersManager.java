package com.haulmont.testtask.views.customers;

import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.services.CustomerService;
import com.haulmont.testtask.views.necessaryTools.Constants;
import com.haulmont.testtask.views.MainViewLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import java.util.Optional;
import java.util.UUID;


@Route(value = "manageCustomer", layout = MainViewLayout.class)
public class CustomersManager extends AppLayout implements HasUrlParameter<String> {
    Label creatingLabel = new Label("Создание клиента");
    Label editingLabel = new Label("Редактирование клиента");
    VerticalLayout layout;
    UUID id;
    FormLayout customerForm;
    TextField surname;
    TextField name;
    TextField patronymic;
    TextField email;
    TextField passportNumber;
    Button saveCustomer;

    final CustomerService customerService;

    public CustomersManager(CustomerService customerService){
        this.customerService = customerService;
        customerForm = new FormLayout();
        surname = new TextField("Фамилия");
        name = new TextField("Имя");
        patronymic = new TextField("Отчество");
        email = new TextField("Email");
        passportNumber = new TextField("Номер паспорта");
        saveCustomer = new Button("Сохранить");
        customerForm.add(surname, name, patronymic, email, passportNumber, saveCustomer);
        layout = new VerticalLayout();
        layout.add(customerForm);
        setContent(layout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String customerUUID) {
        id = UUID.fromString(customerUUID);
        if (!id.equals(Constants.DEFAULT_UUID)) {
            layout.addComponentAtIndex(0, editingLabel);
        } else {
            layout.addComponentAtIndex(0, creatingLabel);
        }
        fillForm();
    }

    public void fillForm(){
        if (!id.equals(Constants.DEFAULT_UUID)) {
            Optional<Customer> customer = Optional.ofNullable(customerService.getById(id));
            customer.ifPresent(x -> {
                surname.setValue(x.getSurname());
                name.setValue(x.getName());
                patronymic.setValue(x.getPatronymic());
                passportNumber.setValue(x.getPassportNumber());
                email.setValue(x.getEmail());
            });
        }

        saveCustomer.addClickListener(clickEvent -> {
            Customer customer = new Customer();
            if (!id.equals(Constants.DEFAULT_UUID)) {
                customer.setId(id);
            }
            customer.setSurname(surname.getValue());
            customer.setName(name.getValue());
            customer.setPatronymic(patronymic.getValue());
            customer.setEmail(email.getValue());
            customer.setPassportNumber(passportNumber.getValue());
            customerService.createClient(customer);

            Notification notification = new Notification(id.equals(Constants.DEFAULT_UUID) ?
                    "Клиент успешно создан" : "Клиент был изменен", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(CustomersList.class);
            });
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            customerForm.setEnabled(false);
            notification.open();
        });
    }

}
