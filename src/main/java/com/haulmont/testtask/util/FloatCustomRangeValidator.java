package main.java.com.haulmont.testtask.util;


import com.vaadin.data.Validator;

public class FloatCustomRangeValidator implements Validator {
    @Override
    public void validate(Object o) throws InvalidValueException {
        Float f;
        try{
            f = (Float.valueOf((String)o));
        }catch (NumberFormatException e){
            throw new InvalidValueException("Not correct");
        }

        if(f<0) throw new InvalidValueException("Not correct");
    }
}
