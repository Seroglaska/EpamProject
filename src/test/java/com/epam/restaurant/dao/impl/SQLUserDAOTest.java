package com.epam.restaurant.dao.impl;

import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.UserDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class SQLUserDAOTest {
    private static ConnectionPool connectionPool;
    private static final UserDAO userDAO = DAOProvider.getInstance().getUserDAO();
    private static final String DELETE_USER_BY_LOGIN_QUERY = "DELETE FROM users WHERE login='%s'";
    private static final int FOUND_USER = 0;

    private final RegistrationUserData newUser = new RegistrationUserData("testUs4", "123".toCharArray(), "TestUser", "+375446785678", "ant@gmail.com", 2);
    private static RegistrationUserData existentUser;

    private final String non_existent_login = "jhgjhj";
    private final char[] non_correct_password = new char[]{'1', '5', '8'};

    private static Criteria criteria;

    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    @BeforeAll
    static void beforeAll() throws SQLException, ClassNotFoundException, InterruptedException, DAOException {
        connectionPool = ConnectionPool.getInstance();
        connectionPool.initConnectionPool();

        criteria = new Criteria();
        criteria.add(SearchCriteria.Users.ID.name(), 1);
        existentUser = userDAO.find(criteria).get(FOUND_USER);
        existentUser.setPassword("123".toCharArray());
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
    void signIn_UserExist_AuthorizedUser() throws DAOException {
        Assertions.assertEquals(existentUser.getLogin(), userDAO.signIn(existentUser.getLogin(), existentUser.getPassword()).getLogin());
    }

    @Test
    void signIn_UserNotExist_Null() throws DAOException {
        Assertions.assertNull(userDAO.signIn(non_existent_login, non_correct_password).getLogin());
    }

    @Test
    void signIn_UserExistPasswordNotCorrect_Null() throws DAOException {
        Assertions.assertNull(userDAO.signIn(existentUser.getLogin(), non_correct_password).getLogin());
    }

    @Test
    void signUp_NewUser_true() throws DAOException, SQLException {
        Assertions.assertTrue(userDAO.signUp(newUser));
        statement.executeUpdate(String.format(DELETE_USER_BY_LOGIN_QUERY, newUser.getLogin()));
    }

    @Test
    void signUp_ThisUserAlreadyExist_false() throws DAOException {
        Assertions.assertFalse(userDAO.signUp(existentUser));
    }

    @Test
    void find_UserWithCriteriaExist_authorizedUser() throws DAOException {
        criteria.add(SearchCriteria.Users.LOGIN.toString(), existentUser.getLogin());

        Assertions.assertEquals(existentUser.getLogin(), userDAO.find(criteria).get(FOUND_USER).getLogin());
    }

    @Test
    void find_UserWithCriteriaNoExist_EmptyList_true() throws DAOException {
        criteria.add(SearchCriteria.Users.LOGIN.toString(), "fgfdsgdsfg");

        Assertions.assertTrue(userDAO.find(criteria).isEmpty());
    }

    @Test
    void updateUserName_succsesfull_true() throws DAOException {
        RegistrationUserData userData = new RegistrationUserData();
        userData.setName(existentUser.getName());

        userData.setLogin(existentUser.getLogin());
        Assertions.assertEquals(existentUser.getName(), userDAO.updateUser(userData).getName());
    }

    @Test
    void updateUserPhone_succsesfull_true() throws DAOException {
        RegistrationUserData userData = new RegistrationUserData();
        userData.setPhoneNumber(existentUser.getPhoneNumber());

        userData.setLogin(existentUser.getLogin());
        Assertions.assertEquals(existentUser.getPhoneNumber(),userDAO.updateUser(userData).getPhoneNumber());
    }

    @Test
    void updateUserEmail_successful_true() throws DAOException {
        RegistrationUserData userData = new RegistrationUserData();
        userData.setEmail(existentUser.getEmail());

        userData.setLogin(existentUser.getLogin());
        Assertions.assertEquals(existentUser.getEmail(), userDAO.updateUser(userData).getEmail());
    }
}