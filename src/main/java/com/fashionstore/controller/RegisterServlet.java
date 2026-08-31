package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.dao.UserDAO;
import com.fashionstore.dao.impl.UserDAOImpl;
import com.fashionstore.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    private final UserDAO userDAO = new UserDAOImpl();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = trim(request.getParameter("fullName"));
        String email = trim(request.getParameter("email"));
        String phone = trim(request.getParameter("phone"));
        String password = trim(request.getParameter("password"));
        String addressLine1 = trim(request.getParameter("addressLine1"));
        String addressLine2 = trim(request.getParameter("addressLine2"));
        String city = trim(request.getParameter("city"));
        String state = trim(request.getParameter("state"));
        String pincode = trim(request.getParameter("pincode"));
        String country = trim(request.getParameter("country"));

        if (isEmpty(fullName) || isEmpty(email) || isEmpty(phone) || isEmpty(password)
                || isEmpty(addressLine1) || isEmpty(city) || isEmpty(state)
                || isEmpty(pincode) || isEmpty(country)) {

            request.setAttribute("errorMessage", "Please fill all required fields.");
            setFormData(request, fullName, email, phone, addressLine1, addressLine2, city, state, pincode, country);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }

        if (userDAO.emailExists(email)) {
            request.setAttribute("errorMessage", "Email already registered.");
            setFormData(request, fullName, email, phone, addressLine1, addressLine2, city, state, pincode, country);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        if (userDAO.phoneExists(phone)) {
            request.setAttribute("errorMessage", "Phone number already registered.");
            setFormData(request, fullName, email, phone, addressLine1, addressLine2, city, state, pincode, country);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            return;
        }
        
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setAddressLine1(addressLine1);
        user.setAddressLine2(addressLine2);
        user.setCity(city);
        user.setState(state);
        user.setPinCode(pincode);
        user.setCountry(country);

        boolean registered = userDAO.registerUser(user);

        if (registered) {
            response.sendRedirect(request.getContextPath() + "/login?registered=true");
        } else {
            request.setAttribute("errorMessage", "Registration failed. Please try again.");
            setFormData(request, fullName, email, phone, addressLine1, addressLine2, city, state, pincode, country);
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        }
    }

    private void setFormData(HttpServletRequest request, String fullName, String email, String phone,
                             String addressLine1, String addressLine2, String city,
                             String state, String pincode, String country) {

        request.setAttribute("fullName", fullName);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("addressLine1", addressLine1);
        request.setAttribute("addressLine2", addressLine2);
        request.setAttribute("city", city);
        request.setAttribute("state", state);
        request.setAttribute("pincode", pincode);
        request.setAttribute("country", country);
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }
}