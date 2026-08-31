package com.fashionstore.dao;
import java.util.List;
import com.fashionstore.model.OrderItem;


public interface OrderItemDAO {
	boolean addOrderItem(OrderItem orderItem);
	boolean addOrderItems(List<OrderItem> orderItems);
	OrderItem getOrderItemsById(int orderItemId);
	List<OrderItem> getOrderItemsByOrderId(int orderId);
	boolean deleteOrderItem(int orderItemId);
	boolean deleteOrderItemsByOrderId(int orderId);

}
