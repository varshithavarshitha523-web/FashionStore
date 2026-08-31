package com.fashionstore.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.dao.impl.CartDAOImpl;
import com.fashionstore.dao.impl.CartItemDAOImpl;
import com.fashionstore.model.Cart;
import com.fashionstore.model.CartItem;
import com.fashionstore.model.User;
import com.fashionstore.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/place-order")
public class PlaceOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CartDAO cartDAO = new CartDAOImpl();
    private CartItemDAO cartItemDAO = new CartItemDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("loggedInUser");
        int userId = user.getUserId();

        try (Connection conn = DBConnection.getConnection()) {

            conn.setAutoCommit(false);

            Cart cart = cartDAO.getOrCreateCartByUserId(userId);

            List<CartItem> items = cartItemDAO.getCartItems(cart.getCartId());

            if (items == null || items.isEmpty()) {
                response.sendRedirect("cart");
                return;
            }

            double total = cartItemDAO.getCartTotal(cart.getCartId());

            String orderSql =
                    "INSERT INTO orders " +
                    "(user_id, total_amount, payment_method, order_status, " +
                    "delivery_name, delivery_phone, " +
                    "delivery_address_line1, delivery_address_line2, " +
                    "delivery_city, delivery_state, delivery_country) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement orderPs = conn.prepareStatement(
                    orderSql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            orderPs.setInt(1, userId);
            orderPs.setDouble(2, total);
            orderPs.setString(3, "COD");
            orderPs.setString(4, "PLACED");

            orderPs.setString(5, user.getFullName());
            orderPs.setString(6, user.getPhone());
            orderPs.setString(7, user.getAddressLine1());
            orderPs.setString(8, user.getAddressLine2());
            orderPs.setString(9, user.getCity());
            orderPs.setString(10, user.getState());
            orderPs.setString(11, user.getCountry());

            orderPs.executeUpdate();

            ResultSet rs = orderPs.getGeneratedKeys();

            int orderId = 0;

            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            if (orderId == 0) {
                conn.rollback();
                response.sendRedirect("checkout");
                return;
            }

            String itemSql =
                    "INSERT INTO order_items " +
                    "(order_id, variant_id, quantity, price) " +
                    "VALUES (?, ?, ?, ?)";

            PreparedStatement itemPs = conn.prepareStatement(itemSql);

            for (CartItem item : items) {
                itemPs.setInt(1, orderId);
                itemPs.setInt(2, item.getVariantId());
                itemPs.setInt(3, item.getQuantity());
                itemPs.setDouble(4, item.getPrice());
                itemPs.addBatch();
            }

            itemPs.executeBatch();

            cartItemDAO.clearCart(cart.getCartId());

            conn.commit();

            response.sendRedirect("order-success?orderId=" + orderId);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("checkout");
        }
    }
}