package com.epam.restaurant.controller.command;

import com.epam.restaurant.service.ServiceException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The interface Command is implemented in every Command class.
 * @see com.epam.restaurant.controller.command.impl
 */
public interface Command {
    /**
     * Execute command and returns the result.
     * @param request client request
     * @param response response to the client
     * @throws ServiceException if an error occurred at the service level
     * @throws ServletException  if an error occurred in the command
     */
    void execute(HttpServletRequest request, HttpServletResponse response) throws ServiceException, ServletException;
}
