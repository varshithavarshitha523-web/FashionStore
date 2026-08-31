package com.fashionstore.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.fashionstore.dao.ProductVariantDAO;
import com.fashionstore.model.ProductVariant;
import com.fashionstore.util.DBConnection;

public class ProductVariantDAOImpl implements ProductVariantDAO {

    // =========================
    // SQL QUERIES
    // =========================

    private static final String INSERT_SQL = """
        INSERT INTO product_variants (product_id, size, stock_quantity)
        VALUES (?, ?, ?)
    """;

    private static final String UPDATE_SQL = """
        UPDATE product_variants
        SET product_id=?, size=?, stock_quantity=?
        WHERE variant_id=?
    """;

    private static final String DELETE_SQL = """
        DELETE FROM product_variants WHERE variant_id=?
    """;

    private static final String GET_BY_ID_SQL = """
        SELECT * FROM product_variants WHERE variant_id=?
    """;

    private static final String GET_BY_PRODUCT_SIZE_SQL = """
        SELECT * FROM product_variants
        WHERE product_id=? AND size=?
    """;

    private static final String GET_BY_PRODUCT_SQL = """
        SELECT * FROM product_variants WHERE product_id=?
    """;

    private static final String GET_ALL_SQL = """
        SELECT * FROM product_variants
    """;

    private static final String UPDATE_STOCK_SQL = """
        UPDATE product_variants
        SET stock_quantity=?
        WHERE variant_id=?
    """;

    private static final String CHECK_STOCK_SQL = """
        SELECT stock_quantity FROM product_variants
        WHERE variant_id=?
    """;

    // =========================
    // CRUD METHODS
    // =========================

    @Override
    public boolean addProductVariant(ProductVariant v) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setInt(1, v.getProductId());
            ps.setString(2, v.getSize());
            ps.setInt(3, v.getStockQuantity());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateProductVariant(ProductVariant v) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_SQL)) {

            ps.setInt(1, v.getProductId());
            ps.setString(2, v.getSize());
            ps.setInt(3, v.getStockQuantity());
            ps.setInt(4, v.getVariantId());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteProductVariant(int variantId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setInt(1, variantId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public ProductVariant getVariantById(int variantId) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_ID_SQL)) {

            ps.setInt(1, variantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ProductVariant getVariantByProductIdAndSize(int productId, String size) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_PRODUCT_SIZE_SQL)) {

            ps.setInt(1, productId);
            ps.setString(2, size);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductVariant> getVariantByProductId(int productId) {
        List<ProductVariant> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_BY_PRODUCT_SQL)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<ProductVariant> getAllVariants() {
        List<ProductVariant> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =========================
    // STOCK METHODS
    // =========================

    @Override
    public boolean updateStock(int variantId, int stockQuantity) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STOCK_SQL)) {

            ps.setInt(1, stockQuantity);
            ps.setInt(2, variantId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isStockAvailable(int variantId, int requiredQuantity) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_STOCK_SQL)) {

            ps.setInt(1, variantId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int available = rs.getInt("stock_quantity");
                    return available >= requiredQuantity;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // =========================
    // COMMON MAPPER
    // =========================

    private ProductVariant map(ResultSet rs) throws Exception {
        ProductVariant v = new ProductVariant();

        v.setVariantId(rs.getInt("variant_id"));
        v.setProductId(rs.getInt("product_id"));
        v.setSize(rs.getString("size"));
        v.setStockQuantity(rs.getInt("stock_quantity"));

        return v;
    }
}