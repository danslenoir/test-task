package com.haulmont.testtask.views.customers;

import com.haulmont.testtask.models.Customer;
import com.haulmont.testtask.services.CustomerService;
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

@Route(value = "customers", layout = MainViewLayout.class)
@PageTitle("Список клиентов")
public class CustomersList extends AppLayout {
    VerticalLayout layout;
    Grid<Customer> grid;
    RouterLink linkCreate;
    final CustomerService customerService;

    public CustomersList(CustomerService customerService) {
        this.customerService = customerService;
        layout = new VerticalLayout();
        grid = new Grid<>();
        linkCreate = new RouterLink("Создать клиента", CustomersManager.class, Constants.STRING_DEFAULT_UUID);
        layout.add(linkCreate);
        layout.add(grid);
        setContent(layout);
    }

    @PostConstruct
    public void fillGrid() {
        List<Customer> customerList = customerService.getAll();
        if (!customerList.isEmpty()){
            GridInfoFiller.fillCustomerInfoGrid(grid);
        }

        grid.addColumn(new NativeButtonRenderer<>("Редактировать", customer -> {
            UI.getCurrent().navigate(CustomersManager.class, customer.getId().toString());
        }));
        grid.addColumn(new NativeButtonRenderer<>("Удалить", customer -> {
            Dialog dialog = new Dialog();
            Button confirm = new Button("Удалить");
            Button cancel = new Button("Отмена");
            dialog.add("Вы уверены что хотите удалить клиента?");
            dialog.add(confirm);
            dialog.add(cancel);

            confirm.addClickListener(clickEvent -> {
                customerService.deleteClient(customer);
                dialog.close();
                Notification notification = new Notification("Клиент удален", 1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.open();

                grid.setItems(customerService.getAll());

            });

            cancel.addClickListener(clickEvent -> {
                dialog.close();
            });

            dialog.open();

        }));

        grid.setItems(customerList);
    }
}
