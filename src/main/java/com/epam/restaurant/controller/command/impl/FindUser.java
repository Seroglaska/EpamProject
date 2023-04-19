package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.ServiceProvider;
import com.epam.restaurant.service.validation.ValidationException;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FindUser implements Command {
    private static final Logger LOGGER = LogManager.getLogger(FindUser.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String LOGIN_PARAM = "login";
    private static final String JSON_UTF8_TYPE = "application/json; charset=UTF-8";
    private static final String ERROR_MSG_JSON = "[{\"validationError\": \"true\", \"message\": \"%s\"}]";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        String login = request.getParameter(LOGIN_PARAM);
        PrintWriter writer = null;

        try {
            writer = response.getWriter();

            List<RegistrationUserData> userDataList = serviceProvider.getUserService().findByLoginPattern(login);

            writer.println( new Gson().toJson(userDataList));
        } catch (IOException e) {
            try {
                LOGGER.error("Error to get writer when try to Edit Dish in Menu", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                throw new ServletException(e);
            } catch (IOException ex) {
                LOGGER.error("Error to send error writer when try to Edit Dish in Menu", e);
                throw new ServletException(e);
            }
        } catch (ValidationException | NumberFormatException e) {
            response.setContentType(JSON_UTF8_TYPE);
            writer.println(String.format(ERROR_MSG_JSON, e.getMessage()));
        }
    }
}
