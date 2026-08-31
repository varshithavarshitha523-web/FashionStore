package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String email =
                trim(request.getParameter("email"));

        String password =
                trim(request.getParameter("password"));

        if (isEmpty(email) || isEmpty(password)) {

            request.setAttribute(
                    "errorMessage",
                    "Email and Password are required.");

            request.setAttribute("email", email);

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp")
                    .forward(request, response);

            return;
        }

        User user =
                userDAO.loginUser(email, password);

        if (user == null) {

            request.setAttribute(
                    "errorMessage",
                    "Invalid email or password.");

            request.setAttribute("email", email);

            request.getRequestDispatcher(
                    "/WEB-INF/views/login.jsp")
                    .forward(request, response);

            return;
        }

        // SESSION
        HttpSession session =
                request.getSession();

        session.setAttribute("loggedInUser", user);

        session.setAttribute(
                "userId",
                user.getUserId());

        session.setMaxInactiveInterval(30 * 60);

        response.sendRedirect(
                request.getContextPath() + "/home");
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
}