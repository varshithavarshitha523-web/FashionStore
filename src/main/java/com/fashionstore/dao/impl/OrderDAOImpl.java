package com.fashionstore.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.OrderDAO;
import com.fashionstore.model.Order;
import com.fashionstore.util.DBConnection;

public class OrderDAOImpl implements OrderDAO {

    private static final String INSERT_ORDER_SQL = """
        INSERT INTO orders (
            user_id, total_amount, payment_method, order_status,
            delivery_name, delivery_phone, delivery_address_line1, delivery_address_line2,
            delivery_city, delivery_state, delivery_pincode, delivery_country
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String GET_ORDER_BY_ID_SQL = """
        SELECT * FROM orders WHERE order_id = ?
    """;

    private static final String GET_ORDERS_BY_USER_SQL = """
        SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC
    """;

    private static final String GET_ALL_ORDERS_SQL = """
        SELECT * FROM orders ORDER BY order_date DESC
    """;

    private static final String UPDATE_ORDER_STATUS_SQL = """
        UPDATE orders SET order_status = ? WHERE order_id = ?
    """;

    private static final String DELETE_ORDER_SQL = """
        DELETE FROM orders WHERE order_id = ?
    """;

    // ✅ PLACE ORDER
    @Override
    public boolean placeOrder(Order order) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ORDER_SQL)) {

            ps.setInt(1, order.getUserId());
            ps.setBigDecimal(2, order.getTotalAmount());
            ps.setString(3, order.getPaymentMethod());
            ps.setString(4, order.getOrderStatus());

            ps.setString(5, order.getDeliveryName());
            ps.setString(6, order.getDeliveryPhone());
            ps.setString(7, order.getDeliveryAddressLine1());
            ps.setString(8, order.getDeliveryAddressLine2());
            ps.setString(9, order.getDeliveryCity());
            ps.setString(10, order.getDeliveryState());
            ps.setString(11, order.getDeliveryPincode());
            ps.setString(12, order.getDeliveryCountry());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ GET ORDER BY ID
    @Override
    public Order getOrderById(int orderId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ORDER_BY_ID_SQL)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ GET ORDERS BY USER
    @Override
    public List<Order> getOrderbyUserId(int userId) {
        List<Order> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ORDERS_BY_USER_SQL)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ GET ALL ORDERS (ADMIN)
    @Override
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL_ORDERS_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ UPDATE STATUS
    @Override
    public boolean updateOrderStatus(int orderId, String orderStatus) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_ORDER_STATUS_SQL)) {

            ps.setString(1, orderStatus);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ DELETE ORDER
    @Override
    public boolean deleteOrder(int orderId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ORDER_SQL)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ MAPPER
    private Order map(ResultSet rs) throws SQLException {
        Order o = new Order();

        o.setOrderId(rs.getInt("order_id"));
        o.setUserId(rs.getInt("user_id"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setOrderStatus(rs.getString("order_status"));
        o.setOrderDate(rs.getTimestamp("order_date"));

        o.setDeliveryName(rs.getString("delivery_name"));
        o.setDeliveryPhone(rs.getString("delivery_phone"));
        o.setDeliveryAddressLine1(rs.getString("delivery_address_line1"));
        o.setDeliveryAddressLine2(rs.getString("delivery_address_line2"));
        o.setDeliveryCity(rs.getString("delivery_city"));
        o.setDeliveryState(rs.getString("delivery_state"));
        o.setDeliveryPincode(rs.getString("delivery_pincode"));
        o.setDeliveryCountry(rs.getString("delivery_country"));

        return o;
    }
}