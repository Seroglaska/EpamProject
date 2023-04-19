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

public class MoveToAccount implements Command {
    private static final Logger LOGGER = LogManager.getLogger(MoveToAccount.class);

    private static final String SHOW_ACCOUNT_ADDR = "/showAccount";
    private static final String USER_DATA_ATTR = "userData";
    private static final String ORDERS_IN_PROCESSING_ATTR = "ordersInProcessing";
    private static final String ORDERS_HISTORY_ATTR = "ordersHistory";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        HttpSession session = request.getSession();
        session.removeAttribute(USER_DATA_ATTR);
        session.removeAttribute(ORDERS_IN_PROCESSING_ATTR);
        session.removeAttribute(ORDERS_HISTORY_ATTR);

        try {
            response.sendRedirect(SHOW_ACCOUNT_ADDR);
        } catch (IOException e) {
            LOGGER.error("Error invalid address to redirect", e);
        }
    }
}
