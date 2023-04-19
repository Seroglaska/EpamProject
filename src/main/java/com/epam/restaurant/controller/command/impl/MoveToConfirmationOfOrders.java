package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
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

public class MoveToConfirmationOfOrders implements Command {
    private static final Logger LOGGER = LogManager.getLogger(MoveToConfirmationOfOrders.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String IN_PROCESSING = "in processing";
    private static final String ORDERS_FOR_CONFIRMATION_ATTR = "ordersForConfirmation";
    private static final String CONFIRMATION_OF_ORDERS_ADDR = "/confirmationOfOrders";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        OrderService orderService = serviceProvider.getOrderService();

        Criteria criteria = new Criteria();
        criteria.add(SearchCriteria.Orders.ORDER_STATUS.toString(), IN_PROCESSING);

        Map<Order, RegistrationUserData> orderUserDataMap = orderService.findOrdersWithUsersInfo(criteria);

        request.getSession().setAttribute(ORDERS_FOR_CONFIRMATION_ATTR, orderUserDataMap);

        try {
            response.sendRedirect(CONFIRMATION_OF_ORDERS_ADDR);
        } catch (IOException e) {
            LOGGER.error("Error invalid address to redirect", e);
        }
    }
}
