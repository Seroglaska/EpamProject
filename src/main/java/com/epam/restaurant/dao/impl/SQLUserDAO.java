package com.epam.restaurant.dao.impl;

import com.epam.restaurant.bean.AuthorizedUser;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.builder.RegistrationUserDataBuilder;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SQLUserDAO implements UserDAO {
    private static final Logger LOGGER = LogManager.getLogger(SQLUserDAO.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String USER_AUTHORIZATION_QUERY = "SELECT login, name, role_id, password FROM users WHERE login=?";
    private static final String CHECK_USER_EXISTENCE_QUERY = "SELECT id FROM users WHERE login=?";
    private static final String REGISTER_USER_QUERY = "INSERT INTO users(login, password, name, phone_number, email, role_id) VALUES(?,?,?,?,?,?)";
    private static final String FIND_USER_BY_CRITERIA_QUERY = "Select * from users where ";
    private static final String FIND_USER_BY_LOGIN_PATTERN_QUERY = "SELECT * FROM users WHERE login LIKE ? LIMIT 5";
    private static final String UPDATE_USER_BY_CRITERIA_QUERY = "UPDATE users SET ";

    private static final String AND = "AND ";
    private static final String COMMA = ", ";
    private static final String LOGIN_PATTERN = "{0}%";
    private static final String SERIAL_VERSION_UID_FIELD_NAME = "serialVersionUID";
    private static final int FOUND_USER = 0;

    @Override
    public AuthorizedUser signIn(String login, char[] password) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();

            preparedStatement = connection.prepareStatement(USER_AUTHORIZATION_QUERY);
            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return new AuthorizedUser();
            }
            if (!BCrypt.checkpw(
                    Arrays.toString(password),                   // auth user psw
                    resultSet.getString(4))) {   // psw from db
                return new AuthorizedUser();
            }

            AuthorizedUser user = new AuthorizedUser();

            user.setLogin(resultSet.getString(1));
            user.setName(resultSet.getString(2));
            user.setRoleId(resultSet.getInt(3));

            return user;

        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a prepared user authorization query", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } finally {
            Arrays.fill(password, ' ');

            try {
                connectionPool.closeConnection(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public synchronized boolean signUp(RegistrationUserData userData) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();

            preparedStatement = connection.prepareStatement(CHECK_USER_EXISTENCE_QUERY);
            preparedStatement.setString(1, userData.getLogin());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return false; // that means that the user with this username is already registered
            }

            preparedStatement = connection.prepareStatement(REGISTER_USER_QUERY);
            preparedStatement.setString(1, userData.getLogin());
            preparedStatement.setString(2, BCrypt.hashpw(Arrays.toString(userData.getPassword()), BCrypt.gensalt()));
            preparedStatement.setString(3, userData.getName());
            preparedStatement.setString(4, userData.getPhoneNumber());
            preparedStatement.setString(5, userData.getEmail());
            preparedStatement.setInt(6, userData.getRoleId());
            preparedStatement.executeUpdate();

            Arrays.fill(userData.getPassword(), ' ');

            return true;

        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a prepared user registration query", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, preparedStatement, resultSet);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public List<RegistrationUserData> find(Criteria criteria) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        Map<String, Object> criterias = criteria.getCriteria();

        try {
            connection = connectionPool.takeConnection();

            StringBuilder queryBuilder = new StringBuilder(FIND_USER_BY_CRITERIA_QUERY);
            for (String criteriaName : criterias.keySet()) {
                queryBuilder.append(String.format("%s=? %s", criteriaName.toLowerCase(), AND));
            }
            queryBuilder = new StringBuilder(queryBuilder.substring(0, queryBuilder.length() - AND.length()));

            statement = connection.prepareStatement(queryBuilder.toString());
            int i = 1;
            for (Object value : criterias.values()) {
                statement.setString(i, value.toString());
                i++;
            }

            resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return Collections.emptyList();
            }

            List<RegistrationUserData> users = new ArrayList<>();
            while (resultSet.next()) {
                RegistrationUserData user = new RegistrationUserDataBuilder()
                        .setId(resultSet.getInt(1))
                        .setLogin(resultSet.getString(2))
                        .setName(resultSet.getString(4))
                        .setPhoneNumber(resultSet.getString(5))
                        .setEmail(resultSet.getString(6))
                        .setRoleId(resultSet.getInt(7))
                        .build();
                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a statement user find query", e);
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
    public List<RegistrationUserData> findByLoginPattern(String login) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.prepareStatement(FIND_USER_BY_LOGIN_PATTERN_QUERY);
            statement.setString(1, MessageFormat.format(LOGIN_PATTERN, login));
            resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                return Collections.emptyList();
            }

            List<RegistrationUserData> users = new ArrayList<>();
            while (resultSet.next()) {
                RegistrationUserData user = new RegistrationUserDataBuilder()
                        .setId(resultSet.getInt(1))
                        .setLogin(resultSet.getString(2))
                        .setName(resultSet.getString(4))
                        .setPhoneNumber(resultSet.getString(5))
                        .setEmail(resultSet.getString(6))
                        .setRoleId(resultSet.getInt(7))
                        .build();

                users.add(user);
            }

            return users;

        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a statement user find query", e);
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
    public RegistrationUserData updateUser(RegistrationUserData userData) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.takeConnection();

            StringBuilder queryBuilder = new StringBuilder(UPDATE_USER_BY_CRITERIA_QUERY);

            final Field[] fields = userData.getClass().getDeclaredFields();
            List<String> values = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                final Object value = field.get(userData);
                if (value != null && !field.getName().equals(SERIAL_VERSION_UID_FIELD_NAME)) {
                    queryBuilder.append(String.format("%s=?, ", field.getName().replaceAll("\\B[A-Z]", "_$0").toLowerCase()));
                    values.add(value.toString());
                }
                field.setAccessible(false);
            }

            queryBuilder = new StringBuilder(queryBuilder.substring(0, queryBuilder.length() - COMMA.length()));
            queryBuilder.append(String.format(" where login='%s'", userData.getLogin()));

            statement = connection.prepareStatement(queryBuilder.toString());

            int i = 1;
            for (String value : values) {
                statement.setString(i, value);
                i++;
            }
            statement.executeUpdate();

            Criteria criteria = new Criteria();
            criteria.add(SearchCriteria.Users.LOGIN.name(), userData.getLogin());
            return find(criteria).get(FOUND_USER);

        } catch (SQLException e) {
            if (e.getClass() == com.mysql.cj.jdbc.exceptions.MysqlDataTruncation.class) {
                throw new DAOException("Error the allowed length of input data has been exceeded", e);
            }
            throw new DAOException("Error when trying to create a statement user edit query", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Eror when try to update user info", e);
            throw new DAOException("Eror when try to update user info");
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, null);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }
}
