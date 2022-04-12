package com.haulmont.testtask.views.credits;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.services.CreditService;
import com.haulmont.testtask.views.MainViewLayout;
import com.haulmont.testtask.views.necessaryTools.Constants;
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

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Route(value = "manageCredit", layout = MainViewLayout.class)
public class CreditsManager extends AppLayout implements HasUrlParameter<String> {
    final static Label CREATING_LABEl = new Label("Создание кредит");
    final static Label EDITING_LABEL = new Label("Редактирование кредит");
    VerticalLayout layout;

    UUID id;
    FormLayout creditForm;
    TextField creditLimit;
    TextField ratePercent;
    Button saveCredit;

    final CreditService creditService;

    public CreditsManager(CreditService creditService){
        this.creditService = creditService;
        creditForm = new FormLayout();
        creditLimit = new TextField("Лимит по кредиту");
        ratePercent = new TextField("Процент по кредиту");
        saveCredit = new Button("Сохранить");
        creditForm.add(creditLimit, ratePercent, saveCredit);
        layout = new VerticalLayout();
        layout.add(creditForm);
        setContent(layout);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String customerUUID){
        id = UUID.fromString(customerUUID);
        if (!id.equals(Constants.DEFAULT_UUID)) {
            layout.addComponentAsFirst(EDITING_LABEL);
        } else {
            layout.addComponentAsFirst(CREATING_LABEl);
        }
        fillForm();
    }

    void fillForm(){
        if (!id.equals(Constants.DEFAULT_UUID)) {
            Optional<Credit> credit = Optional.ofNullable(creditService.getById(id));
            credit.ifPresent(x -> {
                creditLimit.setValue(x.getCreditLimit().toString());
                ratePercent.setValue(x.getRatePercent().toString());
            });
        }

        saveCredit.addClickListener(clickEvent -> {
            Credit credit = new Credit();
            if (!id.equals(Constants.DEFAULT_UUID)) {
                credit.setId(id);
            }
            credit.setCreditLimit(new BigDecimal(creditLimit.getValue()));
            credit.setRatePercent(new BigDecimal(ratePercent.getValue()));
            creditService.createCredit(credit);

            Notification notification = new Notification(id.equals(Constants.DEFAULT_UUID) ?
                    "Кредит успешно создан" : "Кредит был изменен", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(CreditList.class);
            });
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            creditForm.setEnabled(false);
            notification.open();
        });
    }

}
