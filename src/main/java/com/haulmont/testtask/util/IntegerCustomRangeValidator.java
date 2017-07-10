package main.java.com.haulmont.testtask.util;


import com.vaadin.data.Validator;

public class IntegerCustomRangeValidator implements Validator {
    @Override
    public void validate(Object o) throws InvalidValueException {
        Integer i;
        try{
            i = (Integer.valueOf((String)o));
        }catch (NumberFormatException e){
            throw new InvalidValueException("Not correct");
        }

        if(i<0) throw new InvalidValueException("Not correct");
    }
}
