package main.java.com.haulmont.testtask.util;

import com.vaadin.data.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomValidatorForPhone implements Validator {
    @Override
    public void validate(Object o) throws InvalidValueException {

        String sPhoneNumber = (String) o;

/*        Examples: Matches following phone numbers:
        (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890*/
        Pattern pattern = Pattern.compile("^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$");
        Matcher matcher = pattern.matcher(sPhoneNumber);

        if (!matcher.matches()) {
            throw new InvalidValueException("Phone number is not Valid");
        }
    }
}
