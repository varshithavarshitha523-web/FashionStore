package com.fashionstore.dao.impl;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.ProductDAO;
import com.fashionstore.model.Product;
import com.fashionstore.util.DBConnection;

public class ProductDAOImpl implements ProductDAO {

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();

        p.setProductId(rs.getInt("product_id"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setProductName(rs.getString("product_name"));
        p.setBrand(rs.getString("brand"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setImageUrl(rs.getString("image_url"));
        p.setActive(rs.getBoolean("is_active"));

        return p;
    }

    @Override
    public boolean addProduct(Product p) {
        String sql = """
            INSERT INTO products
            (category_id, product_name, brand, description, price, image_url, is_active)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getProductName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setBigDecimal(5, p.getPrice());
            ps.setString(6, p.getImageUrl());
            ps.setBoolean(7, p.isActive());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean updateProduct(Product p) {
        String sql = """
            UPDATE products
            SET category_id=?, product_name=?, brand=?, description=?, price=?, image_url=?, is_active=?
            WHERE product_id=?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getCategoryId());
            ps.setString(2, p.getProductName());
            ps.setString(3, p.getBrand());
            ps.setString(4, p.getDescription());
            ps.setBigDecimal(5, p.getPrice());
            ps.setString(6, p.getImageUrl());
            ps.setBoolean(7, p.isActive());
            ps.setInt(8, p.getProductId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean deleteProduct(int productId) {
        String sql = "UPDATE products SET is_active = false WHERE product_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Product getProductById(int productId) {
        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            WHERE p.product_id = ?
            AND p.is_active = true
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapProduct(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            ORDER BY p.product_id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> getAllActiveProducts() {
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            WHERE p.is_active = true
            ORDER BY p.product_id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            WHERE p.category_id = ?
            AND p.is_active = true
            ORDER BY p.product_id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            JOIN categories c
            ON p.category_id = c.category_id
            WHERE p.is_active = true
            AND (
                LOWER(p.product_name) LIKE LOWER(?)
                OR LOWER(p.brand) LIKE LOWER(?)
                OR LOWER(p.description) LIKE LOWER(?)
                OR LOWER(c.category_name) LIKE LOWER(?)
            )
            ORDER BY p.product_id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = "%" + keyword.trim() + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);
            ps.setString(4, search);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            WHERE p.is_active = true
            AND p.price BETWEEN ? AND ?
            ORDER BY p.product_id DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, minPrice);
            ps.setBigDecimal(2, maxPrice);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> getProductsSortedByPriceAsc() {
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            WHERE p.is_active = true
            ORDER BY p.price ASC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> getProductsSortedByPriceDesc() {
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            WHERE p.is_active = true
            ORDER BY p.price DESC
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapProduct(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> getLatestProducts(int limit) {
        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            WHERE p.is_active = true
            ORDER BY p.product_id DESC
            LIMIT ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> getFilteredProducts(Integer categoryId,
                                             String keywords,
                                             BigDecimal minPrice,
                                             BigDecimal maxPrice) {

        List<Product> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT DISTINCT p.*
            FROM products p
            JOIN categories c
            ON p.category_id = c.category_id
            WHERE p.is_active = true
        """);

        List<Object> params = new ArrayList<>();

        if (categoryId != null) {
            sql.append(" AND p.category_id = ? ");
            params.add(categoryId);
        }

        if (keywords != null && !keywords.trim().isEmpty()) {
            sql.append("""
                AND (
                    LOWER(p.product_name) LIKE LOWER(?)
                    OR LOWER(p.brand) LIKE LOWER(?)
                    OR LOWER(p.description) LIKE LOWER(?)
                    OR LOWER(c.category_name) LIKE LOWER(?)
                )
            """);

            String search = "%" + keywords.trim() + "%";

            params.add(search);
            params.add(search);
            params.add(search);
            params.add(search);
        }

        if (minPrice != null) {
            sql.append(" AND p.price >= ? ");
            params.add(minPrice);
        }

        if (maxPrice != null) {
            sql.append(" AND p.price <= ? ");
            params.add(maxPrice);
        }

        sql.append(" ORDER BY p.product_id DESC ");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Product> getRelatedProducts(int categoryId,
                                            int excludeProductId,
                                            int limit) {

        List<Product> list = new ArrayList<>();

        String sql = """
            SELECT DISTINCT p.*
            FROM products p
            WHERE p.category_id = ?
            AND p.product_id <> ?
            AND p.is_active = true
            ORDER BY p.product_id DESC
            LIMIT ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, categoryId);
            ps.setInt(2, excludeProductId);
            ps.setInt(3, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProduct(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}