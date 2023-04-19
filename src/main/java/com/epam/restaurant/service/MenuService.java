package com.epam.restaurant.service;

import com.epam.restaurant.bean.Category;
import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Menu;
import com.epam.restaurant.bean.criteria.Criteria;

import java.util.List;

/**
 * The ${@code MenuService} interface provides methods for working with {@link Menu}
 * @see Dish
 * @see Criteria
 */
public interface MenuService {
    /**
     * Returns a menu that contains information about dishes
     * @return menu
     * @throws ServiceException if an error occurred in the method
     */
    Menu getMenu() throws ServiceException;

    /**
     * Returns a list of available categories
     * @return a list of categories
     * @throws ServiceException if an error occurred in the method
     */
    List<Category> getCategories() throws ServiceException;

    /**
     * Returns a list of dishes matching the criteria
     * @param criteria which dish should correspond to
     * @return a list of dishes
     * @throws ServiceException if an error occurred in the method
     */
    List<Dish> find(Criteria criteria) throws ServiceException;

    /**
     * Removes dishes that match the criteria
     * @param criteria which dish should correspond to
     * @return quantity of deleted dishes
     * @throws ServiceException if an error occurred in the method
     */
    int removeDish(Criteria criteria) throws ServiceException;

    /**
     * Changes the category name
     * @param editedCategoryId ID of the category being edited
     * @param newCategoryName new category name
     * @return true on success or false on failure when editing the category name
     * @throws ServiceException if an error occurred in the method
     */
    boolean editCategory(Integer editedCategoryId, String newCategoryName) throws ServiceException;

    /**
     * Edits information about the dish
     * @param dish a dish containing changes
     * @return true on success or false on failure when editing the dish
     * @throws ServiceException if an error occurred in the method
     */
    boolean editDish(Dish dish) throws ServiceException;

    /**
     * Adds a dish to the menu
     * @param dish to add to the database
     * @return the id of the added dish
     * @throws ServiceException if an error occurred in the method
     */
    int addDish(Dish dish) throws ServiceException;

    /**
     * Adds a category to the menu
     * @param categoryName new category name
     * @return the id of the added category
     * @throws ServiceException if an error occurred in the method
     */
    int addCategory(String categoryName) throws ServiceException;

    /**
     * Deletes a category from the menu
     * @param categoryId ID of the category to delete
     * @return true on success or false on failure when removing the category
     * @throws ServiceException if an error occurred in the method
     */
    boolean removeCategory(Integer categoryId) throws ServiceException;
}
