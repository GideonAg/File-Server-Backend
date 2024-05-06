package com.amalitechfileserver.fileserverbackend.exception;

public class UserAlreadyRegisteredException extends Exception{
    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
