package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Order;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class RemoveDishFromOrder implements Command {
    private static final Logger LOGGER = LogManager.getLogger(RemoveDishFromOrder.class);
    private static final String DISH_ID_PARAM = "dishId";
    private static final String ORDER_ATTR = "order";
    private static final String CURRENT_ORDER_ADDR = "/showCurrentOrder";
    private static final String QUANTITY_OF_DISHES_ATTR = "quantityOfDishes";

    private static final String EX1 = "Error invalid address to redirect";

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        int idToRemove = Integer.parseInt(request.getParameter(DISH_ID_PARAM));

        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute(ORDER_ATTR);

        Map<Dish, Integer> orderList = order.getOrderList();
        for (Dish dish : orderList.keySet()) {
            if (dish.getId() == idToRemove) {
                orderList.remove(dish);
                int quantity = (Integer) session.getAttribute(QUANTITY_OF_DISHES_ATTR);
                quantity--;
                session.setAttribute(QUANTITY_OF_DISHES_ATTR, quantity);
                break;
            }
        }

        if (orderList.size() == 0) {
            order = null;
            session.removeAttribute(QUANTITY_OF_DISHES_ATTR);
        }

        session.setAttribute(ORDER_ATTR, order);

        try {
            response.sendRedirect(CURRENT_ORDER_ADDR);
        } catch (IOException e) {
            LOGGER.error(EX1, e);
        }
    }
}
