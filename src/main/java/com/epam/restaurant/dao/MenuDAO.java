package com.epam.restaurant.dao;

import com.epam.restaurant.bean.Category;
import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Menu;
import com.epam.restaurant.bean.criteria.Criteria;

import java.util.List;

/**
 * The ${@code MenuDAO} interface provides methods for working with {@link Menu} in the database
 * @see Dish
 * @see Criteria
 */
public interface MenuDAO {
    /**
     * Returns a menu that contains information about dishes with status "0" from the database
     * @return menu
     * @throws DAOException if an error occurred in the method
     */
    Menu getMenu() throws DAOException;

    /**
     * Returns a list of available categories(with status "0") from the database
     * @return a list of categories
     * @throws DAOException if an error occurred in the method
     */
    List<Category> getCategories() throws DAOException;

    /**
     * Returns a list of dishes matching the criteria from the database
     * @param criteria which dish should correspond to
     * @return a list of dishes
     * @throws DAOException if an error occurred in the method
     */
    List<Dish> find(Criteria criteria) throws DAOException;

    List<Category> findCategory(Criteria criteria) throws DAOException;

    /**
     * Removes dishes that match the criteria from the database
     * @param criteria which dish should correspond to
     * @return quantity of deleted dishes
     * @throws DAOException if an error occurred in the method
     */
    int removeDish(Criteria criteria) throws DAOException;

    /**
     * Changes the category name in the database
     * @param editedCategoryId ID of the category being edited
     * @param newCategoryName new category name
     * @return true on success or false on failure when editing the category name
     * @throws DAOException if an error occurred in the method
     */
    boolean editCategory(int editedCategoryId, String newCategoryName) throws DAOException;

    /**
     * Edits information about the dish in the database
     * @param dish a dish containing changes
     * @return true on success or false on failure when editing the dish
     * @throws DAOException if an error occurred in the method
     */
    boolean editDish(Dish dish) throws DAOException;

    /**
     * Adds a dish to the menu in the database
     * @param dish to add to the database
     * @return the id of the added dish
     * @throws DAOException if an error occurred in the method
     */
    int addDish(Dish dish) throws DAOException;

    /**
     * Adds a category to the menu
     * @param categoryName new category name
     * @return the id of the added category
     * @throws DAOException if an error occurred in the method
     */
    int addCategory(String categoryName) throws DAOException;

    /**
     * Deletes a category from the menu in the database
     * @param categoryId ID of the category to delete
     * @return true on success or false on failure when removing the category
     * @throws DAOException if an error occurred in the method
     */
    boolean removeCategory(int categoryId) throws DAOException;
}
