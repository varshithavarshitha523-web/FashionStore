package com.fashionstore.controller;

import java.io.IOException;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.model.Cart;
import com.fashionstore.model.CartItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO =
            new CartDAOImpl();

    private CartItemDAO cartItemDAO =
            new CartItemDAOImpl();

    private Integer getLoggedInUserId(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null
                || session.getAttribute("userId") == null) {

            response.sendRedirect("login");
            return null;
        }

        return (Integer) session.getAttribute("userId");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId =
                getLoggedInUserId(request, response);

        if (userId == null) return;

        Cart cart =
                cartDAO.getOrCreateCartByUserId(userId);

        request.setAttribute(
                "cartItems",
                cartItemDAO.getCartItems(cart.getCartId()));

        request.setAttribute(
                "cartTotal",
                cartItemDAO.getCartTotal(cart.getCartId()));

        request.getRequestDispatcher(
                "/WEB-INF/views/cart.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        Integer userId =
                getLoggedInUserId(request, response);

        if (userId == null) return;

        String action =
                request.getParameter("action");

        Cart cart =
                cartDAO.getOrCreateCartByUserId(userId);

        if ("add".equals(action)) {

            String variantParam =
                    request.getParameter("variantId");

            String quantityParam =
                    request.getParameter("quantity");

            if (variantParam == null
                    || variantParam.trim().isEmpty()) {

                response.sendRedirect("products");
                return;
            }

            int variantId =
                    Integer.parseInt(variantParam);

            int quantity = 1;

            if (quantityParam != null
                    && !quantityParam.trim().isEmpty()) {

                quantity =
                        Integer.parseInt(quantityParam);
            }

            CartItem item =
                    new CartItem();

            item.setVariantId(variantId);
            item.setQuantity(quantity);

            cartItemDAO.addToCart(
                    cart.getCartId(),
                    item);
        }

        else if ("update".equals(action)) {

            String variantParam =
                    request.getParameter("variantId");

            String quantityParam =
                    request.getParameter("quantity");

            if (variantParam == null
                    || variantParam.trim().isEmpty()) {

                response.sendRedirect("cart");
                return;
            }

            int variantId =
                    Integer.parseInt(variantParam);

            int quantity = 1;

            if (quantityParam != null
                    && !quantityParam.trim().isEmpty()) {

                quantity =
                        Integer.parseInt(quantityParam);
            }

            if (quantity <= 0) {

                cartItemDAO.removeItem(
                        cart.getCartId(),
                        variantId);

            } else {

                cartItemDAO.updateQuantity(
                        cart.getCartId(),
                        variantId,
                        quantity);
            }
        }

        else if ("remove".equals(action)) {

            String variantParam =
                    request.getParameter("variantId");

            if (variantParam == null
                    || variantParam.trim().isEmpty()) {

                response.sendRedirect("cart");
                return;
            }

            int variantId =
                    Integer.parseInt(variantParam);

            cartItemDAO.removeItem(
                    cart.getCartId(),
                    variantId);
        }

        response.sendRedirect("cart");
    }
}