package com.haulmont.testtask.views.credits;

import com.haulmont.testtask.models.Credit;
import com.haulmont.testtask.services.CreditService;
import com.haulmont.testtask.views.necessaryTools.Constants;
import com.haulmont.testtask.views.MainViewLayout;
import com.haulmont.testtask.views.necessaryTools.GridInfoFiller;
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

@Route(value = "credits", layout = MainViewLayout.class)
@PageTitle("Список кредитов")
public class CreditList extends AppLayout {
    VerticalLayout layout;
    Grid<Credit> grid;
    RouterLink linkCreate;
    final CreditService creditService;

    public CreditList(CreditService service) {
        this.creditService = service;
        layout = new VerticalLayout();
        grid = new Grid<>();
        linkCreate = new RouterLink("Создать кредит", CreditsManager.class, Constants.STRING_DEFAULT_UUID);
        layout.add(linkCreate);
        layout.add(grid);
        setContent(layout);
    }

    @PostConstruct
    public void fillGrid() {
        List<Credit> creditList = creditService.getAll();
        if (!creditList.isEmpty()) {
            GridInfoFiller.fillCreditInfoGrid(grid);
        }

        grid.addColumn(new NativeButtonRenderer<>("Редактировать", credit -> {
            UI.getCurrent().navigate(CreditsManager.class, credit.getId().toString());
        }));
        grid.addColumn(new NativeButtonRenderer<>("Удалить", credit -> {
            Dialog dialog = new Dialog();
            Button confirm = new Button("Удалить");
            Button cancel = new Button("Отмена");
            dialog.add("Вы уверены что хотите удалить кредит?");
            dialog.add(confirm);
            dialog.add(cancel);

            confirm.addClickListener(clickEvent -> {
                creditService.deleteCredit(credit);
                dialog.close();
                Notification notification = new Notification("Кредит удален", 1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();

                grid.setItems(creditService.getAll());

            });

            cancel.addClickListener(clickEvent -> {
                dialog.close();
            });

            dialog.open();

        }));

        grid.setItems(creditList);
    }
}
