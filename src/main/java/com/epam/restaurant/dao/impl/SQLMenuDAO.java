package com.epam.restaurant.dao.impl;

import com.epam.restaurant.bean.Category;
import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Menu;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.dao.ConnectionPool;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.MenuDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SQLMenuDAO implements MenuDAO {
    private static final Logger LOGGER = LogManager.getLogger(SQLMenuDAO.class);
    private static final ConnectionPool connectionPool = ConnectionPool.getInstance();

    private static final String GET_ALL_CATEGORIES_QUERY = "SELECT * FROM categories WHERE status=0 ORDER BY id";
    private static final String GET_MENU_QUERY = "SELECT dishes_id, name, description, price, category_id, url FROM menu LEFT JOIN dish_photos ON menu_dishes_id = dishes_id where status=0;";
    private static final String FIND_DISH_BY_CRITERIA_QUERY = "SELECT dishes_id, name, description, price, category_id, ph.url FROM menu LEFT JOIN dish_photos ph ON(dishes_id = menu_dishes_id) WHERE ";
    private static final String FIND_CATEGORY_BY_CRITERIA_QUERY = "SELECT id, name, status FROM restaurant.categories WHERE ";
    private static final String REMOVE_DISH_BY_CRITERIA_QUERY = "UPDATE menu SET status=1 where ";
    private static final String REMOVE_CATEGORY_QUERY = "UPDATE categories SET status=1 where id=?";
    private static final String EDIT_CATEGORY_QUERY = "UPDATE categories SET name=? where id=?";
    private static final String EDIT_DISH_QUERY = "UPDATE menu SET name=?, description=?, price=? where dishes_id=?";
    private static final String EDIT_DISH_PHOTO_QUERY = "UPDATE dish_photos SET url=? where menu_dishes_id=?";
    private static final String ADD_DISH_QUERY = "INSERT INTO menu(price, name, description, status,  category_id) VALUES(?,?,?, 0, ?)";
    private static final String ADD_CATEGORY_QUERY = "INSERT INTO categories(name, status) VALUES(?, 0)";
    private static final String ADD_PHOTO_QUERY = "INSERT INTO dish_photos(url, menu_dishes_id) VALUES(?, ?)";
    private static final int GENERATED_KEYS_COLUMN_INDX = 1;

    private static final String AND = "AND ";

    @Override
    public Menu getMenu() throws DAOException {
        Menu menu = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(GET_MENU_QUERY);

            if (!resultSet.isBeforeFirst()) {
                return new Menu();
            }

            menu = new Menu();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String description = resultSet.getString(3);
                BigDecimal price = BigDecimal.valueOf(resultSet.getDouble(4));
                int categoryId = resultSet.getInt(5);
                String photoLink = resultSet.getString(6);

                menu.add(new Dish(id, price, name, description, categoryId, photoLink));
            }
        } catch (SQLException e) {
            throw new DAOException("Error when trying to get menu", e);
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

        return menu;
    }

    @Override
    public List<Category> getCategories() throws DAOException {
        List<Category> categories = null;

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery(GET_ALL_CATEGORIES_QUERY);

            if (!resultSet.isBeforeFirst()) {
                return Collections.emptyList();
            }

            categories = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                int status = resultSet.getInt(3);

                categories.add(new Category(id, name, status));
            }
        } catch (SQLException e) {
            throw new DAOException("Error when trying to get categories", e);
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

        return categories;
    }

    @Override
    public List<Dish> find(Criteria criteria) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        Map<String, Object> criterias = criteria.getCriteria();

        try {
            connection = connectionPool.takeConnection();

            StringBuilder queryBuilder = new StringBuilder(FIND_DISH_BY_CRITERIA_QUERY);
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

            List<Dish> dishes = new ArrayList<>();
            while (resultSet.next()) {
                Dish dish = new Dish();
                dish.setId(resultSet.getInt(1));
                dish.setName(resultSet.getString(2));
                dish.setDescription(resultSet.getString(3));
                dish.setPrice(BigDecimal.valueOf(resultSet.getDouble(4)));
                dish.setCategoryId(resultSet.getInt(5));

                dishes.add(dish);
            }

            return dishes;

        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a statement dish find query", e);
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
    public List<Category> findCategory(Criteria criteria) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        Map<String, Object> criterias = criteria.getCriteria();

        try {
            connection = connectionPool.takeConnection();

            StringBuilder queryBuilder = new StringBuilder(FIND_CATEGORY_BY_CRITERIA_QUERY);
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

            List<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt(1));
                category.setName(resultSet.getString(2));
                category.setStatus(resultSet.getInt(3));

                categories.add(category);
            }

            return categories;

        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a statement category find query", e);
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
    public int removeDish(Criteria criteria) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        Map<String, Object> criterias = criteria.getCriteria();

        try {
            connection = connectionPool.takeConnection();

            StringBuilder queryBuilder = new StringBuilder(REMOVE_DISH_BY_CRITERIA_QUERY);
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

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a statement dish find query", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, null);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public boolean editCategory(int editedCategoryId, String newCategoryName) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.prepareStatement(EDIT_CATEGORY_QUERY);
            statement.setString(1, newCategoryName);
            statement.setInt(2, editedCategoryId);

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a prepareStatement in edit category query", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, null);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public boolean editDish(Dish dish) throws DAOException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.prepareStatement(EDIT_DISH_PHOTO_QUERY);
            statement.setString(1, dish.getPhotoLink());
            statement.setInt(2, dish.getId());
            statement.executeUpdate();

            statement = connection.prepareStatement(EDIT_DISH_QUERY);
            statement.setString(1, dish.getName());
            statement.setString(2, dish.getDescription());
            statement.setBigDecimal(3, dish.getPrice());
            statement.setInt(4, dish.getId());

            statement.executeUpdate();

            return true;
        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a prepareStatement in edit category query", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DAOException("Error when trying to take connection", e);
        } finally {
            try {
                connectionPool.closeConnection(connection, statement, null);
            } catch (SQLException e) {
                LOGGER.error("Error to close connection...", e);
            }
        }
    }

    @Override
    public int addDish(Dish dish) throws DAOException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            connection = DAOProvider.getInstance().getTransactionDAO().getConnectionHolder().get();
            if (connection == null) {
                connection = connectionPool.takeConnection();
            }

            statement = connection.prepareStatement(ADD_DISH_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setBigDecimal(1, dish.getPrice());
            statement.setString(2, dish.getName());
            statement.setString(3, dish.getDescription());
            statement.setInt(4, dish.getCategoryId());
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int addedDishId = resultSet.getInt(GENERATED_KEYS_COLUMN_INDX);

            statement = connection.prepareStatement(ADD_PHOTO_QUERY);
            statement.setString(1, dish.getPhotoLink());
            statement.setInt(2, addedDishId);
            statement.executeUpdate();

            return addedDishId;
        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a prepareStatement in add Dish query", e);
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
    public int addCategory(String categoryName) throws DAOException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.prepareStatement(ADD_CATEGORY_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, categoryName);
            statement.executeUpdate();

            resultSet = statement.getGeneratedKeys();
            resultSet.next();

            return resultSet.getInt(GENERATED_KEYS_COLUMN_INDX);
        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a prepareStatement in add category query", e);
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
    public boolean removeCategory(int categoryId) throws DAOException {
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.takeConnection();

            statement = connection.prepareStatement(REMOVE_CATEGORY_QUERY);
            statement.setInt(1, categoryId);

            statement.executeUpdate();

            return  true;
        } catch (SQLException e) {
            throw new DAOException("Error when trying to create a prepareStatement in remove category query", e);
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
}
