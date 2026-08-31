package com.fashionstore.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class Cart {

    private int cartId;
    private int userId;
    private Timestamp createdAt;

    // =========================
    // SESSION CART ITEMS
    // key = variantId
    // value = quantity
    // =========================
    private Map<Integer, Integer> items = new HashMap<>();

    public Cart() {
    }

    public Cart(int cartId, int userId, Timestamp createdAt) {
        this.cartId = cartId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // =========================
    // CART OPERATIONS
    // =========================

    public void addItem(int variantId) {
        items.put(variantId, items.getOrDefault(variantId, 0) + 1);
    }

    public void removeItem(int variantId) {
        items.remove(variantId);
    }

    public void updateItem(int variantId, int qty) {
        if (qty <= 0) {
            items.remove(variantId);
        } else {
            items.put(variantId, qty);
        }
    }

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }

    // =========================
    // DB FIELDS GETTERS/SETTERS
    // =========================

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "cartId=" + cartId +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", items=" + items +
                '}';
    }
}