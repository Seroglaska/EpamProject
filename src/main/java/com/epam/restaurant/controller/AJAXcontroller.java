package com.epam.restaurant.controller;

import com.epam.restaurant.controller.command.Command;
import com.epam.restaurant.controller.command.CommandProvider;
import com.epam.restaurant.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The {@code AJAXcontroller} class is designed to process asynchronous requests from the client and return results
 *
 * @see CommandProvider
 */
@WebServlet("/ajaxController")
public class AJAXcontroller extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AJAXcontroller.class);
    private static final CommandProvider commandProvider = CommandProvider.getInstance();
    private static final String PARAMETER_COMMAND = "command";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Command command = commandProvider.getCommand(req.getParameter(PARAMETER_COMMAND).toUpperCase());

        try {
            command.execute(req, resp);
        } catch (ServiceException | ServletException e) {
            LOGGER.error(e.getMessage(), e);

            int lastIndx = e.getMessage().lastIndexOf(":");
            if (lastIndx == -1) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            } else {
                String errorMsg = e.getMessage().substring(lastIndx);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }
}
