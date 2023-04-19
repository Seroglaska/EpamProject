package com.epam.restaurant.service;

import com.epam.restaurant.bean.PaymentMethod;

import java.util.List;

/**
 * The ${@code PaymentService} interface provides methods for working with payments and invoices
 */
public interface PaymentService {
    /**
     * Creates an invoice for the order and validates the input orderId
     * @param orderId id of the order for which the invoice is issued
     * @param isOnlinePay payment type
     * @return id of the created invoice
     * @throws ServiceException if an error occurred in the method
     */
    int createInvoice(int orderId, boolean isOnlinePay) throws ServiceException;

    /**
     * Returns a list of possible payment methods
     * @return list of possible payment methods
     * @throws ServiceException if an error occurred in the method
     */
    List<PaymentMethod> getPaymentMethods() throws ServiceException;

    /**
     * Creates a payment for the order and validates the input parameters
     * @param invoiceId invoice ID
     * @param paymentMethodId payment method ID
     * @throws ServiceException if an error occurred in the method
     */
    void createPayment(int invoiceId, int paymentMethodId) throws ServiceException;
}
