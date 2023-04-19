package com.epam.restaurant.service;

import com.epam.restaurant.bean.AuthorizedUser;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;

import java.util.List;

/**
 * The ${@code UserService} interface provides methods for working with user data
 * @see AuthorizedUser
 * @see RegistrationUserData
 */
public interface UserService {
    /**
     * Provides validation of user data and user authorization in the application
     * @param login entered by user
     * @param password entered by user
     * @return information about the user that is required after authorization
     * @throws ServiceException if an error occurred in the method
     */
    AuthorizedUser signIn(String login, char[] password) throws ServiceException;

    /**
     * Provides validation of user data and user registration in the application
     * @param userData information about the user that is required for registration
     * @return true if success or false if registration failure
     * @throws ServiceException if an error occurred in the method
     */
    boolean signUp(RegistrationUserData userData) throws ServiceException;

    /**
     * Validates criteria and finds user data matching the specified criteria
     * @param criteria which user data should correspond to
     * @return a list of found data about the user matching the criteria
     * @throws ServiceException if an error occurred in the method
     */
    List<RegistrationUserData> find(Criteria criteria) throws ServiceException;

    /**
     * Validates login pattern and finds user data matching the specified login pattern
     * @param loginPattern template for login search
     * @return a list of found data about the user matching the login pattern
     * @throws ServiceException if an error occurred in the method
     */
    List<RegistrationUserData> findByLoginPattern(String loginPattern) throws ServiceException;

    /**
     * Validates and updates user information in the database
     * @param userData user data that needs to be updated in the database
     * @return user data that has been stored in the database
     * @throws ServiceException if an error occurred in the method
     */
    RegistrationUserData updateUser(RegistrationUserData userData) throws ServiceException;
}
