package com.epam.restaurant.dao.impl;

import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.OrderForCooking;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.builder.RegistrationUserDataBuilder;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.OrderDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SQLOrderDAO implements OrderDAO {
    private static final Logger LOGGER = LogManager.getLogger(SQLOrderDAO.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String CREATE_ORDER_QUERY = "INSERT INTO orders (date, order_status, user_id) VALUES (?, ?, ?)";
    private static final String CREATE_ORDER_DETAIL_QUERY = "INSERT INTO order_details (orders_id, menu_dishes_id, quantity, methodOfReceiving) VALUES (?, ?, ?, ?)";
    private static final String FIND_ORDER_BY_CRITERIA_QUERY = "SELECT ord.id, SUM(ordd.quantity * m.price) as 'total',  ord.date, methodOfReceiving, order_status FROM orders ord LEFT JOIN order_details ordd on ordd.orders_id = ord.id LEFT JOIN menu m on ordd.menu_dishes_id = m.dishes_id where %s group by ord.id;";
    private static final String GET_ORDERS_WITHOUT_STATUS_INPROCESSING_QUERY = "SELECT ord.id, SUM(ordd.quantity * m.price) as 'total',  ord.date, methodOfReceiving, order_status FROM orders ord LEFT JOIN order_details ordd on ordd.orders_id = ord.id LEFT JOIN menu m on ordd.menu_dishes_id = m.dishes_id where user_id=? AND order_status!='in processing' group by ord.id;";
    private static final String FIND_ORDER_WITH_USER_BY_CRITERIA_QUERY = "SELECT ord.id, SUM(ordd.quantity * m.price) as 'total',  ord.date, methodOfReceiving, u.name, phone_number, email FROM orders ord LEFT JOIN order_details ordd on ordd.orders_id = ord.id LEFT JOIN menu m on ordd.menu_dishes_id = m.dishes_id LEFT JOIN users u on user_id=u.id where %s group by ord.id;";
    private static final String FIND_ORDER_WITH_DISH_BY_CRITERIA_QUERY = "SELECT ord.id,  m.name, methodOfReceiving, quantity FROM orders ord LEFT JOIN order_details ordd on ordd.orders_id = ord.id LEFT JOIN menu m on ordd.menu_dishes_id = m.dishes_id where %s;";
    private static final String UPDATE_ORDER_STATUS_QUERY = "UPDATE orders SET order_status=? where id=?;";

    private static final String AND = "AND ";

    private static final String IN_PROCESSING = "in processing";
    private static final int GENERATED_KEYS = 1;

    @Override
    public int createOrder(Order order, int userId) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = DAOProvider.getInstance().getTransactionDAO().getConnectionHolder().get();
            if (connection == null) {
                connection = connectionPool.takeConnection();
            }

            preparedStatement = connection.prepareStatement(CREATE_ORDER_QUERY, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStatement.setString(2, IN_PROCESSING);
            preparedStatement.setInt(3, userId);
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getInt(GENERATED_KEYS);

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
    public boolean createOrderDetails(int oderId, int menuId, Integer quantity, String methodOfReceiving) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        Connection connection = null;

        try {
            connection = DAOProvider.getInstance().getTransactionDAO().getConnectionHolder().get();
            if (connection == null) {
                connection = connectionPool.takeConnection();
            }

            preparedStatement = connection.prepareStatement(CREATE_ORDER_DETAIL_QUERY);
            preparedStatement.setInt(1, oderId);
            preparedStatement.setInt(2, menuId);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, methodOfReceiving);
            preparedStatement.executeUpdate();

            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to createOrderDetails", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public List<Order> find(Criteria criteria) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = connectionPool.takeConnection();

            Map<String, Object> criterias = criteria.getCriteria();

            StringBuilder whereBuilder = new StringBuilder("");
            for (String criteriaName : criterias.keySet()) {
                whereBuilder.append(String.format("%s=? %s", criteriaName.toLowerCase(), AND));
            }
            whereBuilder = new StringBuilder(whereBuilder.substring(0, whereBuilder.length() - AND.length()));
            String queryBuilder = String.format(FIND_ORDER_BY_CRITERIA_QUERY, whereBuilder);

            statement = connection.prepareStatement(queryBuilder);
            int i = 1;
            for (Object value : criterias.values()) {
                statement.setString(i, value.toString());
                i++;
            }
            resultSet = statement.executeQuery();

            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt(1));
                order.setTotalPrice(resultSet.getBigDecimal(2));
                order.setDateTime(resultSet.getTimestamp(3));
                order.setMethodOfReceiving(resultSet.getString(4));
                order.setStatus(resultSet.getString(5));
                orders.add(order);
            }

            return orders;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to find order", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public List<Order> getHistoryOfOrders(int userId) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.prepareStatement(GET_ORDERS_WITHOUT_STATUS_INPROCESSING_QUERY);
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();

            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt(1));
                order.setTotalPrice(resultSet.getBigDecimal(2));
                order.setDateTime(resultSet.getTimestamp(3));
                order.setMethodOfReceiving(resultSet.getString(4));
                order.setStatus(resultSet.getString(5));
                orders.add(order);
            }

            return orders;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to find", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public Map<Order, RegistrationUserData> findOrdersWithUsersInfo(Criteria criteria) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = connectionPool.takeConnection();

            Map<String, Object> criterias = criteria.getCriteria();

            StringBuilder whereBuilder = new StringBuilder("");
            for (String criteriaName : criterias.keySet()) {
                whereBuilder.append(String.format("%s=? %s", criteriaName.toLowerCase(), AND));
            }
            whereBuilder = new StringBuilder(whereBuilder.substring(0, whereBuilder.length() - AND.length()));
            String queryBuilder = String.format(FIND_ORDER_WITH_USER_BY_CRITERIA_QUERY, whereBuilder);

            statement = connection.prepareStatement(queryBuilder);
            int i = 1;
            for (Object value : criterias.values()) {
                statement.setString(i, value.toString());
                i++;
            }
            resultSet = statement.executeQuery();

            Map<Order, RegistrationUserData> orderUserDataMap = new LinkedHashMap<>();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt(1));
                order.setTotalPrice(resultSet.getBigDecimal(2));
                order.setDateTime(resultSet.getTimestamp(3));
                order.setMethodOfReceiving(resultSet.getString(4));

                RegistrationUserData userData = new RegistrationUserDataBuilder()
                        .setName(resultSet.getString(5))
                        .setPhoneNumber(resultSet.getString(6))
                        .setEmail(resultSet.getString(7))
                        .build();

                orderUserDataMap.put(order, userData);
            }

            return orderUserDataMap;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to findOrdersWithUsersInfo", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    public boolean updateOrderStatus(int orderID, String status) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.prepareStatement(UPDATE_ORDER_STATUS_QUERY);
            statement.setString(1, status);
            statement.setInt(2, orderID);
            statement.executeUpdate();

            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to confirmOrder", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public List<OrderForCooking> findOrdersWithDishInfo(Criteria criteria) throws DAOException {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        Connection connection = null;

        try {
            connection = connectionPool.takeConnection();

            Map<String, Object> criterias = criteria.getCriteria();

            StringBuilder whereBuilder = new StringBuilder("");
            for (String criteriaName : criterias.keySet()) {
                whereBuilder.append(String.format("%s=? %s", criteriaName.toLowerCase(), AND));
            }
            whereBuilder = new StringBuilder(whereBuilder.substring(0, whereBuilder.length() - AND.length()));
            String queryBuilder = String.format(FIND_ORDER_WITH_DISH_BY_CRITERIA_QUERY, whereBuilder);

            statement = connection.prepareStatement(queryBuilder);
            int i = 1;
            for (Object value : criterias.values()) {
                statement.setString(i, value.toString());
                i++;
            }
            resultSet = statement.executeQuery();

            List<OrderForCooking> orderForCookingList = new ArrayList<>();
            while (resultSet.next()) {
                OrderForCooking order = new OrderForCooking();
                order.setOrderId(resultSet.getInt(1));
                order.setDishName(resultSet.getString(2));
                order.setMethodOfReceiving(resultSet.getString(3));
                order.setQuantity(resultSet.getInt(4));
                orderForCookingList.add(order);
            }

            return orderForCookingList;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to findOrdersWithUsersInfo", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }
}
