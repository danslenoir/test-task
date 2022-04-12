package com.haulmont.testtask.views.mainPage;

import com.haulmont.testtask.views.MainViewLayout;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainViewLayout.class)
@PageTitle("Главная страница")
public class MainView extends AppLayout {

    public MainView(){}
}
