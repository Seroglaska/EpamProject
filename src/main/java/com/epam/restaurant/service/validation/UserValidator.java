package com.epam.restaurant.service.validation;

import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;

import java.util.Map;

public final class UserValidator {
    private UserValidator() {
    }

    public static void validateUserData(RegistrationUserData userData) throws ValidationException {
        if (userData == null) {
            throw new ValidationException("User is null");
        } else if (userData.getName() != null && !userData.getName().matches(ValidationType.NAME.getRegex())) {
            throw new ValidationException(ValidationType.NAME.getErrorMsg());
        } else if (userData.getLogin() != null && !userData.getLogin().matches(ValidationType.LOGIN.getRegex())) {
            throw new ValidationException(ValidationType.LOGIN.getErrorMsg());
        } else if (userData.getPhoneNumber() != null && !userData.getPhoneNumber().matches(ValidationType.PHONE_NUMBER.getRegex())) {
            throw new ValidationException(ValidationType.PHONE_NUMBER.getErrorMsg());
        } else if (userData.getEmail() != null && !userData.getEmail().matches(ValidationType.EMAIL.getRegex())) {
            throw new ValidationException(ValidationType.EMAIL.getErrorMsg());
        }
    }

    public static void validate(Criteria criteria) throws ValidationException {
        if (criteria == null) {
            throw new ValidationException("criteria to find is null");
        }

        Map<String, Object> criterias = criteria.getCriteria();
        for (Map.Entry<String, Object> entry : criterias.entrySet()) {
            if (entry.getKey().equals(ValidationType.LOGIN.name())) {
                if (entry.getValue() != null && !entry.getValue().toString().matches(ValidationType.LOGIN.getRegex())) {
                    throw new ValidationException(ValidationType.LOGIN.getErrorMsg());
                }
            } else if (entry.getKey().equals(ValidationType.NAME.name())) {
                if (entry.getValue() != null && !entry.getValue().toString().matches(ValidationType.NAME.getRegex())) {
                    throw new ValidationException(ValidationType.NAME.getErrorMsg());
                }
            } else if (entry.getKey().equals(ValidationType.EMAIL.name())) {
                if (entry.getValue() != null && !entry.getValue().toString().matches(ValidationType.EMAIL.getRegex())) {
                    throw new ValidationException(ValidationType.EMAIL.getErrorMsg());
                }
            } else if (entry.getKey().equals(ValidationType.PHONE_NUMBER.name())) {
                if (entry.getValue() != null && !entry.getValue().toString().matches(ValidationType.PHONE_NUMBER.getRegex())) {
                    throw new ValidationException(ValidationType.PHONE_NUMBER.getErrorMsg());
                }
            }
        }
    }
}
