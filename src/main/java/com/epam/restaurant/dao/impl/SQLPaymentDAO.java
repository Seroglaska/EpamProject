package com.epam.restaurant.dao.impl;

import com.epam.restaurant.bean.PaymentMethod;
import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.PaymentDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLPaymentDAO implements PaymentDAO {
    private static final Logger LOGGER = LogManager.getLogger(SQLPaymentDAO.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String INSERT_INVOICE_QUERY = "INSERT INTO invoice(date, status, orders_id) VALUES(?,?,?)";
    private static final String INSERT_PAYMENT_QUERY = "INSERT INTO payments(date, status, invoice_id, payment_methods_id) VALUES(?,?,?,?)";
    private static final String GET_PAYMENT_METHODS_QUERY = "SELECT * FROM payment_methods";

    private static final String UNPAID = "unpaid";
    private static final String PAID = "paid";
    private static final String SUCCESS = "success";
    private static final int GENERATED_KEY = 1;

    @Override
    public int createInvoice(int orderId, boolean isOnlinePay) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = DAOProvider.getInstance().getTransactionDAO().getConnectionHolder().get();
            if (connection == null) {
                connection = connectionPool.takeConnection();
            }

            preparedStatement = connection.prepareStatement(INSERT_INVOICE_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(2, isOnlinePay ? PAID : UNPAID);
            preparedStatement.setInt(3, orderId);
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getInt(GENERATED_KEY);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to create order", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() throws DAOException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(GET_PAYMENT_METHODS_QUERY);

            if (!resultSet.isBeforeFirst()) {
                return Collections.emptyList();
            }

            List<PaymentMethod> methods = new ArrayList<>();
            while (resultSet.next()) {
                PaymentMethod paymentMethod = new PaymentMethod();

                paymentMethod.setId(resultSet.getInt(1));
                paymentMethod.setMethod(resultSet.getString(2));

                methods.add(paymentMethod);
            }

            return methods;
        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a query", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public int createPayment(int invoiceId, int paymentMethodId) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = DAOProvider.getInstance().getTransactionDAO().getConnectionHolder().get();
            if (connection == null) {
                connection = connectionPool.takeConnection();
            }

            preparedStatement = connection.prepareStatement(INSERT_PAYMENT_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(2, SUCCESS);
            preparedStatement.setInt(3, invoiceId);
            preparedStatement.setInt(4, paymentMethodId);
            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();

            return generatedKeys.getInt(GENERATED_KEY);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to create order", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }
}
