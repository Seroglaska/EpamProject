package com.epam.restaurant.service.util;

import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.service.ServiceException;

public class TransactionImpl implements TransactionService{
    private static final DAOProvider daoProvider = DAOProvider.getInstance();

    @Override
    public void startTransaction() throws ServiceException {
        try {
            daoProvider.getTransactionDAO().startTransaction();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void commit() throws ServiceException {
        try {
            daoProvider.getTransactionDAO().commit();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void rollback() throws ServiceException {
        try {
            daoProvider.getTransactionDAO().rollback();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
