package com.epam.restaurant.dao.impl;

import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.builder.DishBuilder;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.MenuDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;

class SQLMenuDAOTest {
    private static ConnectionPool connectionPool;
    private static final MenuDAO menuDAO = DAOProvider.getInstance().getMenuDAO();

    private static final String UPDATE_DISH_STATUS_TO_OK_QUERY = "UPDATE menu SET status=0 where dishes_id=%s";
    private static final String UPDATE_CATEGORY_STATUS_TO_OK_QUERY = "UPDATE categories SET status=0 where id=%s";
    private static final String SELECT_DISH_STATUS_QUERY = "SELECT status FROM menu WHERE dishes_id=%S";
    private static final String SELECT_CATEGORY_STATUS_QUERY = "SELECT status FROM categories WHERE id=%S";
    private static final String GET_NUM_OF_OK_STATUS_CATEGORIES_QUERY = "SELECT count(id) FROM categories WHERE status=0";
    private static final String GET_NUM_OF_OK_STATUS_DISHES_QUERY = "SELECT count(dishes_id) FROM menu WHERE status=0";
    private static final String DELETE_CATEGORY_QUERY = "DELETE FROM categories WHERE id=%s";
    private static final String DELETE_DISH_QUERY = "DELETE FROM menu WHERE dishes_id=%s";
    private static final String DELETE_DISH_PHOTO_QUERY = "DELETE FROM dish_photos WHERE menu_dishes_id=%s";
    private Criteria criteria;
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
        if (connection != null){
            connection.close();
        }
    }

    @Test
    void getMenu_Success_true() throws DAOException {
        criteria.add(SearchCriteria.Dishes.DISHES_ID.toString(), 2);

        Dish dishWithSecondId = menuDAO.find(criteria).get(0);

        int secondDishIdInArray = 1;
        Assertions.assertEquals(dishWithSecondId.getName(), menuDAO.getMenu().getDishes().get(secondDishIdInArray).getName());
    }

    @Test
    void getCategories_NumOfCategoriesEquals5_true() throws DAOException, SQLException, InterruptedException {
        ResultSet resultSet = statement.executeQuery(GET_NUM_OF_OK_STATUS_CATEGORIES_QUERY);
        resultSet.next();
        int NumOfCategories = resultSet.getInt(1);

        Assertions.assertEquals(NumOfCategories, menuDAO.getCategories().size());
    }

    @Test
    void find_DishWithCriteriaNoExist_emptyList_true() throws DAOException {
        String noExistDishName = "sfsdfsdfsgs";
        criteria.add(SearchCriteria.Dishes.NAME.toString(), noExistDishName);

        Assertions.assertEquals(Collections.emptyList(), menuDAO.find(criteria));
    }

    @Test
    void find_DishWithCriteriaExist_true() throws DAOException, SQLException, InterruptedException {
        criteria.add(SearchCriteria.Dishes.STATUS.toString(), "0");

        ResultSet resultSet = statement.executeQuery(GET_NUM_OF_OK_STATUS_DISHES_QUERY);
        resultSet.next();
        int currentSizeOfMenu = resultSet.getInt(1);

        Assertions.assertEquals(currentSizeOfMenu, menuDAO.find(criteria).size());
    }

    @Test
    void remove_DishWithId3_numOfRemovedDishes_equal_1_true() throws DAOException, SQLException, InterruptedException {
        int dishToRemove = 3;
        
        ResultSet resultSet = statement.executeQuery(String.format(SELECT_DISH_STATUS_QUERY, dishToRemove));

        resultSet.next();
        int currentStatus = resultSet.getInt(1);

        criteria.add(SearchCriteria.Dishes.DISHES_ID.toString(), dishToRemove);

        int numOfRemovedDishes = 1;
        Assertions.assertEquals(numOfRemovedDishes, menuDAO.removeDish(criteria));

        if (currentStatus == 0) {
            statement.executeUpdate(String.format(UPDATE_DISH_STATUS_TO_OK_QUERY, dishToRemove));
        }
    }

    @Test
    void editCategory_ResultOfUpdateMoreThan0_true() throws DAOException {
        int editedCategoryId = 2;
        criteria.add(SearchCriteria.Dishes.DISHES_ID.toString(), editedCategoryId);

        List<Dish> dishes = menuDAO.find(criteria);
        String newCategoryName = dishes.get(0).getName();

        Assertions.assertTrue(menuDAO.editCategory(editedCategoryId, newCategoryName));
    }

    @Test
    void removeCategory_withExistId_true() throws DAOException, SQLException, InterruptedException {
        int categoryForRemove = 1;

        ResultSet resultSet = statement.executeQuery(String.format(SELECT_CATEGORY_STATUS_QUERY, categoryForRemove));

        resultSet.next();
        int currentStatus = resultSet.getInt(1);

        Assertions.assertTrue(menuDAO.removeCategory(categoryForRemove));

        if (currentStatus == 0) {
            statement.executeUpdate(String.format(UPDATE_CATEGORY_STATUS_TO_OK_QUERY, categoryForRemove));
        }

    }

    @Test
    void addCategory_Success_true() throws DAOException, SQLException, InterruptedException {
        int idOfAddedCategory = menuDAO.addCategory("New category");
        Assertions.assertTrue(idOfAddedCategory > 0);

        statement.execute(String.format(DELETE_CATEGORY_QUERY, idOfAddedCategory));
    }

    @Test
    void addDish_Success_true() throws DAOException, SQLException, InterruptedException {
        int idOfAddedDish = menuDAO.addDish( new DishBuilder()
                .setPrice(new BigDecimal("14.50"))
                .setName("New")
                .setDescription("description")
                .setCategoryId(2)
                .setPhotoLink("../../images/dishes/1.jpg")
                .build());
        Assertions.assertTrue(idOfAddedDish > 0);

        statement.execute(String.format(DELETE_DISH_PHOTO_QUERY, idOfAddedDish));
        statement.execute(String.format(DELETE_DISH_QUERY, idOfAddedDish));
    }
}