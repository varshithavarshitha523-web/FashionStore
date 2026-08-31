package com.fashionstore.dao;

import java.util.List;

import com.fashionstore.model.Order;
public interface OrderDAO{
	boolean placeOrder(Order order);
	Order getOrderById(int orderId);
	List<Order> getOrderbyUserId(int userId);
	List<Order> getAllOrders();
	boolean updateOrderStatus(int orderId,String orderStatus);
	boolean deleteOrder(int orderId);
}