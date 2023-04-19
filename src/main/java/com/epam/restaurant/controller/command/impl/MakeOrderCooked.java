package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.OrderForCooking;
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
import java.util.List;

public class MakeOrderCooked implements Command {
    private static final Logger LOGGER = LogManager.getLogger(MakeOrderCooked.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String COOKED_ORDER_ID_PARAM = "cookedOrderId";
    private static final String ORDERS_FOR_COOKING_ATTR = "ordersForCooking";
    private static final String KITCHEN_ADDR = "/kitchen";
    private static final String COOKED_STATUS = "cooked";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        int cookedOrderId = Integer.parseInt(request.getParameter(COOKED_ORDER_ID_PARAM));

        OrderService orderService = serviceProvider.getOrderService();
        orderService.updateOrderStatus(cookedOrderId, COOKED_STATUS);

        List<OrderForCooking> ordersForCooking = (List<OrderForCooking>) request.getSession().getAttribute(ORDERS_FOR_COOKING_ATTR);
        for (OrderForCooking order : ordersForCooking) {
            if (order.getOrderId() == cookedOrderId) {
                ordersForCooking.remove(order);
                break;
            }
        }

        try {
            response.sendRedirect(KITCHEN_ADDR);
        } catch (IOException e) {
            LOGGER.error("Error invalid address to redirect", e);
        }
    }
}
