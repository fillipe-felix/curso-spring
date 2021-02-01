package com.cursospring.cursomc.services.exceptions;

import javassist.NotFoundException;

public class FileException extends NotFoundException {


    public FileException(String msg) {
        super(msg);
    }

    public FileException(String msg, Throwable cause) {
        super(msg, (Exception) cause);
    }
}
