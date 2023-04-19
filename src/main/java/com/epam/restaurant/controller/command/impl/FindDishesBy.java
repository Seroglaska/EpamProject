package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.criteria.Criteria;
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
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

public class FindDishesBy implements Command {
    private static final Logger LOGGER = LogManager.getLogger(FindDishesBy.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String CATEGORY_ID = "category_id";
    private static final String DISHES_ATTR = "dishes";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        Integer id = Integer.parseInt(request.getParameter(CATEGORY_ID));

        Criteria criteria = new Criteria();
        criteria.add(CATEGORY_ID, id);

        MenuService menuService = serviceProvider.getMenuService();

        try {
            List<Dish> dishes = menuService.find(criteria);
            request.getSession().setAttribute(DISHES_ATTR, dishes);
        } catch (ValidationException e) {
            try {
                request.getRequestDispatcher(MessageFormat.format("/home?invalidDish=true&errMsgUpdDish={0}", e.getMessage())).forward(request, response);
            } catch (IOException ex) {
                LOGGER.error("Error invalid address to forward when Find Dishes By", e);
            }
        }
    }
}
