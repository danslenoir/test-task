package com.haulmont.testtask.views.creditPropositions;

import com.haulmont.testtask.models.CreditProposition;
import com.haulmont.testtask.models.PaymentRecord;
import com.haulmont.testtask.services.CreditPropositionService;
import com.haulmont.testtask.views.MainViewLayout;
import com.haulmont.testtask.views.necessaryTools.GridInfoFiller;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import java.util.UUID;

@Route(value = "creditPropositionDetails", layout = MainViewLayout.class)
public class CreditPropositionDetails extends AppLayout implements HasUrlParameter<String> {
    VerticalLayout layout;
    RouterLink linkEdit;
    Label customerLabel = new Label("О клиенте");
    Label surnameNamePatronymicLabel;
    Label passportNumberLabel;
    Label emailLabel;
    Label creditLabel = new Label("О кредите");
    Label creditLimitLabel;
    Label ratePercentLabel;
    Label creditAmountLabel;
    Label creditTermLabel;
    Label totalInterestsLabel;
    Grid<PaymentRecord> paymentRecordGrid;

    final CreditPropositionService creditPropositionService;

    public CreditPropositionDetails(CreditPropositionService creditPropositionService) {
        this.creditPropositionService = creditPropositionService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String stringUUID) {
        CreditProposition creditProposition = creditPropositionService.getById(UUID.fromString(stringUUID));
        layout = new VerticalLayout();
        linkEdit = new RouterLink("Редактировать кредитное предложение", CreditPropositionManager.class, stringUUID);

        surnameNamePatronymicLabel = new Label("ФИО: " + creditProposition.getCustomer().getSurname() + " "
        + creditProposition.getCustomer().getName() + " "
        + creditProposition.getCustomer().getPatronymic());
        passportNumberLabel = new Label("Номер паспорта: " + creditProposition.getCustomer().getPassportNumber());
        emailLabel = new Label("email: " + creditProposition.getCustomer().getEmail());

        creditLimitLabel = new Label("Лимит по кредиту: " + creditProposition.getCredit().getCreditLimit().toString());
        ratePercentLabel = new Label("Процентная ставка: " + creditProposition.getCredit().getRatePercent().toString());
        creditAmountLabel = new Label("Сумма кредита: " + creditProposition.getCreditAmount().toString());
        creditTermLabel = new Label("Срок, на который выдан кредит: " + creditProposition.getCreditTerm().toString());
        totalInterestsLabel = new Label("Итоговая процентная ставка по кредиту: " + creditProposition.getTotalInterests().toString());

        paymentRecordGrid = new Grid<>();
        fillPaymentRecordsGrid();
        paymentRecordGrid.setItems(creditProposition
                .getPaymentSchedule()
                .getPaymentRecords());

        layout.add(linkEdit);
        layout.add(customerLabel);
        layout.add(surnameNamePatronymicLabel);
        layout.add(passportNumberLabel);
        layout.add(emailLabel);
        layout.add(creditLabel);
        layout.add(creditLimitLabel);
        layout.add(ratePercentLabel);
        layout.add(creditAmountLabel);
        layout.add(creditTermLabel);
        layout.add(totalInterestsLabel);
        layout.add(paymentRecordGrid);
        setContent(layout);
    }

    private void fillPaymentRecordsGrid(){
        GridInfoFiller.fillPaymentRecordGrid(paymentRecordGrid);
    }
}