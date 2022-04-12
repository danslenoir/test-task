package com.haulmont.testtask.views;

import com.haulmont.testtask.views.banks.BankList;
import com.haulmont.testtask.views.creditPropositions.CreditPropositionList;
import com.haulmont.testtask.views.credits.CreditList;
import com.haulmont.testtask.views.customers.CustomersList;
import com.haulmont.testtask.views.mainPage.MainView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;

public class MainViewLayout extends AppLayout {
    public MainViewLayout(){
        createHeader();
        createDrawer();
    }

    private void createHeader(){
        H3 logo = new H3("Haulmont Test Task");
        logo.addClassNames("text-l", "m-m");

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void createDrawer(){
        RouterLink mainLink = new RouterLink("Главная страница", MainView.class);
        RouterLink customersLink = new RouterLink("Клиенты", CustomersList.class);
        RouterLink creditsLink = new RouterLink("Кредиты", CreditList.class);
        RouterLink banksLink = new RouterLink("Банки", BankList.class);
        RouterLink creditsPropositionsLink = new RouterLink("Кредитные предложения", CreditPropositionList.class);
        addToDrawer(new VerticalLayout(
                mainLink,
                customersLink,
                creditsLink,
                banksLink,
                creditsPropositionsLink
        ));
    }
}
