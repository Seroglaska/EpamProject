package com.epam.restaurant.service.validation;

public final class PaymentValidator {
    public static void validate(int invoiceId, int paymentMethodId) throws ValidationException {
        if (invoiceId <= 0) {
            throw new ValidationException("orderId can't be <= 0");
        } else if (paymentMethodId <= 0) {
            throw new ValidationException("paymentMethodId can't be <= 0");
        }
    }

    public static void validate(int orderId) throws ValidationException {
        if (orderId <=0) {
            throw new ValidationException("orderId can't be <= 0");
        }
    }
}
