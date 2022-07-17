package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Order;
import com.ecommerce.model.OrderModel;

public interface OrderService {

	int placeOrder(String token, Order order);

	List<OrderModel> checkuserOrderHistory(int userId, String token);

}
