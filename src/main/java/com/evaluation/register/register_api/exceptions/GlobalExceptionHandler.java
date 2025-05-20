package com.evaluation.register.register_api.exceptions;

import com.evaluation.register.register_api.model.form.MessageForm;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageForm> handleResourceNotFoundException(ResourceNotFoundException exception){
        MessageForm messageError = new MessageForm(exception.getMessage());
        return new ResponseEntity<>(messageError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageForm> handleGlobalException(Exception exception){
        MessageForm messageError = new MessageForm(exception.getMessage());
        return new ResponseEntity<>(messageError,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        StringBuilder errors = new StringBuilder();
        MessageForm messageError = new MessageForm();
        ex.getBindingResult().getAllErrors()
            .forEach(error -> {
                String field = ((FieldError)error).getField();
                errors.append(field).append(": ").append(error.getDefaultMessage()).append(" | ");
                messageError.setMensaje(errors.toString());
            }
        );
        return new ResponseEntity<>(messageError,HttpStatus.BAD_REQUEST);

    }
}
