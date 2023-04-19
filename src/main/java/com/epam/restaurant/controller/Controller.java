package com.epam.restaurant.controller;

import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.controller.command.CommandProvider;
import com.epam.restaurant.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The {@code Controller} class is designed to process synchronous requests from the client and return results
 * @see CommandProvider
 */
public class Controller extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(Controller.class);
    private static final CommandProvider commandProvider = CommandProvider.getInstance();
    private static final String PARAMETER_COMMAND = "command";
    private static final String ERROR_PAGE_ADDR = "/errorPage?errorMsg=%s";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Command command = commandProvider.getCommand(req.getParameter(PARAMETER_COMMAND).toUpperCase());
        try {
            command.execute(req, resp);
        } catch (ServiceException | ServletException e) {
            LOGGER.error(e.getMessage(), e);

            int lastIndx = e.getMessage().lastIndexOf(":");
            if (lastIndx == -1) {
                String errorMsg = e.getMessage();
                resp.sendRedirect(String.format(ERROR_PAGE_ADDR, errorMsg));
            } else {
                String errorMsg = e.getMessage().substring(lastIndx);
                resp.sendRedirect(String.format(ERROR_PAGE_ADDR, errorMsg));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
