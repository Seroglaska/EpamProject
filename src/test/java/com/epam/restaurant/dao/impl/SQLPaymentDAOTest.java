package com.epam.restaurant.dao.impl;

import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.PaymentDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLPaymentDAOTest {
    private static ConnectionPool connectionPool;
    private static final PaymentDAO paymentDAO = DAOProvider.getInstance().getPaymentDAO();
    private static final String DELETE_PAYMENT_BY_ID = "DELETE FROM payments WHERE id=%s";
    private static final String DELETE_INVOICE_BY_ID = "DELETE FROM invoice WHERE id=%s";
    private static final String GET_COUNT_OF_PAYMENT_METHODS = "SELECT count(id) FROM payment_methods";
    private static int paymentMethodId = 2;
    private static int createdInvoiceId;
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    @BeforeAll
    static void beforeAll() throws SQLException, ClassNotFoundException, InterruptedException {
        connectionPool = ConnectionPool.getInstance();
        connectionPool.initConnectionPool();
    }

    @AfterAll
    static void afterAll() throws SQLException, InterruptedException {
        connectionPool.dispose();
    }

    @BeforeEach
    void setUp() throws SQLException, InterruptedException {
        connection = connectionPool.takeConnection();
        statement = connection.createStatement();
    }

    @AfterEach
    void tearDown() throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    @Test
    @Order(1)
    void createInvoice_idIsPositive_true() throws DAOException {
        int orderId = 1;
        createdInvoiceId = paymentDAO.createInvoice(orderId, true);

        Assertions.assertTrue(createdInvoiceId > 0);

    }

    @Test
    @Order(2)
    void createPayment_idIsPositive_true() throws DAOException, SQLException {
        int paymentId = paymentDAO.createPayment(createdInvoiceId, paymentMethodId);
        Assertions.assertTrue(paymentId > 0);

        statement.executeUpdate(String.format(DELETE_PAYMENT_BY_ID, paymentId));
        statement.executeUpdate(String.format(DELETE_INVOICE_BY_ID, createdInvoiceId));
    }

    @Test
    void getPaymentMethods_CountOfMethodsEquals_true() throws DAOException, SQLException {
        resultSet = statement.executeQuery(GET_COUNT_OF_PAYMENT_METHODS);
        resultSet.next();

        int countIndx = 1;
        Assertions.assertEquals(resultSet.getInt(countIndx), paymentDAO.getPaymentMethods().size());
    }
}