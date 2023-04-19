package com.epam.restaurant.service.validation;

import com.epam.restaurant.service.ServiceException;

/**
 * The ${@code ValidationException} occurs in case of validation error
 */
public class ValidationException extends ServiceException {
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
