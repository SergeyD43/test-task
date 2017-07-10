package main.java.com.haulmont.testtask.pages;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.BeanContainer;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import main.java.com.haulmont.testtask.MainUI;
import main.java.com.haulmont.testtask.dao.OrderDAOImpl;
import main.java.com.haulmont.testtask.entity.Order;
import main.java.com.haulmont.testtask.entity.Status;
import main.java.com.haulmont.testtask.util.IntegerCustomRangeValidator;
import main.java.com.haulmont.testtask.windows.ModalWindowOrderAdd;
import main.java.com.haulmont.testtask.windows.ModalWindowOrderEdit;

import java.sql.Date;
import java.util.Collection;

public class OrdersPage extends VerticalLayout implements View {

    public static final String NAME = "orderspage";
    private static final long serialVersionUID = 1L;
    private static OrderDAOImpl orderDAO = new OrderDAOImpl();
    private Table dataTable;
    private BeanContainer<String, Order> container;
    private BeanContainer<String, Order> filterContainer;
    private Button filterBtn;
    private Button closeFilter;

    public OrdersPage() {
        Collection<Order> orders = orderDAO.getAll();
        container = new BeanContainer<>(Order.class);
        container.setBeanIdProperty("id");
        container.addAll(orders);
        dataTable = new Table("Orders", container);
        dataTable.setPageLength(dataTable.size());

        dataTable.addValueChangeListener((Property.ValueChangeEvent event) -> {

        });

        filterContainer = new BeanContainer<>(Order.class);
        filterContainer.setBeanIdProperty("id");

        Button addBtn = new Button("Add");
        addBtn.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                ModalWindowOrderAdd modalWindowOrderAdd = new ModalWindowOrderAdd();
                getUI().addWindow(modalWindowOrderAdd);
            }
        });

        Button removeBtn = new Button("Delete");
        removeBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                removeOrder();
            }
        });

        Button editBtn = new Button("Edit");
        editBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                editOrder();
            }
        });

        TextField filterDescriptionField = new TextField("Description");
        ComboBox cb = new ComboBox("Status");
        cb.addItems("SCHEDULED", "COMPLETED", "ACCEPTED");
        cb.setValue("SCHEDULED");
        cb.setNullSelectionAllowed(false);
        TextField filterIdClientField = new TextField("IdClient");

        filterDescriptionField.addValidator(new StringLengthValidator("Not correct!",1,50,false));
        filterDescriptionField.setValidationVisible(false);
        filterDescriptionField.setMaxLength(50);

        filterIdClientField.addValidator(new IntegerCustomRangeValidator());
        filterIdClientField.setValidationVisible(false);
        filterIdClientField.setMaxLength(9);

        filterBtn = new Button("Apply");
        filterBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                int isValid = 0;

                try {
                    filterIdClientField.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    filterIdClientField.setValidationVisible(true);
                }

                try {
                    filterDescriptionField.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    filterDescriptionField.setValidationVisible(true);
                }

                if(isValid==2) {
                    String str1 = filterIdClientField.getValue();
                    Integer i1 = Integer.valueOf(str1);
                    String valueStatus = cb.getValue().toString();
                    Status status = null;
                    switch (valueStatus) {
                        case "SCHEDULED":
                            status = Status.SCHEDULED;
                            break;
                        case "COMPLETED":
                            status = Status.COMPLETED;
                            break;
                        case "ACCEPTED":
                            status = Status.ACCEPTED;
                        default:
                            break;
                    }
                    Collection<Order> orderCollection = orderDAO.findOrderByFilter(filterDescriptionField.getValue(), i1, status);
                    System.out.println(orderCollection);
                    filterContainer.addAll(orderCollection);
                    dataTable.setContainerDataSource(filterContainer);
                    filterBtn.setVisible(false);
                    closeFilter.setVisible(true);
                }
            }
        });

        closeFilter = new Button("Close");
        closeFilter.setVisible(false);
        closeFilter.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                filterContainer.removeAllItems();
                filterBtn.setVisible(true);
                closeFilter.setVisible(false);
                dataTable.setContainerDataSource(container);
            }
        });

        HorizontalLayout layoutForButton = new HorizontalLayout();
        layoutForButton.addComponent(addBtn);
        layoutForButton.addComponent(editBtn);
        layoutForButton.addComponent(removeBtn);

        HorizontalLayout layoutForFilter = new HorizontalLayout();
        layoutForFilter.addComponent(filterDescriptionField);
        layoutForFilter.addComponent(filterIdClientField);
        layoutForFilter.addComponent(cb);
        layoutForFilter.addComponent(closeFilter);

        addComponent(layoutForButton);
        addComponent(layoutForFilter);
        addComponent(filterBtn);
        addComponent(dataTable);
    }


    private void removeOrder() {
        Object selected = dataTable.getValue();
        if(selected == null) {
            Notification.show("You must select an item");
        } else {
            Item item = dataTable.getItem(selected);
            Property idOrderProperty = item.getItemProperty("id");
            int idOrder = (int) idOrderProperty.getValue();
            orderDAO.deleteOrderById(idOrder);
            Notification.show("Value : " + selected);
            dataTable.removeItem(selected);
            dataTable.setPageLength(dataTable.size());
        }
    }

    private void editOrder() {
        Object selected = dataTable.getValue();
        if(selected == null) {
            Notification.show("You must select an item");
        } else {
            Item item = dataTable.getItem(selected);

            Property idProperty = item.getItemProperty("id");
            int id = (int) idProperty.getValue();

            Property idClientProperty = item.getItemProperty("idClient");
            int idClient = (int) idClientProperty.getValue();

            Property idMechanicProperty = item.getItemProperty("idMechanic");
            int idMechanic = (int) idMechanicProperty.getValue();

            Property dateOfCreationProperty = item.getItemProperty("dateOfCreation");
            Date dateOfCreation = (Date) dateOfCreationProperty.getValue();

            Property endDateProperty = item.getItemProperty("endDate");
            Date endDate = (Date) endDateProperty.getValue();

            Property costProperty = item.getItemProperty("cost");
            float cost = (float) costProperty.getValue();

            Property statusProperty = item.getItemProperty("status");
            Status status = (Status) statusProperty.getValue();

            Property descriptionProperty = item.getItemProperty("description");
            String description = (String) descriptionProperty.getValue();

            ModalWindowOrderEdit modalWindowOrderEdit = new ModalWindowOrderEdit(id,
                    idClient, idMechanic,
                    dateOfCreation, endDate, cost, status, description);

            getUI().addWindow(modalWindowOrderEdit);
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
