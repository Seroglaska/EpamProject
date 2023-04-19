package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.Category;
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
import java.io.PrintWriter;
import java.util.List;

public class RemoveCategory implements Command {
    private static final Logger LOGGER = LogManager.getLogger(RemoveCategory.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String CATEGORY_ID_PARAM = "categoryId";
    private static final String CATEGORIES_ATTR = "categories";
    private static final String JSON_UTF8_TYPE = "application/json; charset=UTF-8";
    private static final String ERROR_MSG_JSON = "{\"validationError\": \"true\", \"message\": \"%s\"}";
    private static final String RESULT_SUCCESS_JSON = "{\"result\": \"success\"}";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        int categoryId = Integer.parseInt(request.getParameter(CATEGORY_ID_PARAM));
        PrintWriter writer = null;

        try {
            writer = response.getWriter();

            MenuService menuService = serviceProvider.getMenuService();
            menuService.removeCategory(categoryId);

            List<Category> categories = (List<Category>) request.getSession().getAttribute(CATEGORIES_ATTR);
            for (Category category : categories) {
                if (category.getId() == categoryId) {
                    categories.remove(category);
                    break;
                }
            }

            writer.println(RESULT_SUCCESS_JSON);
        } catch (IOException e) {
            try {
                LOGGER.error("Error to get writer when try to remove category", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                throw new ServletException(e);
            } catch (IOException ex) {
                LOGGER.error("Error to send error writer when try to remove category", e);
                throw new ServletException(e);
            }
        } catch (ValidationException e) {
            response.setContentType(JSON_UTF8_TYPE);
            writer.println(String.format(ERROR_MSG_JSON, e.getMessage()));
        }
    }
}
