package com.epam.restaurant.service;

import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.OrderForCooking;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;

import java.util.List;
import java.util.Map;

/**
 * The ${@code OrderService} interface provides methods for working with {@link Order}
 * @see Criteria
 */
public interface OrderService {
    /**
     * Creates an order and validates the input parameters
     * @param order user order
     * @param userLogin user login
     * @return id of the created order
     * @throws ServiceException if an error occurred in the method
     */
    int createOder(Order order, String userLogin) throws ServiceException;

    /**
     * Creates an order details and validates the input parameters
     * @param oderId order id
     * @param menuId dish id in the menu
     * @param quantity quantity of dish
     * @param methodOfReceiving order method of receiving
     * @return true on success or false on failure when creating an order
     * @throws ServiceException if an error occurred in the method
     */
    boolean createOderDetail(int oderId, int menuId, Integer quantity, String methodOfReceiving) throws ServiceException;

    /**
     * Returns the list of the user's dishes with the status "confirmed" or "cooked"
     * @param userId user id
     * @return the list of the user orders with the status "confirmed" or "cooked"
     * @throws ServiceException if an error occurred in the method
     */
    List<Order> getHistoryOfOrders(int userId) throws ServiceException;

    /**
     * Return list of orders with the status "in processing" and information about users
     * @param criteria which order should correspond to
     * @return list of orders with the status "in processing" and information about users
     * @throws ServiceException if an error occurred in the method
     */
    Map<Order, RegistrationUserData> findOrdersWithUsersInfo(Criteria criteria) throws ServiceException;

    /**
     * Updates the order status
     * @param orderID order ID
     * @param status order status(can be: "in processing", "confirmed", "cooked")
     * @return true on success or false on failure when updating the order status
     * @throws ServiceException if an error occurred in the method
     */
    boolean updateOrderStatus(int orderID, String status) throws ServiceException;

    /**
     * Returns a list of orders for cooking
     * @param criteria which order should correspond to
     * @return a list of orders for cooking
     * @throws ServiceException if an error occurred in the method
     */
    List<OrderForCooking> findOrdersWithDishInfo(Criteria criteria) throws ServiceException;

    /**
     * Returns a list of orders matching the criteria
     * @param orderCriteria order criteria
     * @return a list of orders matching the criteria
     * @throws ServiceException if an error occurred in the method
     */
    List<Order> find(Criteria orderCriteria) throws ServiceException;
}
