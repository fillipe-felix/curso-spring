package com.cursospring.cursomc.services.exceptions;

import javassist.NotFoundException;

public class AuthorizationException extends NotFoundException {


    public AuthorizationException(String msg) {
        super(msg);
    }

    public AuthorizationException(String msg, Throwable cause) {
        super(msg, (Exception) cause);
    }
}
