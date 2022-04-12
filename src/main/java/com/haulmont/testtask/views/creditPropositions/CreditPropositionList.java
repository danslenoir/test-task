package com.haulmont.testtask.views.creditPropositions;

import com.haulmont.testtask.models.CreditProposition;
import com.haulmont.testtask.models.PaymentRecord;
import com.haulmont.testtask.services.CreditPropositionService;
import com.haulmont.testtask.services.PaymentRecordService;
import com.haulmont.testtask.services.PaymentScheduleService;
import com.haulmont.testtask.views.MainViewLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.haulmont.testtask.views.necessaryTools.Constants.STRING_DEFAULT_UUID;

@Route(value = "creditsPropositions", layout = MainViewLayout.class)
@PageTitle("Кредитные предложения")
public class CreditPropositionList extends AppLayout {
    final CreditPropositionService creditPropositionService;
    final PaymentScheduleService paymentScheduleService;
    final PaymentRecordService paymentRecordService;
    VerticalLayout layout;
    RouterLink linkCreate;
    Grid<CreditProposition> grid;

    public CreditPropositionList(CreditPropositionService creditPropositionService, PaymentScheduleService paymentScheduleService, PaymentRecordService paymentRecordService){
        this.creditPropositionService = creditPropositionService;
        this.paymentScheduleService = paymentScheduleService;
        this.paymentRecordService = paymentRecordService;
        layout = new VerticalLayout();
        linkCreate = new RouterLink("Создать кредитное предложение", CreditPropositionManager.class, STRING_DEFAULT_UUID);
        grid = new Grid<>();
        layout.add(linkCreate);
        layout.add(grid);
        setContent(layout);
    }

    @PostConstruct
    private void fillGrid(){
        List<CreditProposition> creditPropositionsList = creditPropositionService.getAll();
        if(!creditPropositionsList.isEmpty()){
            grid.addColumn(creditProposition -> creditProposition.getCustomer().getSurname())
                    .setHeader("Фамилия клиента").setSortable(true);
            grid.addColumn(creditProposition -> creditProposition.getCustomer().getName())
                    .setHeader("Имя клиента").setSortable(true);
            grid.addColumn(creditProposition -> creditProposition.getCustomer().getPatronymic())
                    .setHeader("Отчество клиента").setSortable(true);
            grid.addColumn(creditProposition -> creditProposition.getCredit().getRatePercent())
                    .setHeader("Процентная ставка").setSortable(true);
            grid.addColumn(CreditProposition::getCreditAmount).setHeader("Сумма кредита").setSortable(true);
            grid.addColumn(CreditProposition::getCreditTerm).setHeader("Срок кредита").setSortable(true);
        }

        grid.addColumn(new NativeButtonRenderer<>("Открыть подробности", creditProposition -> {
            UI.getCurrent().navigate(CreditPropositionDetails.class, creditProposition.getId().toString());
        }));

        grid.addColumn(new NativeButtonRenderer<>("Редактировать", creditProposition -> {
            UI.getCurrent().navigate(CreditPropositionManager.class, creditProposition.getId().toString());
        }));

        grid.addColumn(new NativeButtonRenderer<>("Удалить", creditProposition -> {
            Dialog dialog = new Dialog();
            Button confirm = new Button("Удалить");
            Button cancel = new Button("Отмена");
            dialog.add("Вы уверены что хотите удалить кредитное предложение? Будет удалён и график платежей.");
            dialog.add(confirm);
            dialog.add(cancel);

            confirm.addClickListener(clickEvent -> {
                creditPropositionService.delete(creditProposition);
                for(PaymentRecord pR: creditProposition.getPaymentSchedule().getPaymentRecords()) {
                    paymentRecordService.delete(pR);
                }
                paymentScheduleService.delete(creditProposition.getPaymentSchedule());
                dialog.close();
                Notification notification = new Notification("Кредитное предложение удалено", 1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();

                grid.setItems(creditPropositionService.getAll());

            });

            cancel.addClickListener(clickEvent -> {
                dialog.close();
            });

            dialog.open();

        }));

        grid.setItems(creditPropositionsList);
    }
}
