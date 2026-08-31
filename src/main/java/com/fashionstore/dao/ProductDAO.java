package com.fashionstore.dao;

import java.math.BigDecimal;
import java.util.List;

import com.fashionstore.model.Product;

public interface ProductDAO {

    // =====================
    // CRUD
    // =====================
    boolean addProduct(Product product);
    boolean updateProduct(Product product);
    boolean deleteProduct(int productId);

    // =====================
    // READ
    // =====================
    Product getProductById(int productId);
    List<Product> getAllProducts();
    List<Product> getAllActiveProducts();

    // =====================
    // FILTERS
    // =====================
    List<Product> getProductsByCategory(int categoryId);

    List<Product> searchProducts(String keyword); // ✔ KEEP ONLY THIS

    List<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> getProductsSortedByPriceAsc();
    List<Product> getProductsSortedByPriceDesc();

    List<Product> getLatestProducts(int limit);

    List<Product> getFilteredProducts(Integer categoryId,
                                      String keywords,
                                      BigDecimal minPrice,
                                      BigDecimal maxPrice);

    List<Product> getRelatedProducts(int categoryId,
                                     int excludeProductId,
                                     int limit);
}