package main.java.com.haulmont.testtask.windows;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import main.java.com.haulmont.testtask.MainUI;
import main.java.com.haulmont.testtask.dao.ClientDAOImpl;
import main.java.com.haulmont.testtask.dao.OrderDAOImpl;
import main.java.com.haulmont.testtask.entity.Order;
import main.java.com.haulmont.testtask.entity.Status;
import main.java.com.haulmont.testtask.dao.MechanicDAOImpl;
import main.java.com.haulmont.testtask.util.DateCustomValidator;
import main.java.com.haulmont.testtask.util.FloatCustomRangeValidator;
import main.java.com.haulmont.testtask.util.IntegerCustomRangeValidator;

public class ModalWindowOrderAdd extends Window {

    private static OrderDAOImpl orderDAO = new OrderDAOImpl();
    private static MechanicDAOImpl mechanicDAO = new MechanicDAOImpl();
    private static ClientDAOImpl clientDAO = new ClientDAOImpl();

    public ModalWindowOrderAdd() {
        super("Add order");

        TextField idClient = new TextField("idClient");
        TextField idMechanic = new TextField("idMechanic");
        DateField dateOfCreation = new DateField("date of creation");
        DateField endDate = new DateField("end date");
        TextField cost = new TextField("cost");
        TextField description = new TextField("description");
        ComboBox cb = new ComboBox("status");
        cb.addItems("SCHEDULED", "COMPLETED", "ACCEPTED");
        cb.setValue("SCHEDULED");
        cb.setNullSelectionAllowed(false);

        idClient.addValidator(new IntegerCustomRangeValidator());
        idClient.setValidationVisible(false);
        idClient.setMaxLength(9);

        idMechanic.addValidator(new IntegerCustomRangeValidator());
        idMechanic.setValidationVisible(false);
        idMechanic.setMaxLength(9);


        dateOfCreation.addValidator(new DateCustomValidator());
        dateOfCreation.setValidationVisible(false);

        endDate.addValidator(new DateCustomValidator());
        endDate.setValidationVisible(false);

        cost.addValidator(new FloatCustomRangeValidator());
        cost.setValidationVisible(false);
        cost.setMaxLength(9);

        description.addValidator(new StringLengthValidator("The description must have 3-30 characters lenght",3,50,false));
        description.setValidationVisible(false);
        description.setMaxLength(50);

        Button close = new Button("Close", FontAwesome.CLOSE);
        close.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        Button add = new Button("Add order");
        add.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                int isValid = 0;

                try {
                    idClient.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    idClient.setValidationVisible(true);
                }

                try {
                    idMechanic.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    idMechanic.setValidationVisible(true);
                }

                try {
                    dateOfCreation.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    dateOfCreation.setValidationVisible(true);
                }

                try {
                    endDate.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    endDate.setValidationVisible(true);
                }

                try {
                    cost.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    cost.setValidationVisible(true);
                }

                try {
                    description.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    description.setValidationVisible(true);
                }

                if(isValid==6) {
                    Integer iC = Integer.valueOf(idClient.getValue());
                    Integer iM = Integer.valueOf(idMechanic.getValue());
                    Float c = Float.valueOf(cost.getValue());

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

                    int clientExists = clientDAO.findByIdClient(iC);
                    int mechanicExists = mechanicDAO.findByIdMechanic(iM);

                    if (mechanicExists == 1 && clientExists == 1) {
                        Order order = new Order(iC, iM, dateOfCreation.getValue(), endDate.getValue(), c, status, description.getValue());
                        orderDAO.insert(order);
                        getUI().refreshOrders();
                        close();
                    } else {
                        Notification.show("Mechanics and/or client with the specified identifiers does not exist!");
                    }
                }
            }
        });

        VerticalLayout windowContent = new VerticalLayout();
        windowContent.setMargin(true);
        setContent(windowContent);

        windowContent.addComponent(idClient);
        windowContent.addComponent(idMechanic);
        windowContent.addComponent(dateOfCreation);
        windowContent.addComponent(endDate);
        windowContent.addComponent(cost);
        windowContent.addComponent(cb);
        windowContent.addComponent(description);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(add);
        buttonLayout.addComponent(close);
        windowContent.addComponent(buttonLayout);

        setPosition(20, 150);
        setWidth("250px");
        setHeight("750px");
        setModal(true);
    }

    @Override
    public MainUI getUI() {
        return (MainUI) super.getUI();
    }
}
