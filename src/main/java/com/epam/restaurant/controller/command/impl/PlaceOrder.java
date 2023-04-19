package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.bean.AuthorizedUser;
import com.epam.restaurant.bean.Dish;
import com.epam.restaurant.bean.Order;
import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.service.OrderService;
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

public class PlaceOrder implements Command {
    private static final Logger LOGGER = LogManager.getLogger(PlaceOrder.class);
    private static final ServiceProvider serviceProvider = ServiceProvider.getInstance();

    private static final String ORDER_ATTR = "order";
    private static final String USER_ATTR = "user";
    private static final String QUANTITY_OF_DISHES_ATTR = "quantityOfDishes";
    private static final String PAYMENT_BY_PARAM = "paymentBy";
    private static final String RECEIVING_PARAM = "receiving";
    private static final int CARD_ONLINE_ID = 2;
    private static final String FINISHING_THE_ORDER_ADDR = "/finishingTheOrder";
    private static final String ONLINE_PAY_ADDR = "/onlinePay";
    private static final boolean IS_ONLINE_PAY = false;

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException {
        int paymentMethodId = Integer.parseInt(request.getParameter(PAYMENT_BY_PARAM));
        String receiving = request.getParameter(RECEIVING_PARAM);

        HttpSession session = request.getSession();
        session.setAttribute(PAYMENT_BY_PARAM, paymentMethodId);
        session.setAttribute(RECEIVING_PARAM, receiving);

        try {
            if (CARD_ONLINE_ID == paymentMethodId) {
                response.sendRedirect(ONLINE_PAY_ADDR);
            } else {
                serviceProvider.getTransaction().startTransaction();

                setInvoice(request, IS_ONLINE_PAY);

                serviceProvider.getTransaction().commit();

                session.removeAttribute(ORDER_ATTR);
                session.removeAttribute(PAYMENT_BY_PARAM);
                session.removeAttribute(RECEIVING_PARAM);
                session.removeAttribute(QUANTITY_OF_DISHES_ATTR);

                response.sendRedirect(FINISHING_THE_ORDER_ADDR);
            }
        } catch (ServiceException e) {
            serviceProvider.getTransaction().rollback();
            throw new ServiceException(e);
        } catch (IOException e) {
            LOGGER.error("Invalid address to forward or redirect in the PlaceOrder command..", e);
        }
    }

    public static int setInvoice(HttpServletRequest request, boolean isOnlinePay) throws ServiceException {
        HttpSession session = request.getSession();

        // create order
        AuthorizedUser user = (AuthorizedUser) session.getAttribute(USER_ATTR);
        Order order = (Order) session.getAttribute(ORDER_ATTR);

        OrderService orderService = serviceProvider.getOrderService();
        int orderId = orderService.createOder(order, user.getLogin());

        // create order detail
        String methodOfReceiving = (String) request.getSession().getAttribute(RECEIVING_PARAM);

        for (Dish dish : order.getOrderList().keySet()) {
            Integer quantity = order.getOrderList().get(dish);
            orderService.createOderDetail(orderId, dish.getId(), quantity, methodOfReceiving);
        }

        // create invoice
        PaymentService paymentService = serviceProvider.getPaymentService();
        int invoiceId = paymentService.createInvoice(orderId, isOnlinePay);

        return invoiceId;
    }
}
