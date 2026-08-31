package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.CartItemDAO;
import com.fashionstore.model.CartItem;
import com.fashionstore.util.DBConnection;

public class CartItemDAOImpl implements CartItemDAO {

    // =========================
    // ADD TO CART (FIXED)
    // =========================
    @Override
    public boolean addToCart(int cartId, CartItem item) {

        try (Connection conn = DBConnection.getConnection()) {

            // 1. check if item already exists
            String checkSql =
                    "SELECT quantity FROM cart_items WHERE cart_id=? AND variant_id=?";

            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setInt(1, cartId);
            checkPs.setInt(2, item.getVariantId());

            ResultSet rs = checkPs.executeQuery();

            if (rs.next()) {

                // 2. update quantity if exists
                int existingQty = rs.getInt("quantity");

                String updateSql =
                        "UPDATE cart_items SET quantity=? WHERE cart_id=? AND variant_id=?";

                PreparedStatement updatePs = conn.prepareStatement(updateSql);

                updatePs.setInt(1, existingQty + item.getQuantity());
                updatePs.setInt(2, cartId);
                updatePs.setInt(3, item.getVariantId());

                return updatePs.executeUpdate() > 0;
            }

            // 3. insert new item
            String insertSql =
                    "INSERT INTO cart_items(cart_id, variant_id, quantity) VALUES(?,?,?)";

            PreparedStatement insertPs = conn.prepareStatement(insertSql);

            insertPs.setInt(1, cartId);
            insertPs.setInt(2, item.getVariantId());
            insertPs.setInt(3, item.getQuantity());

            return insertPs.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // UPDATE QUANTITY
    // =========================
    @Override
    public boolean updateQuantity(int cartId, int variantId, int quantity) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql =
                    "UPDATE cart_items SET quantity=? WHERE cart_id=? AND variant_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, variantId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // REMOVE ITEM
    // =========================
    @Override
    public boolean removeItem(int cartId, int variantId) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql =
                    "DELETE FROM cart_items WHERE cart_id=? AND variant_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, cartId);
            ps.setInt(2, variantId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // GET CART ITEMS (JOIN PRODUCT DETAILS)
    // =========================
    @Override
    public List<CartItem> getCartItems(int cartId) {

        List<CartItem> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            String sql =
                    "SELECT " +
                    "ci.variant_id, ci.quantity, " +
                    "p.product_name, p.price, v.size " +
                    "FROM cart_items ci " +
                    "JOIN product_variants v ON ci.variant_id = v.variant_id " +
                    "JOIN products p ON v.product_id = p.product_id " +
                    "WHERE ci.cart_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                CartItem item = new CartItem();

                item.setVariantId(rs.getInt("variant_id"));
                item.setQuantity(rs.getInt("quantity"));

                item.setProductName(rs.getString("product_name"));
                item.setPrice(rs.getDouble("price"));
                item.setSize(rs.getString("size"));

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // CLEAR CART
    // =========================
    @Override
    public boolean clearCart(int cartId) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql =
                    "DELETE FROM cart_items WHERE cart_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // =========================
    // CART TOTAL
    // =========================
    @Override
    public double getCartTotal(int cartId) {

        double total = 0;

        try (Connection conn = DBConnection.getConnection()) {

            String sql =
                    "SELECT p.price, ci.quantity " +
                    "FROM cart_items ci " +
                    "JOIN product_variants v ON ci.variant_id = v.variant_id " +
                    "JOIN products p ON v.product_id = p.product_id " +
                    "WHERE ci.cart_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                total += rs.getDouble("price") * rs.getInt("quantity");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    // =========================
    // EXISTS CHECK
    // =========================
    @Override
    public boolean exists(int cartId, int variantId) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql =
                    "SELECT 1 FROM cart_items WHERE cart_id=? AND variant_id=?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cartId);
            ps.setInt(2, variantId);

            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}