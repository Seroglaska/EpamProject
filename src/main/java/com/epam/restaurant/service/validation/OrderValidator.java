package com.epam.restaurant.service.validation;

import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.criteria.Criteria;

import java.util.Map;

public final class OrderValidator {
    public static void validate(Order order, String userLogin) throws ValidationException {
        if (order == null) {
            throw new ValidationException("Order is null when trying to create order...");
        }
        if (userLogin == null) {
            throw new ValidationException("user login is null when trying to create order...");
        }
        if (!userLogin.matches(ValidationType.LOGIN.getRegex())) {
            throw new ValidationException(ValidationType.LOGIN.getErrorMsg());
        }
    }

    public static void validate(int orderId, int menuId, Integer quantity, String methodOfReceiving) throws ValidationException {
        if (methodOfReceiving == null) {
            throw new ValidationException("method of receiving is null when trying to create oder detail");
        }
        if (orderId <= 0) {
            throw new ValidationException("order Id should be more then 0 when trying to create oder detail");
        }
        if (menuId <= 0) {
            throw new ValidationException("menu Id should be more then 0 when trying to create oder detail");
        }
        if (quantity < 0) {
            throw new ValidationException("menu Id should be positive when trying to create oder detail");
        }
    }

    public static void validate(int userId) throws ValidationException {
        if (userId <= 0) {
            throw new ValidationException("user Id should be more then 0 when trying to create oder detail");
        }
    }

    public static void validate(Criteria criteria) throws ValidationException {
        if (criteria == null) {
            throw new ValidationException("criteria is null when trying to find order with...");
        }
        for (Map.Entry<String, Object> entry : criteria.getCriteria().entrySet()) {
            if (entry.getValue() == null) {
                throw new ValidationException(String.format("%s is null...", entry.getKey()));
            }
        }
    }
}
