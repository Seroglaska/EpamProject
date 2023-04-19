package com.epam.restaurant.service.validation;

import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;

import java.util.List;
import java.util.Map;

public final class DishValidator {
    private static final Integer ID_IS_MISSING = null;
    private static final Integer FOUND_DISH_INDX = 0;
    private static final Integer OK_STATUS = 0;

    private DishValidator() {
    }

    public static void validate(Criteria criteria) throws ValidationException {
        Map<String, Object> criterias = criteria.getCriteria();
        for (Map.Entry<String, Object> entry : criterias.entrySet()) {
            if (entry.getKey().equals(SearchCriteria.Dishes.DISHES_ID.name())) {
                if (entry.getValue() == null || !entry.getValue().toString().matches(ValidationType.ID.getRegex())) {
                    throw new ValidationException(ValidationType.ID.getErrorMsg());
                }
            } else if (entry.getKey().equals(SearchCriteria.Dishes.NAME.name())) {
                if (entry.getValue() == null || !entry.getValue().toString().matches(ValidationType.NAME.getRegex())) {
                    throw new ValidationException(ValidationType.NAME.getErrorMsg());
                }
            } else if (entry.getKey().equals(SearchCriteria.Dishes.PRICE.name())) {
                if (entry.getValue() == null || !entry.getValue().toString().matches(ValidationType.PRICE.getRegex())) {
                    throw new ValidationException(ValidationType.PRICE.getErrorMsg());
                }
            } else if (entry.getKey().equals(SearchCriteria.Dishes.STATUS.name())) {
                if (entry.getValue() == null || !entry.getValue().toString().matches(ValidationType.STATUS.getRegex())) {
                    throw new ValidationException(ValidationType.STATUS.getErrorMsg());
                }
            } else if (entry.getKey().equals(SearchCriteria.Dishes.CATEGORY_ID.name())) {
                if (entry.getValue() == null || !entry.getValue().toString().matches(ValidationType.ID.getRegex())) {
                    throw new ValidationException(ValidationType.ID.getErrorMsg());
                }
            } else if (entry.getKey().equals(SearchCriteria.Dishes.DESCRIPTION.name()) && entry.getValue() == null) {
                throw new ValidationException("DESCRIPTION shouldn't be null");
            }
        }
    }

    public static void validate(Dish dish) throws ValidationException, DAOException {
        if (dish.getPrice() == null || !dish.getPrice().toString().matches(ValidationType.PRICE.getRegex())) {
            throw new ValidationException(ValidationType.PRICE.getErrorMsg());
        } else if (dish.getName() == null || !dish.getName().matches(ValidationType.NAME.getRegex())) {
            throw new ValidationException(ValidationType.NAME.getErrorMsg());
        } else if (dish.getDescription() == null) {
            throw new ValidationException("DESCRIPTION shouldn't be null");
        } else if (!Integer.toString(dish.getId()).matches(ValidationType.ID.getRegex())) {
            throw new ValidationException(ValidationType.ID.getErrorMsg());
        } else if (dish.getPhotoLink() == null) {
            throw new ValidationException("PHOTO shouldn't be null");
        }

        checkAvailability(dish.getName(), dish.getPhotoLink(), dish.getId());
    }

    /**
     * Check if the name or link to the photo is not used in the database, because these fields should be unique in menu
     *
     * @param name      of dish
     * @param photoLink of dish
     * @throws DAOException        if problems with method find(criteria)
     * @throws ValidationException if the name or photo is already in use
     */
    private static void checkAvailability(String name, String photoLink, Integer id) throws DAOException, ValidationException {
        Criteria criteria = new Criteria();
        criteria.add(SearchCriteria.Dishes.NAME.name(), name);
        criteria.add(SearchCriteria.Dishes.STATUS.name(), OK_STATUS);

        List<Dish> foundDishes = DAOProvider.getInstance().getMenuDAO().find(criteria);
        if (!foundDishes.isEmpty() && id == 0) {  // when  add new dish to menu
            throw new ValidationException("Dish with such NAME has already exist");
        } else if (!foundDishes.isEmpty() && foundDishes.get(FOUND_DISH_INDX).getId() != id) {  // when edit exist dish in menu
            throw new ValidationException("Dish with such NAME has already exist");
        }
        criteria.remove(SearchCriteria.Dishes.NAME.name());

        criteria.add(SearchCriteria.Dishes.URL.name(), photoLink);
        foundDishes = DAOProvider.getInstance().getMenuDAO().find(criteria);
        if (!foundDishes.isEmpty() && id == 0) {  // when  add new dish to menu
            throw new ValidationException("Dish with such PHOTO has already exist");
        } else if (!foundDishes.isEmpty() && foundDishes.get(FOUND_DISH_INDX).getId() != id) {  // when edit exist dish in menu
            throw new ValidationException("Dish with such PHOTO has already exist");
        }
    }
}
