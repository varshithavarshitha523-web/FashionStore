package com.fashionstore.dao.impl;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fashionstore.dao.CartDAO;
import com.fashionstore.model.Cart;
import com.fashionstore.util.DBConnection;

public class CartDAOImpl implements CartDAO {

    private static final String INSERT_CART_SQL = """
        INSERT INTO cart (user_id)
        VALUES (?)
    """;

    private static final String GET_CART_BY_ID_SQL = """
        SELECT * FROM cart WHERE cart_id = ?
    """;

    private static final String GET_CART_BY_USER_ID_SQL = """
        SELECT * FROM cart WHERE user_id = ?
    """;

    private static final String DELETE_CART_SQL = """
        DELETE FROM cart WHERE cart_id = ?
    """;

    private static final String CHECK_CART_EXISTS_SQL = """
        SELECT 1 FROM cart WHERE user_id = ?
    """;

    // ✅ CREATE CART
    @Override
    public boolean createCart(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_CART_SQL)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ GET BY CART ID
    @Override
    public Cart getCartById(int cartId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_CART_BY_ID_SQL)) {

            ps.setInt(1, cartId);

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

    // ✅ GET BY USER ID
    @Override
    public Cart getCartByUserId(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_CART_BY_USER_ID_SQL)) {

            ps.setInt(1, userId);

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

    // ✅ GET OR CREATE CART
    @Override
    public Cart getOrCreateCartByUserId(int userId) {

        // 1. Try to get existing cart
        Cart cart = getCartByUserId(userId);

        if (cart != null) {
            return cart;
        }

        // 2. If not exists → create
        boolean created = createCart(userId);

        if (created) {
            return getCartByUserId(userId);
        }

        return null;
    }

    // ✅ DELETE CART
    @Override
    public boolean deleteCart(int cartId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_CART_SQL)) {

            ps.setInt(1, cartId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ CHECK EXISTS
    @Override
    public boolean cartExistsByUserId(int userId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_CART_EXISTS_SQL)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // if row exists → true
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ MAPPER
    private Cart map(ResultSet rs) throws SQLException {
        Cart cart = new Cart();

        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));
        cart.setCreatedAt(rs.getTimestamp("created_at"));

        return cart;
    }
}