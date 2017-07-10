package main.java.com.haulmont.testtask.util;

import com.vaadin.data.Validator;

public class DoubleCustomRangeValidator implements Validator {
    @Override
    public void validate(Object o) throws InvalidValueException {
        Double d;
        try{
            d = (Double.valueOf((String)o));
        }catch (NumberFormatException e){
            throw new InvalidValueException("Not correct");
        }

        if(d<0) throw new InvalidValueException("Not correct");
    }
}
