package com.library.libraryservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorException> handleNotFoundException(NotFoundException ex){
        CustomErrorException error = new CustomErrorException(
            ex.getMessage(), "404", "No data found");
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }
}
