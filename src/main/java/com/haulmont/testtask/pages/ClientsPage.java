package main.java.com.haulmont.testtask.pages;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import main.java.com.haulmont.testtask.MainUI;
import main.java.com.haulmont.testtask.dao.ClientDAOImpl;
import main.java.com.haulmont.testtask.entity.Client;
import main.java.com.haulmont.testtask.dao.OrderDAOImpl;
import main.java.com.haulmont.testtask.windows.ModalWindowClientAdd;
import main.java.com.haulmont.testtask.windows.ModalWindowClientEdit;

import java.util.Collection;

public class ClientsPage extends VerticalLayout implements View {

    private static final long serialVersionUID = 1L;
    public static final String NAME = "clientspage";
    private static ClientDAOImpl clientDAO = new ClientDAOImpl();
    private static OrderDAOImpl orderDAO = new OrderDAOImpl();
    private Table dataTable;

    public ClientsPage() {
        Collection<Client> clients = clientDAO.getAll();
        BeanContainer<String, Client> dataBean = new BeanContainer<>(Client.class);
        dataBean.setBeanIdProperty("id");
        dataBean.addAll(clients);
        dataTable = new Table("clients", dataBean);
        dataTable.setPageLength(dataTable.size());

        dataTable.addValueChangeListener((Property.ValueChangeEvent event) -> {

        });

        Button addBtn = new Button("Add");
        addBtn.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                ModalWindowClientAdd modalWindowClientAdd = new ModalWindowClientAdd();
                getUI().addWindow(modalWindowClientAdd);
            }
        });

        Button removeBtn = new Button("Delete");
        removeBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                removeClient();
            }
        });

        Button editBtn = new Button("Edit");
        editBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                        editClient();
            }
        });

        HorizontalLayout layoutForButton = new HorizontalLayout();
        layoutForButton.addComponent(addBtn);
        layoutForButton.addComponent(editBtn);
        layoutForButton.addComponent(removeBtn);
        addComponent(layoutForButton);
        addComponent(dataTable);
    }

    private void removeClient(){
        Object selected = dataTable.getValue();
        if(selected == null)
        {
            Notification.show("You must select an item");
        }
        else
        {
            Item item = dataTable.getItem(selected);
            Property idClientProperty = item.getItemProperty("id");
            int idClient = (int) idClientProperty.getValue();
            int m = orderDAO.findByIdClient(idClient);
            if(m>0){
                Notification.show("This client has an order!");
            } else {
                clientDAO.deleteClientById(idClient);
                Notification.show("Value : " + selected);
                dataTable.removeItem(selected);
                dataTable.setPageLength(dataTable.size());
            }
        }
    }

    private void editClient() {
        Object selected = dataTable.getValue();
        if(selected == null)
        {
            Notification.show("You must select an item");
        }
        else
        {
            Item item = dataTable.getItem(selected);

            Property idClientProperty = item.getItemProperty("id");
            int idClient = (int) idClientProperty.getValue();

            Property nameProperty = item.getItemProperty("name");
            String nameClient = (String) nameProperty.getValue();

            Property surnameClientProperty = item.getItemProperty("surname");
            String surnameClient = (String) surnameClientProperty.getValue();

            Property patronomicClientProperty = item.getItemProperty("patronomic");
            String patronomicClient = (String) patronomicClientProperty.getValue();

            Property phoneClientProperty = item.getItemProperty("phone");
            String phoneClient = (String) phoneClientProperty.getValue();

            ModalWindowClientEdit modalWindowClientEdit = new ModalWindowClientEdit(idClient, nameClient, surnameClient,
                    patronomicClient, phoneClient);
            getUI().addWindow(modalWindowClientEdit);
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
