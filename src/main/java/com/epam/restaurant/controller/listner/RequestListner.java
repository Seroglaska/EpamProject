package com.epam.restaurant.controller.listner;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * The {@code RequestListner} class for receiving notification events about requests coming into and going out of scope
 * of a web application
 */
@WebListener
public class RequestListner implements ServletRequestListener {
    private static final String LAST_PAGE_ATTR = "lastPage";
    private static final String MAIN_PAGE_ADDR = "/home";
    private static final String NOT_LAST_PAGE_REGEX = "(/restaurant).*|(/images).*|(/css).*|(/js).*|(/ajaxController)";

    /**
     * ${@code requestDestroyed} is used to save the last request to display the page<br>
     * When the request is destroyed, if it was associated with the receiving of the page, then it is saved to the session
     * @param sre servlet request event
     * @see com.epam.restaurant.controller.command.impl.ChangeLocale
     */
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();

        String lastPage = (String) session.getAttribute(LAST_PAGE_ATTR);
        if (lastPage == null) {
            session.setAttribute(LAST_PAGE_ATTR, MAIN_PAGE_ADDR);
        } else {
            String uri = request.getRequestURI();
            if (!uri.matches(NOT_LAST_PAGE_REGEX)) {
                session.setAttribute(LAST_PAGE_ATTR, uri);
            }
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
    }
}
