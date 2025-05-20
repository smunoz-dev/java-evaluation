package com.evaluation.register.register_api.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private String resource;
    private String field;
    private long value;

    public ResourceNotFoundException(String resource, String field, long value) {
        super(String.format("%s no encontrado con : %s : '%s'", resource, field, value));
        this.resource = resource;
        this.field = field;
        this.value = value;
    }

    public ResourceNotFoundException(String resource) {
        super(String.format("%s no encontrado", resource));
        this.resource = resource;
    }
}
