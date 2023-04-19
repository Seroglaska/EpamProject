package com.epam.restaurant.dao;

import com.epam.restaurant.bean.PaymentMethod;

import java.util.List;

/**
 * The ${@code PaymentDAO} interface provides methods for working with payments and invoices in database
 */
public interface PaymentDAO {
    /**
     * Creates an invoice for the order in database
     * @param orderId id of the order for which the invoice is issued
     * @param isOnlinePay payment type
     * @return id of the created invoice
     * @throws DAOException if an error occurred in the method
     */
    int createInvoice(int orderId, boolean isOnlinePay) throws DAOException;

    /**
     * Returns a list of possible payment methods from the database
     * @return list of possible payment methods
     * @throws DAOException if an error occurred in the method
     */
    List<PaymentMethod> getPaymentMethods() throws DAOException;

    /**
     * Creates a payment for the order in the database
     * @param invoiceId invoice ID
     * @param paymentMethodId payment method ID
     * @throws DAOException if an error occurred in the method
     */
    int createPayment(int invoiceId, int paymentMethodId) throws DAOException;
}
