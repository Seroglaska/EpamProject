package com.epam.restaurant.dao.impl;

import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.OrderDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SQLOrderDAOTest {
    private static ConnectionPool connectionPool;
    private static final OrderDAO orderDAO = DAOProvider.getInstance().getOrderDAO();
    private static final String DELETE_ORDER_BY_ID = "DELETE FROM orders WHERE id=%s";
    private static final String DELETE_ORDER_DETAILS_BY_ID = "DELETE FROM order_details WHERE orders_id=%s";
    private static final String GET_ORDER_BY_ID = "SELECT * FROM orders WHERE id=%s";
    private static final String GET_COUNT_OF_COOKED_ORDER_BY_ID = "SELECT count(id) FROM orders WHERE user_id=%s AND order_status='cooked'";
    private static int createdDishId;
    private static Criteria criteria;
    private static final int FOUND_ORDER_INDX = 0;
    private static final int ORDER_STATUS_INDX = 3;
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

        criteria = new Criteria();
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
    @org.junit.jupiter.api.Order(1)
    void createOrder_returnIdOfNewOrderMoreThan0_True() throws DAOException, InterruptedException, SQLException {
        createdDishId = orderDAO.createOrder(new Order(), 9);
        Assertions.assertTrue(createdDishId > 0);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    void createOrderDetails_returnTrue() throws DAOException, SQLException {
        Assertions.assertTrue(orderDAO.createOrderDetails(createdDishId, 2, 3, "takeaway"));

        // because of order details ID is foreign key to order ID
        statement.executeUpdate(String.format(DELETE_ORDER_DETAILS_BY_ID, createdDishId));
        statement.executeUpdate(String.format(DELETE_ORDER_BY_ID, createdDishId));
    }

    @Test
    void find_orderWithID_1_equals() throws DAOException, SQLException {
        int orderId = 1;

        resultSet = statement.executeQuery(String.format(GET_ORDER_BY_ID, orderId));
        resultSet.next();

        criteria.add(SearchCriteria.Orders.ID.name(), orderId);

        Assertions.assertEquals(resultSet.getTimestamp(2), orderDAO.find(criteria).get(FOUND_ORDER_INDX).getDateTime());
    }

    @Test
    void find_numberOfCookedOrder_equals() throws DAOException, SQLException {
        int userId = 9;
        String cookedStatus = "cooked";
        int countOfOrdersIndx = 1;

        resultSet = statement.executeQuery(String.format(GET_COUNT_OF_COOKED_ORDER_BY_ID, userId));
        resultSet.next();

        criteria.add(SearchCriteria.Orders.USER_ID.name(), userId);
        criteria.add(SearchCriteria.Orders.ORDER_STATUS.name(), cookedStatus);

        Assertions.assertEquals(resultSet.getInt(countOfOrdersIndx), orderDAO.find(criteria).size());
    }

    @Test
    void findOrdersWithUsersInfo_SizeOfListMoreThan0_true() throws DAOException {
        criteria.add(SearchCriteria.Orders.ORDER_STATUS.toString(), "cooked");

        Assertions.assertTrue(orderDAO.findOrdersWithUsersInfo(criteria).size() > 0);
    }

    @Test
    void updateOrderStatus_withOrderIdEq5_true() throws DAOException, SQLException {
        int orderIdForUpdate = 1;
        String status = "confirmed";

        resultSet = statement.executeQuery(String.format(GET_ORDER_BY_ID, orderIdForUpdate));
        resultSet.next();
        String previousStatus = resultSet.getString(ORDER_STATUS_INDX);

        Assertions.assertTrue(orderDAO.updateOrderStatus(orderIdForUpdate, status));

        orderDAO.updateOrderStatus(orderIdForUpdate, previousStatus);
    }

    @Test
    void findOrdersWithDishInfo_SizeOfListMoreThan0_true() throws DAOException {
        criteria.add(SearchCriteria.Orders.ORDER_STATUS.name(), "cooked");

        Assertions.assertTrue(orderDAO.findOrdersWithDishInfo(criteria).size() > 0);
    }
}