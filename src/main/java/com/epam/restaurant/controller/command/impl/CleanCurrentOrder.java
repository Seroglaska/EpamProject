package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The ${@code CleanCurrentOrder} class provides order clearing
 */
public class CleanCurrentOrder implements Command {
    private static final Logger LOGGER = LogManager.getLogger(CleanCurrentOrder.class);

    private static final String ORDER_ATTR = "order";
    private static final String QUANTITY_ATTR = "quantityOfDishes";
    private static final String CURRENT_ORDER_ADDR = "/showCurrentOrder";

    /**
     * Removes the order object and the quantity of dishes from the session
     * @param request client request
     * @param response response to the client
     * @throws ServiceException if an error occurred at the service level
     * @throws ServletException if an error occurred during execute command
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        request.getSession().removeAttribute(ORDER_ATTR);
        request.getSession().removeAttribute(QUANTITY_ATTR);

        try {
            response.sendRedirect(CURRENT_ORDER_ADDR);
        } catch (IOException e) {
            LOGGER.error("Error invalid address to redirect", e);
            throw new ServletException(e);
        }
    }
}
