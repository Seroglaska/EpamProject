package com.epam.restaurant.service.impl;

import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.OrderForCooking;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.bean.criteria.SearchCriteria;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.OrderDAO;
import com.epam.restaurant.dao.UserDAO;
import com.epam.restaurant.service.OrderService;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.validation.OrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class OrderImpl implements OrderService {
    private static final Logger LOGGER = LogManager.getLogger(OrderImpl.class);

    private static final DAOProvider daoProvider = DAOProvider.getInstance();
    private static final UserDAO userDAO = daoProvider.getUserDAO();
    private static final OrderDAO orderDAO = daoProvider.getOrderDAO();

    @Override
    public int createOder(Order order, String userLogin) throws ServiceException {
        OrderValidator.validate(order, userLogin);

        Criteria criteria = new Criteria();
        criteria.add(SearchCriteria.Users.LOGIN.toString(), userLogin);

        try {
            List<RegistrationUserData> authorizedUsers = userDAO.find(criteria);

            int orderId = orderDAO.createOrder(order, authorizedUsers.get(0).getId());

            return orderId;
        } catch (DAOException e) {

            throw new ServiceException(e);
        }
    }

    @Override
    public boolean createOderDetail(int oderId, int menuId, Integer quantity, String methodOfReceiving) throws ServiceException {
        OrderValidator.validate(oderId, menuId, quantity, methodOfReceiving);

        try {
            return orderDAO.createOrderDetails(oderId, menuId, quantity, methodOfReceiving);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> getHistoryOfOrders(int userId) throws ServiceException {
        OrderValidator.validate(userId);

        try {
            return orderDAO.getHistoryOfOrders(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Map<Order, RegistrationUserData> findOrdersWithUsersInfo(Criteria criteria) throws ServiceException {
        OrderValidator.validate(criteria);

        try {
            return orderDAO.findOrdersWithUsersInfo(criteria);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean updateOrderStatus(int orderID, String status) throws ServiceException {
        try {
            return orderDAO.updateOrderStatus(orderID, status);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<OrderForCooking> findOrdersWithDishInfo(Criteria criteria) throws ServiceException {
        OrderValidator.validate(criteria);

        try {
            return orderDAO.findOrdersWithDishInfo(criteria);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Order> find(Criteria orderCriteria) throws ServiceException {
        OrderValidator.validate(orderCriteria);

        try {
            return orderDAO.find(orderCriteria);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
