package com.fashionstore.dao;

import java.util.List;
import com.fashionstore.model.CartItem;

public interface CartItemDAO {

    // Add item to cart
    boolean addToCart(int cartId, CartItem item);

    // Update quantity (FIXED: variantId instead of productId)
    boolean updateQuantity(int cartId, int variantId, int quantity);

    // Remove item (FIXED)
    boolean removeItem(int cartId, int variantId);

    // Get all cart items
    List<CartItem> getCartItems(int cartId);

    // Clear cart
    boolean clearCart(int cartId);

    // Get total price
    double getCartTotal(int cartId);

    // Check if item exists (FIXED)
    boolean exists(int cartId, int variantId);
}