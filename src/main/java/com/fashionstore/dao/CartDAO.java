package com.fashionstore.dao;


import com.fashionstore.model.Cart;
public interface CartDAO
{
	boolean createCart(int userId);
	Cart getCartById(int cartId);
	Cart getCartByUserId(int userId);
	Cart getOrCreateCartByUserId(int userId);
	boolean deleteCart(int cartId);
	boolean cartExistsByUserId(int userId);
}