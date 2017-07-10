package main.java.com.haulmont.testtask.windows;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import main.java.com.haulmont.testtask.MainUI;
import main.java.com.haulmont.testtask.dao.MechanicDAOImpl;
import main.java.com.haulmont.testtask.entity.Mechanic;
import main.java.com.haulmont.testtask.util.DoubleCustomRangeValidator;

public class ModalWindowMechanicEdit extends Window {

    private static MechanicDAOImpl mechanicDAO = new MechanicDAOImpl();

    public ModalWindowMechanicEdit(int idMechanic, String nameMechanic, String surnameMechanic,
                                 String patronomicMechanic, Double hourlyPayment) {
        super("Mechanic editing");
        TextField name = new TextField("name");
        TextField surname = new TextField("surname");
        TextField patronomic = new TextField("patronomic");
        TextField hourlyPaymentMechanic = new TextField("hourly payment");

        name.addValidator(new StringLengthValidator("The name must have 3-30 characters lenght", 3, 30, true));
        name.setValidationVisible(false);
        name.setMaxLength(30);

        surname.addValidator(new StringLengthValidator("The name must have 3-30 characters lenght", 3, 30, true));
        surname.setValidationVisible(false);
        surname.setMaxLength(30);

        patronomic.addValidator(new StringLengthValidator("The name must have 0-30 characters lenght", 0, 30, true));
        patronomic.setValidationVisible(false);
        patronomic.setMaxLength(30);

        hourlyPaymentMechanic.addValidator(new DoubleCustomRangeValidator());
        hourlyPaymentMechanic.setValidationVisible(false);
        hourlyPaymentMechanic.setMaxLength(9);

        name.setValue(nameMechanic);
        surname.setValue(surnameMechanic);
        patronomic.setValue(patronomicMechanic);
        hourlyPaymentMechanic.setValue(hourlyPayment.toString());

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
                    name.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    name.setValidationVisible(true);
                }

                try {
                    surname.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    surname.setValidationVisible(true);
                }

                try {
                    patronomic.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    patronomic.setValidationVisible(true);
                }

                try {
                    hourlyPaymentMechanic.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    hourlyPaymentMechanic.setValidationVisible(true);
                }

                if(isValid==4) {
                    Double hourlyPayment = Double.valueOf(hourlyPaymentMechanic.getValue());
                    Mechanic mechanic = new Mechanic(idMechanic, name.getValue(), surname.getValue(), patronomic.getValue(), hourlyPayment);
                    mechanicDAO.update(mechanic);
                    getUI().refreshMechanics();
                    close();
                }
            }
        });

        VerticalLayout windowContent = new VerticalLayout();
        windowContent.setMargin(true);
        setContent(windowContent);

        windowContent.addComponent(name);
        windowContent.addComponent(surname);
        windowContent.addComponent(patronomic);
        windowContent.addComponent(hourlyPaymentMechanic);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(edit);
        buttonLayout.addComponent(close);
        windowContent.addComponent(buttonLayout);

        setPosition(20, 150);
        setWidth("250px");
        setHeight("450px");
        setModal(true);
    }

    @Override
    public MainUI getUI() {
        return (MainUI) super.getUI();
    }
}
