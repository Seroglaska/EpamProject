package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.AuthorizedUser;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.ServiceProvider;
import com.epam.restaurant.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PrintUserRegistrData implements Command {
    private static final Logger LOGGER = LogManager.getLogger(PrintUserRegistrData.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();
    private static final String USER_ATTR = "user";
    private static final String USER_DATA_ATTR = "userData";
    private static final String PERSONAL_INFO_ADDR = "/personalInfo";
    private static final int FOUND_USER = 0;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        UserService userService = serviceProvider.getUserService();
        HttpSession session = request.getSession();

        AuthorizedUser user = (AuthorizedUser) session.getAttribute(USER_ATTR);

        Criteria criteria = new Criteria();
        criteria.add(SearchCriteria.Users.LOGIN.toString(), user.getLogin());

        List<RegistrationUserData> registrationUserData = userService.find(criteria);

        RegistrationUserData userData = registrationUserData.get(FOUND_USER);

        try {
            session.setAttribute(USER_DATA_ATTR, userData);
            request.getRequestDispatcher(PERSONAL_INFO_ADDR).forward(request, response);
        } catch (IOException e) {
            LOGGER.error("Error invalid address to forward during print user data",e);
        }
    }
}
