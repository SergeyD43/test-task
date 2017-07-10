package main.java.com.haulmont.testtask.pages;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import main.java.com.haulmont.testtask.MainUI;
import main.java.com.haulmont.testtask.dao.MechanicDAOImpl;
import main.java.com.haulmont.testtask.entity.Mechanic;
import main.java.com.haulmont.testtask.entity.Statistic;
import main.java.com.haulmont.testtask.dao.OrderDAOImpl;
import main.java.com.haulmont.testtask.windows.ModalWindowMechanicAdd;
import main.java.com.haulmont.testtask.windows.ModalWindowMechanicEdit;

import java.util.Collection;
import java.util.Set;

public class MechanicsPage extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;
    public static final String NAME = "mechanicspage";
    private static MechanicDAOImpl mechanicDAO = new MechanicDAOImpl();
    private static OrderDAOImpl orderDAO = new OrderDAOImpl();
    private Table dataTable;
    private Table statisticTable = new Table("Statistic Table");
    private Button removeStatisticBtn;
    private Button statisticBtn;

    public MechanicsPage() {
        statisticTable.setVisible(false);
        Collection<Mechanic> mechanics = mechanicDAO.getAll();
        BeanContainer<String, Mechanic> dataBean = new BeanContainer<>(Mechanic.class);
        BeanContainer<String, Statistic> statisticContainer = new BeanContainer(Statistic.class);

        statisticContainer.setBeanIdProperty("nameMechanic");
        dataBean.setBeanIdProperty("id");
        dataBean.addAll(mechanics);
        dataTable = new Table("mechanics", dataBean);
        dataTable.setPageLength(dataTable.size());

        dataTable.addValueChangeListener((Property.ValueChangeEvent event) -> {

        });

        Button addBtn = new Button("Add");
        addBtn.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                ModalWindowMechanicAdd modalWindowMechanicAdd = new ModalWindowMechanicAdd();
                getUI().addWindow(modalWindowMechanicAdd);
            }
        });

        Button removeBtn = new Button("Delete");
        removeBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                removeMechanic();
            }
        });

        Button editBtn = new Button("Edit");
        editBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editMechanic();
            }
        });

        statisticBtn = new Button("Show statistics");
        statisticBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Set<Statistic> statistic = mechanicDAO.getStatistic();
                statisticContainer.addAll(statistic);
                statisticTable.setContainerDataSource(statisticContainer);
                statisticTable.setPageLength(statisticTable.size());
                dataTable.setVisible(false);
                statisticTable.setVisible(true);
                statisticBtn.setVisible(false);
                removeStatisticBtn.setVisible(true);
            }
        });

        removeStatisticBtn = new Button("Close statistics");
        removeStatisticBtn.setVisible(false);
        removeStatisticBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                statisticContainer.removeAllItems();
                dataTable.setVisible(true);
                statisticTable.setVisible(false);
                removeStatisticBtn.setVisible(false);
                statisticBtn.setVisible(true);
            }
        });

        HorizontalLayout layoutForButton = new HorizontalLayout();
        layoutForButton.addComponent(addBtn);
        layoutForButton.addComponent(editBtn);
        layoutForButton.addComponent(removeBtn);
        layoutForButton.addComponent(statisticBtn);
        layoutForButton.addComponent(removeStatisticBtn);
        addComponent(layoutForButton);
        addComponent(dataTable);
        addComponent(statisticTable);
    }

    private void removeMechanic() {
        Object selected = dataTable.getValue();
        if(selected == null)
        {
            Notification.show("You must select an item");
        }
        else
        {
            Item item = dataTable.getItem(selected);
            Property idMechanicProperty = item.getItemProperty("id");
            int idMechanic = (int) idMechanicProperty.getValue();
            int m = orderDAO.findByIdMechanic(idMechanic);
            if(m>0){
                Notification.show("This mechanic has an order!");
            } else {
                mechanicDAO.deleteMechanicById(idMechanic);
                Notification.show("Value : " + selected);
                dataTable.removeItem(selected);
                dataTable.setPageLength(dataTable.size());
            }
        }
    }

    private void editMechanic() {
        Object selected = dataTable.getValue();
        if(selected == null)
        {
            Notification.show("You must select an item");
        }
        else
        {
            Item item = dataTable.getItem(selected);

            Property idMechanicProperty = item.getItemProperty("id");
            int idMechanic = (int) idMechanicProperty.getValue();

            Property nameProperty = item.getItemProperty("name");
            String nameMechanic = (String) nameProperty.getValue();

            Property surnameProperty = item.getItemProperty("surname");
            String surnameMechanic = (String) surnameProperty.getValue();

            Property patronomicProperty = item.getItemProperty("patronomic");
            String patronomicMechanic = (String) patronomicProperty.getValue();

            Property hourlypaymentProperty = item.getItemProperty("hourlypayment");
            double hourlypaymentMechanic = (double) hourlypaymentProperty.getValue();

            ModalWindowMechanicEdit modalWindowMechanicEdit = new ModalWindowMechanicEdit(idMechanic, nameMechanic, surnameMechanic,
                    patronomicMechanic, hourlypaymentMechanic);

            getUI().addWindow(modalWindowMechanicEdit);
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

    @Override
    public MainUI getUI() {
        return (MainUI) super.getUI();
    }
}
