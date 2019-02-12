package com.melardev.spring.shoppingcartweb.dtos.response.base;

import org.springframework.validation.BeanPropertyBindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class ErrorResponse extends AppResponse {
    private Map<String, Object> errors;

    public ErrorResponse(Map<String, Object> errors) {
        super(false);
        this.errors = errors;
        if (getFullMessages() == null)
            setFullMessages(new ArrayList<>());

        errors.forEach((key, value) -> {
            if (value.getClass() == BeanPropertyBindingResult.class)
                getFullMessages().addAll(((BeanPropertyBindingResult) value).getAllErrors().stream().map(e -> e.getDefaultMessage()).collect(toList()));
            else {
                if (key.equalsIgnoreCase("stack_trace")) // stack trace is added in errors, not in full_messages
                    return;
                getFullMessages().add(value.toString());
            }
        });
    }

    public ErrorResponse(String errors) {
        super(false);
        getFullMessages().add(errors);
    }

    public ErrorResponse(String message, StackTraceElement[] stackTraceElements) {
        this(message);
        this.errors = new HashMap<>(1);
        this.errors.put("stack_trace", Arrays.asList(stackTraceElements).stream().map(StackTraceElement::toString).collect(toList()));
    }


    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }

    ErrorResponse() {
        this(new HashMap<>());
    }


}
