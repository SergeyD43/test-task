package main.java.com.haulmont.testtask.util;

import com.vaadin.data.Validator;

public class DateCustomValidator implements Validator {
    @Override
    public void validate(Object o) throws InvalidValueException {
        if(o==null) throw new InvalidValueException("Please enter a date");
    }
}
