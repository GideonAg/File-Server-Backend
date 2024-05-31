package com.amalitechfileserver.fileserverbackend.exception;

import io.jsonwebtoken.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class Listeners {

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<String> invalidCredentials(InvalidCredentials exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<String> userAlreadyRegistered(UserAlreadyRegisteredException exception) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(exception.getMessage());
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> userNameNotFound(UserNotFound exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(FileNotFound.class)
    public ResponseEntity<String> fileNotFound(FileNotFound exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(InputBlank.class)
    public ResponseEntity<String> inputBlank(InputBlank exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> fileSizeExceeded(MaxUploadSizeExceededException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("File size should be less than 20MB");
    }

    @ExceptionHandler({Exception.class, IOException.class})
    public ResponseEntity<String> genericException(Exception exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }
}
