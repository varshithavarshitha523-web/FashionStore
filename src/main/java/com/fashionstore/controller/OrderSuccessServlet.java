package com.fashionstore.controller;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import com.fashionstore.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/order-success")
public class OrderSuccessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            String orderIdParam = request.getParameter("orderId");

            if (orderIdParam == null || orderIdParam.isEmpty()) {
                response.sendRedirect("products");
                return;
            }

            int orderId = Integer.parseInt(orderIdParam);

            try (Connection conn = DBConnection.getConnection()) {

                String orderSql = "SELECT * FROM orders WHERE order_id=?";

                PreparedStatement orderPs = conn.prepareStatement(orderSql);
                orderPs.setInt(1, orderId);

                ResultSet orderRs = orderPs.executeQuery();

                if (!orderRs.next()) {
                    response.sendRedirect("products");
                    return;
                }

                request.setAttribute("orderId", orderRs.getInt("order_id"));
                request.setAttribute("orderDate", orderRs.getTimestamp("order_date"));
                request.setAttribute("orderStatus", orderRs.getString("order_status"));
                request.setAttribute("paymentMethod", orderRs.getString("payment_method"));
                request.setAttribute("totalAmount", orderRs.getBigDecimal("total_amount"));

                request.setAttribute("deliveryName", orderRs.getString("delivery_name"));
                request.setAttribute("deliveryPhone", orderRs.getString("delivery_phone"));

                String address =
                        orderRs.getString("delivery_address_line1") + ", " +
                        orderRs.getString("delivery_address_line2") + ", " +
                        orderRs.getString("delivery_city") + ", " +
                        orderRs.getString("delivery_state") + ", " +
                        orderRs.getString("delivery_country");

                request.setAttribute("deliveryAddress", address);

                String itemSql =
                        "SELECT p.product_name, v.size, oi.quantity, oi.price " +
                        "FROM order_items oi " +
                        "JOIN product_variants v ON oi.variant_id = v.variant_id " +
                        "JOIN products p ON v.product_id = p.product_id " +
                        "WHERE oi.order_id=?";

                PreparedStatement itemPs = conn.prepareStatement(itemSql);
                itemPs.setInt(1, orderId);

                ResultSet itemRs = itemPs.executeQuery();

                List<Map<String, Object>> orderItems = new ArrayList<>();

                while (itemRs.next()) {
                    Map<String, Object> item = new HashMap<>();

                    item.put("productName", itemRs.getString("product_name"));
                    item.put("size", itemRs.getString("size"));
                    item.put("quantity", itemRs.getInt("quantity"));
                    item.put("price", itemRs.getBigDecimal("price"));

                    orderItems.add(item);
                }

                request.setAttribute("orderItems", orderItems);
            }

            request.getRequestDispatcher("/WEB-INF/views/order-success.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("products");
        }
    }
}