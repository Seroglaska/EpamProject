package com.epam.restaurant.service.validation;

import com.epam.restaurant.bean.Category;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;

import java.util.List;

public final class CategoryValidator {
    private static final Integer ID_IS_MISSING = null;
    private static final Integer FOUND_DISH_INDX = 0;
    private static final Integer OK_STATUS = 0;

    private CategoryValidator() {
    }

    public static void validate(Integer editedCategoryId, String newCategoryName) throws ValidationException, DAOException {
        if (editedCategoryId == null) {
            throw new ValidationException("editedCategoryId is null");
        } else if (!editedCategoryId.toString().matches(ValidationType.ID.getRegex())) {
            throw new ValidationException(ValidationType.ID.getErrorMsg());
        } else if (newCategoryName == null) {
            throw new ValidationException("newCategoryName is null");
        } else if (!newCategoryName.matches(ValidationType.NAME.getRegex())) {
            throw new ValidationException(ValidationType.NAME.getErrorMsg());
        }

        checkAvailability(newCategoryName, editedCategoryId);
    }

    public static void validate(String newCategoryName) throws ValidationException, DAOException {
        if (newCategoryName == null) {
            throw new ValidationException("newCategoryName is null");
        } else if (!newCategoryName.matches(ValidationType.NAME.getRegex())) {
            throw new ValidationException(ValidationType.NAME.getErrorMsg());
        }

        checkAvailability(newCategoryName, ID_IS_MISSING);
    }

    public static void validate(Integer editedCategoryId) throws ValidationException {
        if (editedCategoryId == null) {
            throw new ValidationException("editedCategoryId is null");
        } else if (!editedCategoryId.toString().matches(ValidationType.ID.getRegex())) {
            throw new ValidationException(ValidationType.ID.getErrorMsg());
        }
    }

    /**
     * Check if the name of category is not used in the database, because these fields must be unique in menu
     * Firstly, check if there is already category with such name in the menu.
     * If YES Then check if the category is null(that's mean it's a new category) OR the id of the found category doesn't
     * match the id of the category being modified - Category cannot be added to the database, because such category already exists
     *
     * @param name of category
     * @param id   of category
     * @throws DAOException        if problems with method find(criteria)
     * @throws ValidationException if the name or photo is already in use
     */
    private static void checkAvailability(String name, Integer id) throws DAOException, ValidationException {
        Criteria criteria = new Criteria();
        criteria.add(SearchCriteria.Categories.NAME.name(), name);
        criteria.add(SearchCriteria.Dishes.STATUS.name(), OK_STATUS);

        List<Category> foundDishes = DAOProvider.getInstance().getMenuDAO().findCategory(criteria);

        if (!foundDishes.isEmpty() &&
                (id == null || foundDishes.get(FOUND_DISH_INDX).getId() != id)) {
            throw new ValidationException("Category with such NAME has already exist");
        }
    }
}
