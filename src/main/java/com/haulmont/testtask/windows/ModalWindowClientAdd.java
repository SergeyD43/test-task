package main.java.com.haulmont.testtask.windows;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import main.java.com.haulmont.testtask.MainUI;
import main.java.com.haulmont.testtask.dao.ClientDAOImpl;
import main.java.com.haulmont.testtask.entity.Client;
import main.java.com.haulmont.testtask.util.CustomValidatorForPhone;

public class ModalWindowClientAdd extends Window {

    private static ClientDAOImpl clientDAO = new ClientDAOImpl();

    public ModalWindowClientAdd() {
        super("Add client");
        TextField name = new TextField("name");
        TextField surname = new TextField("surname");
        TextField patronomic = new TextField("patronomic");
        TextField phone = new TextField("phone");

        name.addValidator(new StringLengthValidator("The name must have 3-30 characters lenght", 3, 30, false));
        name.setValidationVisible(false);
        name.setMaxLength(30);

        surname.addValidator(new StringLengthValidator("The surname must have 3-30 characters lenght", 3, 30, false));
        surname.setValidationVisible(false);
        surname.setMaxLength(30);

        patronomic.addValidator(new StringLengthValidator("The patronomic must have 0-30 characters lenght", 0, 30, true));
        patronomic.setValidationVisible(false);
        patronomic.setMaxLength(30);

        phone.addValidator(new CustomValidatorForPhone());
        phone.setValidationVisible(false);
        phone.setMaxLength(30);

        Button close = new Button("Close", FontAwesome.CLOSE);
        close.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                close();
            }
        });

        Button add = new Button("Add");
        add.addClickListener(new Button.ClickListener() {
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
                    phone.validate();
                    isValid++;
                } catch (Validator.InvalidValueException e) {
                    phone.setValidationVisible(true);
                }

                if(isValid==4) {
                    Client client = new Client(name.getValue(), surname.getValue(), patronomic.getValue(), phone.getValue());
                    clientDAO.insert(client);
                    getUI().refreshClients();
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
        windowContent.addComponent(phone);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addComponent(add);
        buttonLayout.addComponent(close);
        windowContent.addComponent(buttonLayout);

        setPosition(20, 150);
        setWidth("250px");
        setHeight("400px");
        setModal(true);
    }

    @Override
    public MainUI getUI() {
        return (MainUI) super.getUI();
    }
}
