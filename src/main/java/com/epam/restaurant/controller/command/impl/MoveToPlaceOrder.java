package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.PaymentMethod;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.PaymentService;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoveToPlaceOrder implements Command {
    private static final Logger LOGGER = LogManager.getLogger(MoveToPlaceOrder.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String DISH_ID_PARAM = "dishId";
    private static final String QUANTITY_PARAM = "quantity";
    private static final String ORDER_ATTR = "order";
    private static final String PAYMENT_METHODS_ATTR = "paymentMethods";
    private static final String PLACE_ODER_ADDR = "/showPlaceOrder";
    private static final String COMMAND_NAME = MoveToPlaceOrder.class.getSimpleName();

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        calculateTotalPrice(request);

        setPaymentMethodsToSession(request);

        try {
            response.sendRedirect(PLACE_ODER_ADDR);
        } catch (IOException e) {
            LOGGER.error(MessageFormat.format("Invalid address - {0}: getRequestDispatcher({0}) in the {1} command..", PLACE_ODER_ADDR, COMMAND_NAME));
        }
    }

    private void setPaymentMethodsToSession(HttpServletRequest request) throws ServiceException {
        HttpSession session = request.getSession();
        List<PaymentMethod> paymentMethods = (List<PaymentMethod>) session.getAttribute(PAYMENT_METHODS_ATTR);
        if (paymentMethods == null) {
            PaymentService paymentService = serviceProvider.getPaymentService();
            paymentMethods = paymentService.getPaymentMethods();

            session.setAttribute(PAYMENT_METHODS_ATTR, paymentMethods);
        }
    }

    private void calculateTotalPrice(HttpServletRequest request) {
        String[] strIds = request.getParameterValues(DISH_ID_PARAM);
        String[] strQuantities = request.getParameterValues(QUANTITY_PARAM);

        Map<Integer, Integer> idQuantity = new HashMap<>();
        for (int i = 0; i < strIds.length; i++) {
            idQuantity.put(
                    Integer.parseInt(strIds[i]),
                    Integer.parseInt(strQuantities[i]));
        }

        HttpSession session = request.getSession();
        Order order = (Order) session.getAttribute(ORDER_ATTR);

        Map<Dish, Integer> orderList = order.getOrderList();
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        for (Dish dish : orderList.keySet()) {
            for (Integer id : idQuantity.keySet()) {
                if (dish.getId() == id) {
                    orderList.put(dish, idQuantity.get(id));
                    totalPrice = totalPrice.add(
                            dish.getPrice().multiply(BigDecimal.valueOf(idQuantity.get(id))));
                }
            }
        }
        order.setTotalPrice(totalPrice);
        session.setAttribute(ORDER_ATTR, order);
    }
}
