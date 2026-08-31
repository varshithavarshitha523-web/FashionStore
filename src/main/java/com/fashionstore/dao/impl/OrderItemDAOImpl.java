package com.fashionstore.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.OrderItemDAO;
import com.fashionstore.model.OrderItem;
import com.fashionstore.util.DBConnection;

public class OrderItemDAOImpl implements OrderItemDAO {

    private static final String INSERT_ORDER_ITEM_SQL = """
        INSERT INTO order_items (order_id, variant_id, quantity, price)
        VALUES (?, ?, ?, ?)
    """;

    private static final String GET_ORDER_ITEM_BY_ID_SQL = """
        SELECT * FROM order_items WHERE order_item_id = ?
    """;

    private static final String GET_ITEMS_BY_ORDER_ID_SQL = """
        SELECT * FROM order_items WHERE order_id = ?
    """;

    private static final String DELETE_ORDER_ITEM_SQL = """
        DELETE FROM order_items WHERE order_item_id = ?
    """;

    private static final String DELETE_ITEMS_BY_ORDER_ID_SQL = """
        DELETE FROM order_items WHERE order_id = ?
    """;

    // ✅ ADD SINGLE ITEM
    @Override
    public boolean addOrderItem(OrderItem item) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ORDER_ITEM_SQL)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getVariantId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getPrice());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ ADD MULTIPLE ITEMS (BATCH)
    @Override
    public boolean addOrderItems(List<OrderItem> items) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ORDER_ITEM_SQL)) {

            for (OrderItem item : items) {
                ps.setInt(1, item.getOrderId());
                ps.setInt(2, item.getVariantId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getPrice());
                ps.addBatch();
            }

            int[] result = ps.executeBatch();

            for (int r : result) {
                if (r == Statement.EXECUTE_FAILED) {
                    return false;
                }
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ GET BY ID
    @Override
    public OrderItem getOrderItemsById(int orderItemId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ORDER_ITEM_BY_ID_SQL)) {

            ps.setInt(1, orderItemId);

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

    // ✅ GET BY ORDER ID
    @Override
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ITEMS_BY_ORDER_ID_SQL)) {

            ps.setInt(1, orderId);

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

    // ✅ DELETE SINGLE ITEM
    @Override
    public boolean deleteOrderItem(int orderItemId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ORDER_ITEM_SQL)) {

            ps.setInt(1, orderItemId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ DELETE ALL ITEMS OF ORDER
    @Override
    public boolean deleteOrderItemsByOrderId(int orderId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_ITEMS_BY_ORDER_ID_SQL)) {

            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ MAPPER
    private OrderItem map(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();

        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setVariantId(rs.getInt("variant_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));

        return item;
    }
}