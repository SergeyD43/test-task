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

import java.sql.Date;

public class ModalWindowOrderEdit extends Window {

    private static OrderDAOImpl orderDAO = new OrderDAOImpl();
    private static MechanicDAOImpl mechanicDAO = new MechanicDAOImpl();
    private static ClientDAOImpl clientDAO = new ClientDAOImpl();

    public ModalWindowOrderEdit(int id, Integer idClient, Integer idMechanic,
                                Date dateOfCreation, Date endDate,
                                Float cost, Status status, String description) {
        super("Order editing");

        TextField idClientField = new TextField("idClient");
        TextField idMechanicField = new TextField("idMechanic");
        DateField dateOfCreationField = new DateField("date of creation");
        DateField endDateField = new DateField("end date");
        TextField costField = new TextField("cost");
        ComboBox cb = new ComboBox("status");
        cb.addItems("SCHEDULED", "COMPLETED", "ACCEPTED");
        cb.setNullSelectionAllowed(false);
        TextField descriptionField = new TextField("description");

        idClientField.addValidator(new IntegerCustomRangeValidator());
        idClientField.setValidationVisible(false);
        idClientField.setMaxLength(9);

        idMechanicField.addValidator(new IntegerCustomRangeValidator());
        idMechanicField.setValidationVisible(false);
        idMechanicField.setMaxLength(9);

        dateOfCreationField.addValidator(new DateCustomValidator());
        dateOfCreationField.setValidationVisible(false);

        endDateField.addValidator(new DateCustomValidator());
        endDateField.setValidationVisible(false);

        costField.addValidator(new FloatCustomRangeValidator());
        costField.setValidationVisible(false);
        costField.setMaxLength(9);
        descriptionField.addValidator(new StringLengthValidator("The description must have 3-30 characters lenght",3,50,false));
        descriptionField.setValidationVisible(false);
        descriptionField.setMaxLength(50);

        idClientField.setValue(idClient.toString());
        idMechanicField.setValue(idMechanic.toString());
        dateOfCreationField.setValue(dateOfCreation);
        endDateField.setValue(endDate);
        costField.setValue(cost.toString());

        cb.setValue(status.toString());
        descriptionField.setValue(description);

        Button close = new Button("Close", FontAwesome.CLOSE);
        close.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        Button edit = new Button("Edit");
        edit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {

                int isValid = 0;

                try {
                    idClientField.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    idClientField.setValidationVisible(true);
                }

                try {
                    idMechanicField.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    idMechanicField.setValidationVisible(true);
                }

                try {
                    dateOfCreationField.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    dateOfCreationField.setValidationVisible(true);
                }

                try {
                    endDateField.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    endDateField.setValidationVisible(true);
                }

                try {
                    costField.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    costField.setValidationVisible(true);
                }

                try {
                    descriptionField.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    descriptionField.setValidationVisible(true);
                }

                if(isValid==6) {
                    Integer iC = Integer.valueOf(idClientField.getValue());
                    Integer iM = Integer.valueOf(idMechanicField.getValue());
                    Float c = Float.valueOf(costField.getValue());

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
                        Order order = new Order(id, iC, iM, dateOfCreationField.getValue(), endDateField.getValue()
                                , c, status, descriptionField.getValue());
                        orderDAO.update(order);
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

        windowContent.addComponent(idClientField);
        windowContent.addComponent(idMechanicField);
        windowContent.addComponent(dateOfCreationField);
        windowContent.addComponent(endDateField);
        windowContent.addComponent(costField);
        windowContent.addComponent(cb);
        windowContent.addComponent(descriptionField);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(edit);
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
