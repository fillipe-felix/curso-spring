package com.cursospring.cursomc.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationError  extends StandardError{

    private List<FieldMessage> errors = new ArrayList<>();

    public ValidationError(Integer status, String msg, Long timestamp) {
        super(status, msg, timestamp);
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }

    public void addError(String fielName, String message){
        errors.add(new FieldMessage(fielName, message));
    }
}
