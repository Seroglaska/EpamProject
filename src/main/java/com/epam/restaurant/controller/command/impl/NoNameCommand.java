package com.epam.restaurant.controller.command.impl;

import com.epam.restaurant.controller.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NoNameCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(NoNameCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/errorPage");
            requestDispatcher.forward(request, response);
        } catch (IOException e) {
            LOGGER.error("Invalid address getRequestDispatcher(/errorPage) in a noname command..", e);
        } catch (ServletException e) {
            LOGGER.error("Error to forward in a noname command..", e);
        }
    }
}
