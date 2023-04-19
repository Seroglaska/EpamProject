package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.OrderForCooking;
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
import java.util.ArrayList;
import java.util.List;

public class MoveToCookOrders implements Command {
    private static final Logger LOGGER = LogManager.getLogger(MoveToCookOrders.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String CONFIRMED_STATUS = "confirmed";
    private static final String ORDERS_FOR_COOKING_ATTR = "ordersForCooking";
    private static final String KITCHEN_ADDR = "/kitchen";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        OrderService orderService = serviceProvider.getOrderService();

        Criteria criteria = new Criteria();
        criteria.add(SearchCriteria.Orders.ORDER_STATUS.name(), CONFIRMED_STATUS);

        List<OrderForCooking> ordersForCooking = orderService.findOrdersWithDishInfo(criteria);

        List<OrderForCooking> groupedOrders = new ArrayList<>();
        long previousOderId = 0;
        StringBuilder dishiesName = new StringBuilder("");
        int j = -1;
        for (OrderForCooking order : ordersForCooking) {
            if (previousOderId != order.getOrderId()) {
                j++;

                dishiesName = new StringBuilder(order.getDishName()).append(" ( x").append(order.getQuantity()).append(")");

                order.setDishName(dishiesName.toString());
                groupedOrders.add(order);
                previousOderId = order.getOrderId();

            } else {
                dishiesName.append("<hr>").append(order.getDishName()).append(" ( x").append(order.getQuantity()).append(")");
                groupedOrders.get(j).setDishName(dishiesName.toString());
            }
        }

        request.getSession().setAttribute(ORDERS_FOR_COOKING_ATTR, groupedOrders);

        try {
            response.sendRedirect(KITCHEN_ADDR);
        } catch (IOException e) {
            LOGGER.error("Error invalid address to redirect", e);
        }
    }
}
