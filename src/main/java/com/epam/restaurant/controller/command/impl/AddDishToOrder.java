package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.MenuService;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.ServiceProvider;
import com.epam.restaurant.service.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * The ${@code AddDishToOrder} class provides adding a dish to an order
 */
public class AddDishToOrder implements Command {
    private static final Logger LOGGER = LogManager.getLogger(AddDishToOrder.class);
    private static final MenuService menuService =  ServiceProvider.getInstance().getMenuService();

    private static final String ORDER_ATTR = "order";
    private static final String QUANTITY_OF_DISHES_ATTR = "quantityOfDishes";
    private static final String DISH_ID_PARAM = "dish_id";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String JSON_UTF8_TYPE = "application/json; charset=UTF-8";
    private static final String ERROR_MSG_JSON = "{\"validationError\": \"true\", \"message\": \"%s\"}";
    private static final String SUCCESS_MSG_JSON = "{\"validationError\": false}";

    private static final int FOUND_DISH = 0;

    /**
     * Provides the adding of the specified number of dishes to the order, and puts it in the session
     * @param request client request
     * @param response response to the client
     * @throws ServiceException if an error occurred at the service level
     * @throws ServletException if an error occurred during execute command
     */
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute(ORDER_ATTR);

        order = checkOrderExist(session, order);

        PrintWriter writer = null;
        try {
            writer = response.getWriter();

            Criteria criteria = new Criteria();
            criteria.add(SearchCriteria.Dishes.DISHES_ID.toString(), request.getParameter(DISH_ID_PARAM));

            Dish dish = menuService.find(criteria).get(FOUND_DISH);
            Integer newQuantity = Integer.parseInt(request.getParameter(QUANTITY_PARAM));

            if (order.getOrderList().containsKey(dish)) {
                Integer currentQuantity = order.getOrderList().get(dish);
                currentQuantity += newQuantity;

                order.getOrderList().put(dish, currentQuantity);
            } else {
                order.getOrderList().put(dish, newQuantity);
            }

            setQuantityOfDishesToSession(order.getOrderList(), session);

            response.setContentType(JSON_UTF8_TYPE);
            writer.println(SUCCESS_MSG_JSON);
        } catch (IOException e) {
            try {
                LOGGER.error("Error to get writer when try to add dish to order", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
                throw new ServletException(e);
            } catch (IOException ex) {
                LOGGER.error("Error to send error writer when try to add dish to order", e);
                throw new ServletException(e);
            }
        } catch (ValidationException | NumberFormatException e) {
            response.setContentType(JSON_UTF8_TYPE);
            writer.println(String.format(ERROR_MSG_JSON, e.getMessage()));
        }
    }

    /**
     * Checks if the user had an order earlier and returns it, otherwise creates a new one
     * @param session current session between client and server
     * @param order the current customer order stored in the session
     * @return an existing client order in the session, otherwise creates a new one
     */
    private Order checkOrderExist(HttpSession session, Order order) {
        if (order == null) {
            order = new Order();
            session.setAttribute(ORDER_ATTR, order);

            Integer quantityOfDishes = 0;
            session.setAttribute(QUANTITY_OF_DISHES_ATTR, quantityOfDishes);
        }
        return order;
    }

    /**
     * Sets the number of dishes per session. It is necessary to display the number of dishes in the order on the
     * user's page
     * @param orderList contains a map of dishes and their quantities
     * @param session current session between client and server
     */
    private void setQuantityOfDishesToSession(Map<Dish, Integer> orderList, HttpSession session) {
        Integer count = 0;

        for (Integer quantity : orderList.values()) {
            count += quantity;
        }
        session.setAttribute(QUANTITY_OF_DISHES_ATTR, count);
    }
}
