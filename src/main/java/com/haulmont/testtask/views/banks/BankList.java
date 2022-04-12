package com.haulmont.testtask.views.banks;

import com.haulmont.testtask.models.Bank;
import com.haulmont.testtask.services.BankService;
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

@Route(value = "banks", layout = MainViewLayout.class)
@PageTitle("Список банков")
public class BankList extends AppLayout {
    VerticalLayout layout;
    Grid<Bank> grid;
    RouterLink linkCreate;

    final BankService bankService;

    public BankList(BankService bankService){
        this.bankService = bankService;
        layout = new VerticalLayout();
        grid = new Grid<>();
        linkCreate = new RouterLink("Создать банк", BankManager.class, STRING_DEFAULT_UUID);
        layout.add(linkCreate);
        layout.add(grid);
        setContent(layout);
    }

    @PostConstruct
    public void fillGrid(){
        List<Bank> bankList = bankService.getAll();
        if(!bankList.isEmpty()){
            grid.addColumn(Bank::getName).setHeader("Название").setSortable(true);
        }

        grid.addColumn(new NativeButtonRenderer<>("Открыть подробности", bank -> {
            UI.getCurrent().navigate(BankDetails.class, bank.getId().toString());
        }));

        grid.addColumn(new NativeButtonRenderer<>("Редактировать", bank -> {
            UI.getCurrent().navigate(BankManager.class, bank.getId().toString());
        }));

        grid.addColumn(new NativeButtonRenderer<>("Удалить", bank -> {
            Dialog dialog = new Dialog();
            Button confirm = new Button("Удалить");
            Button cancel = new Button("Отмена");
            dialog.add("Вы уверены что хотите удалить банк?");
            dialog.add(confirm);
            dialog.add(cancel);

            confirm.addClickListener(clickEvent -> {
                bankService.delete(bank);
                dialog.close();
                Notification notification = new Notification("Банк удален", 1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();

                grid.setItems(bankService.getAll());

            });

            cancel.addClickListener(clickEvent -> {
                dialog.close();
            });

            dialog.open();

        }));

        grid.setItems(bankList);

    }
}