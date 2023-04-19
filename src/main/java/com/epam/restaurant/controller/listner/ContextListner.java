package com.epam.restaurant.controller.listner;

import com.epam.restaurant.dao.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

/**
 * The {@code ContextListner} class is used for receiving notification events about ServletContext lifecycle changes.
 */
@WebListener
public class ContextListner implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(ContextListner.class);
    private final ConnectionPool connectionPool = ConnectionPool.getInstance();

    /**
     * Initialize the connection pool when the servlet context is created
     * @param sce servlet context event
       @see ConnectionPool
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            connectionPool.initConnectionPool();
        } catch (ClassNotFoundException | InterruptedException | SQLException e) {
            LOGGER.error("Connection pool initialization error...", e);
            throw new RuntimeException("Connection pool initialization error", e);
        }
    }

    /**
     * Destroy connection pool when the servlet context is destroyed
     * @param sce servlet context event
     * @see ConnectionPool
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            connectionPool.dispose();
        } catch (SQLException | InterruptedException e) {
            LOGGER.error(e);
        }
    }
}
