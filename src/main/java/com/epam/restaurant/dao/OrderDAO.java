package com.epam.restaurant.dao;

import com.epam.restaurant.bean.Order;
import com.epam.restaurant.bean.OrderForCooking;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.service.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * The ${@code OrderDAO} interface provides methods for working with orders in database
 */
public interface OrderDAO {
    /**
     * Creates an order in the database
     * @param order user order
     * @param userId user ID
     * @return id of the created order
     * @throws DAOException if an error occurred in the method
     */
    int createOrder(Order order, int userId) throws DAOException;

    /**
     * Creates an order details in the database
     * @param oderId order id
     * @param menuId dish id in the menu
     * @param quantity quantity of dish
     * @param methodOfReceiving order method of receiving
     * @return true on success or false on failure when creating an order
     * @throws DAOException if an error occurred in the method
     */
    boolean createOrderDetails(int oderId, int menuId, Integer quantity, String methodOfReceiving) throws DAOException;

    /**
     * Returns the list of the user's dishes with the status "confirmed" or "cooked" from the database
     * @param userId user id
     * @return the list of the user orders with the status "confirmed" or "cooked"
     * @throws DAOException if an error occurred in the method
     */
    List<Order> getHistoryOfOrders(int userId) throws DAOException;

    /**
     * Return list of orders with the status "in processing" and information about users from the database
     * @param criteria which order should correspond to
     * @return list of orders with the status "in processing" and information about users
     * @throws DAOException if an error occurred in the method
     */
    Map<Order, RegistrationUserData> findOrdersWithUsersInfo(Criteria criteria) throws DAOException;

    /**
     * Updates the order status in the database
     * @param orderID order ID
     * @param status order status(can be: "in processing", "confirmed", "cooked")
     * @return true on success or false on failure when updating the order status
     * @throws DAOException if an error occurred in the method
     */
    boolean updateOrderStatus(int orderID, String status) throws DAOException;

    /**
     * Returns a list of orders for cooking from the database
     * @param criteria which order should correspond to
     * @return a list of orders for cooking
     * @throws DAOException if an error occurred in the method
     */
    List<OrderForCooking> findOrdersWithDishInfo(Criteria criteria) throws DAOException;

    /**
     * Returns a list of orders matching the criteria from the database
     * @param criteria order criteria
     * @return a list of orders matching the criteria
     * @throws ServiceException if an error occurred in the method
     */
    List<Order> find(Criteria criteria) throws DAOException;
}
