package com.epam.restaurant.service.impl;

import com.epam.restaurant.bean.PaymentMethod;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.PaymentDAO;
import com.epam.restaurant.service.PaymentService;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.validation.PaymentValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PaymentImpl implements PaymentService {
    private static final Logger LOGGER = LogManager.getLogger(OrderImpl.class);
    private static final DAOProvider daoProvider = DAOProvider.getInstance();
    private static final PaymentDAO paymentDAO = daoProvider.getPaymentDAO();

    @Override
    public int createInvoice(int orderId, boolean isOnlinePay) throws ServiceException {
        PaymentValidator.validate(orderId);

        try {
            int invoiceId = paymentDAO.createInvoice(orderId, isOnlinePay);

            return invoiceId;
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() throws ServiceException {
        try {
            return paymentDAO.getPaymentMethods();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void createPayment(int invoiceId, int paymentMethodId) throws ServiceException {
        PaymentValidator.validate(invoiceId, paymentMethodId);

        try {
            paymentDAO.createPayment(invoiceId, paymentMethodId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
