package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.OrderService;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * The ${@code ConfirmOrder} class provides order confirmation
 */
public class ConfirmOrder implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ConfirmOrder.class);
    private static final OrderService orderService = ServiceProvider.getInstance().getOrderService();

    private static final String CONFIRMED_ORDER_ID = "confirmedOrderID";
    private static final String ORDERS_FOR_CONFIRMATION_ATTR = "ordersForConfirmation";
    private static final String CONFIRMED_STATUS = "confirmed";
    private static final String CONFIRMATION_OF_ORDERS_ADDR = "/confirmationOfOrders";

    /**
     * Updates the order status in the database to "confirmed" and removes it from the list of orders for confirmation
     * in the session
     * @param request client request
     * @param response response to the client
     * @throws ServiceException if an error occurred at the service level
     * @throws ServletException if an error occurred during execute command
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        int orderIDforConfirming = Integer.parseInt(request.getParameter(CONFIRMED_ORDER_ID));

        orderService.updateOrderStatus(orderIDforConfirming, CONFIRMED_STATUS);

        removeOrder(request, orderIDforConfirming);

        try {
            response.sendRedirect(CONFIRMATION_OF_ORDERS_ADDR);
        } catch (IOException e) {
            LOGGER.error("Error invalid address to redirect", e);
            throw new ServletException(e);
        }
    }

    /**
     * Deletes an order from the list of orders for confirmation(in session)
     * @param request client request
     * @param orderIDforConfirming order id for confirming
     */
    private void removeOrder(HttpServletRequest request, int orderIDforConfirming) {
        Map<Order, RegistrationUserData> orders = (Map<Order, RegistrationUserData>) request.getSession().getAttribute(ORDERS_FOR_CONFIRMATION_ATTR);
        for (Order order : orders.keySet()) {
            if (order.getId() == orderIDforConfirming) {
                orders.remove(order);
                break;
            }        }
        request.getSession().setAttribute(ORDERS_FOR_CONFIRMATION_ATTR, orders);
    }
}
