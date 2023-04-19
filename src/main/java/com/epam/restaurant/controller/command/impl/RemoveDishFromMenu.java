package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Menu;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.MenuService;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.ServiceProvider;
import com.epam.restaurant.service.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class RemoveDishFromMenu implements Command {
    private static final Logger LOGGER = LogManager.getLogger(RemoveDishFromMenu.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String DISH_ID_PARAM = "dishId";
    private static final String MENU_ATTR = "menu";
    private static final String JSON_UTF8_TYPE = "application/json; charset=UTF-8";
    private static final String ERROR_MSG_JSON = "{\"validationError\": \"true\", \"message\": \"%s\"}";
    private static final String RESULT_SUCCESS_JSON = "{\"result\": \"success\"}";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        int idToRemove = Integer.parseInt(request.getParameter(DISH_ID_PARAM));
        PrintWriter writer = null;

        try {
            writer = response.getWriter();

            removeFromDB(idToRemove);

            removeDishFromSessionMenu(request, idToRemove);

            writer.println(RESULT_SUCCESS_JSON);
        } catch (IOException e) {
            try {
                LOGGER.error("Error to get writer when try to remove dish from menu", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                throw new ServletException(e);
            } catch (IOException ex) {
                LOGGER.error("Error to send error writer when try to remove dish from menu", e);
                throw new ServletException(e);
            }
        } catch (ValidationException e) {
            response.setContentType(JSON_UTF8_TYPE);
            writer.println(String.format(ERROR_MSG_JSON, e.getMessage()));
        }
    }

    private void removeFromDB(int idToRemove) throws ServiceException {
        MenuService menuService = serviceProvider.getMenuService();

        Criteria criteria = new Criteria();
        criteria.add(SearchCriteria.Dishes.DISHES_ID.toString(), idToRemove);

        menuService.removeDish(criteria);
    }

    private void removeDishFromSessionMenu(HttpServletRequest request, int idToRemove) {
        HttpSession session = request.getSession();
        Menu menu = (Menu) session.getAttribute(MENU_ATTR);

        List<Dish> dishes = menu.getDishes();
        for (Dish dish : dishes) {
            if (dish.getId() == idToRemove) {
                dishes.remove(dish);
                break;
            }
        }
    }
}
