package com.epam.restaurant.service.impl;

import com.epam.restaurant.bean.AuthorizedUser;
import com.epam.restaurant.bean.RegistrationUserData;
import com.epam.restaurant.bean.criteria.Criteria;
import com.epam.restaurant.dao.DAOException;
import com.epam.restaurant.dao.DAOProvider;
import com.epam.restaurant.dao.UserDAO;
import com.epam.restaurant.service.ServiceException;
import com.epam.restaurant.service.UserService;
import com.epam.restaurant.service.validation.UserValidator;
import com.epam.restaurant.service.validation.ValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;


public class UserImpl implements UserService {
    private static final Logger LOGGER = LogManager.getLogger(UserImpl.class);

    private static final DAOProvider daoProvider = DAOProvider.getInstance();
    private static final UserDAO userDAO = daoProvider.getUserDAO();

    @Override
    public AuthorizedUser signIn(String login, char[] password) throws ServiceException {
        if (login == null || password == null) {
            LOGGER.info("login or password is null in UserImpl.find()");
            return new AuthorizedUser();
        }

        try {
            AuthorizedUser user = userDAO.signIn(login, password);
            return user;
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean signUp(RegistrationUserData userData) throws ServiceException {
        UserValidator.validateUserData(userData);

        boolean registred = false;
        try {
            registred = userDAO.signUp(userData);
            if (!registred) {
                throw new ValidationException("User with this name already exists");
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        return registred;
    }

    @Override
    public List<RegistrationUserData> find(Criteria criteria) throws ServiceException {
        UserValidator.validate(criteria);

        try {
            return userDAO.find(criteria);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<RegistrationUserData> findByLoginPattern(String loginPattern) throws ServiceException {
        if (loginPattern == null) {
            return Collections.emptyList();
        }

        try {
            return userDAO.findByLoginPattern(loginPattern);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public RegistrationUserData updateUser(RegistrationUserData userData) throws ServiceException {
        UserValidator.validateUserData(userData);

        try {
            return userDAO.updateUser(userData);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }


}
