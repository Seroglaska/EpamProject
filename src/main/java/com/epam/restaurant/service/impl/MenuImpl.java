package com.epam.restaurant.service.impl;

import com.epam.restaurant.bean.Category;
import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Menu;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.MenuDAO;
import com.epam.restaurant.service.MenuService;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.validation.CategoryValidator;
import com.epam.restaurant.service.validation.DishValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class MenuImpl implements MenuService {
    private static final DAOProvider daoProvider = DAOProvider.getInstance();
    private static final MenuDAO menuDAO = daoProvider.getMenuDAO();
    private static final Logger LOGGER = LogManager.getLogger(MenuImpl.class);

    @Override
    public Menu getMenu() throws ServiceException {
        try {
            return menuDAO.getMenu();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Category> getCategories() throws ServiceException {
        try {
            return menuDAO.getCategories();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Dish> find(Criteria criteria) throws ServiceException {
        DishValidator.validate(criteria);

        try {
            return menuDAO.find(criteria);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int removeDish(Criteria criteria) throws ServiceException {
        DishValidator.validate(criteria);

        try {
            return menuDAO.removeDish(criteria);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean editCategory(Integer editedCategoryId, String newCategoryName) throws ServiceException {
        try {
            CategoryValidator.validate(editedCategoryId, newCategoryName);

            return menuDAO.editCategory(editedCategoryId, newCategoryName);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean editDish(Dish dish) throws ServiceException {
        try {
            DishValidator.validate(dish);

            return menuDAO.editDish(dish);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public int addDish(Dish dish) throws ServiceException {
        try {
            DishValidator.validate(dish);
            int id = 0;

            daoProvider.getTransactionDAO().startTransaction();
            id = menuDAO.addDish(dish);
            daoProvider.getTransactionDAO().commit();

            return id;
        } catch (DAOException e) {
            try {
                daoProvider.getTransactionDAO().rollback();
                throw new ServiceException(e);
            } catch (DAOException ex) {
                LOGGER.error("error to rollback when adding dish to menu", ex);
                throw new ServiceException(ex);
            }
        }
    }

    @Override
    public int addCategory(String categoryName) throws ServiceException {
        try {
            CategoryValidator.validate(categoryName);

            return menuDAO.addCategory(categoryName);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean removeCategory(Integer categoryId) throws ServiceException {
        CategoryValidator.validate(categoryId);

        try {
            return menuDAO.removeCategory(categoryId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
