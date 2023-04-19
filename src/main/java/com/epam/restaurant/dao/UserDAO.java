package com.epam.restaurant.dao;

import com.epam.restaurant.bean.AuthorizedUser;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;

import java.util.List;

/**
 * The ${@code UserDAO} interface provides methods for working with user data in the database
 * @see AuthorizedUser
 * @see RegistrationUserData
 */
public interface UserDAO {
    /**
     * Provides user authorization in the application
     * @param login entered by user
     * @param password entered by user
     * @return information about the user that is required after authorization
     * @throws DAOException if an error occurred in the method
     */
    AuthorizedUser signIn(String login, char[] password) throws DAOException;

    /**
     * Provides user registration in the application by creating a new record in the database
     * @param userData information about the user that is required for registration
     * @return true if success or false if registration failure
     * @throws DAOException if an error occurred in the method
     */
    boolean signUp(RegistrationUserData userData) throws DAOException;

    /**
     * Finds user data matching the specified criteria in the database
     * @param criteria which user data should correspond to
     * @return a list of found data about the user matching the criteria
     * @throws DAOException if an error occurred in the method
     */
    List<RegistrationUserData> find(Criteria criteria) throws DAOException;

    /**
     * Finds user data matching the specified login pattern in the database
     * @param loginPattern template for login search
     * @return a list of found data about the user matching the login pattern
     * @throws DAOException if an error occurred in the method
     */
    List<RegistrationUserData> findByLoginPattern(String loginPattern) throws DAOException;

    /**
     * Updates user information in the database
     * @param userData user data that needs to be updated in the database
     * @return user data that has been stored in the database
     * @throws DAOException if an error occurred in the method
     */
    RegistrationUserData updateUser(RegistrationUserData userData) throws DAOException;
}
