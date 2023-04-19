package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.AuthorizedUser;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.ServiceProvider;
import com.epam.restaurant.service.UserService;
import com.epam.restaurant.service.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;

public class Registration implements Command {
    private static final Logger LOGGER = LogManager.getLogger(Registration.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String LOGIN_PARAM = "login";
    private static final String PASSWORD_PARAM = "password";
    private static final String NAME_PARAM = "name";
    private static final String PHONE_NUMBER_PARAM = "phoneNumber";
    private static final String EMAIL_PARAM = "email";
    private static final int ROLE_ID = 2;

    private static final String USER_ATTR = "user";

    private static final String HOME_ADDRESS = "/home";
    private static final String REGISTR_PAGE_ADDRESS_WITH_MSG = "/registrationPage?invalidSignUp=true&validationMessage={0}";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        RegistrationUserData userData =
                new RegistrationUserData(
                        request.getParameter(LOGIN_PARAM),
                        request.getParameter(PASSWORD_PARAM).toCharArray(),
                        request.getParameter(NAME_PARAM),
                        request.getParameter(PHONE_NUMBER_PARAM),
                        request.getParameter(EMAIL_PARAM),
                        ROLE_ID);

        UserService userService = serviceProvider.getUserService();

        try {
            userService.signUp(userData);

            request.getSession().setAttribute(USER_ATTR, new AuthorizedUser(userData.getLogin(), userData.getName(), userData.getRoleId()));

            response.sendRedirect(HOME_ADDRESS);
        } catch (ValidationException e) {
            try {
                request.getRequestDispatcher(MessageFormat.format(REGISTR_PAGE_ADDRESS_WITH_MSG, e.getMessage())).forward(request, response);
            } catch (ServletException ex) {
                LOGGER.error("Error to forward in the registration command..", ex);
            } catch (IOException ex) {
                LOGGER.error("Invalid address in getRequestDispatcher() in the registration command..", ex);
            }

        } catch (IOException e) {
            LOGGER.error("Invalid address in getRequestDispatcher() in the registration command..", e);
        }
    }
}
