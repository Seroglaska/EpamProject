package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.controller.command.Command;
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

public class EditUser implements Command {
    private static final Logger LOGGER = LogManager.getLogger(EditUser.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String USER_NAME_PARAM = "name";
    private static final String PHONE_NUMBER_PARAM = "phoneNumber";
    private static final String EMAIL_PARAM = "email";
    private static final String ROLE_ID_PARAM = "role";
    private static final String LOGIN_PARAM = "login";
    private static final String JSON_UTF8_TYPE = "application/json; charset=UTF-8";
    private static final String ERROR_MSG_JSON = "{\"validationError\": true, \"message\": \"%s\"}";
    private static final String SUCCESS_MSG_JSON = "{\"validationError\": false}";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            String userName = request.getParameter(USER_NAME_PARAM);
            String phoneNumber = request.getParameter(PHONE_NUMBER_PARAM);
            String email = request.getParameter(EMAIL_PARAM);
            String login = request.getParameter(LOGIN_PARAM);
            String roleId = request.getParameter(ROLE_ID_PARAM);

            RegistrationUserData userData = new RegistrationUserData();

            if (userName != null) {
                userData.setName(userName);
            } else if (phoneNumber != null) {
                userData.setPhoneNumber(phoneNumber);
            } else if (email != null) {
                userData.setEmail(email);
            } else if (roleId != null) {
                userData.setRoleId(Integer.parseInt(roleId));
            }
            userData.setLogin(login);
            serviceProvider.getUserService().updateUser(userData);

            writer.println(SUCCESS_MSG_JSON);
        } catch (IOException e) {
            try {
                LOGGER.error("Error to get writer when try to Edit user info by admin", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                throw new ServletException(e);
            } catch (IOException ex) {
                LOGGER.error("Error to send error writer when try to Edit user info by admin", e);
                throw new ServletException(e);
            }
        } catch (ValidationException | NumberFormatException e) {
            response.setContentType(JSON_UTF8_TYPE);
            writer.println(String.format(ERROR_MSG_JSON, e.getMessage()));
        }

    }
}
