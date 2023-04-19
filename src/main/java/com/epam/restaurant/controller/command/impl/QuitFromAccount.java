package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class QuitFromAccount implements Command {
    private static final Logger LOGGER = LogManager.getLogger(QuitFromAccount.class);
    private static final String MAIN_PAGE_ADDR = "/home";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        request.getSession().invalidate();
        request.getSession(true);

        try {
            response.sendRedirect(MAIN_PAGE_ADDR);
        } catch (IOException e) {
            LOGGER.error("Invalid address to redirect in QuitFromAccount", e);
        }
    }
}
