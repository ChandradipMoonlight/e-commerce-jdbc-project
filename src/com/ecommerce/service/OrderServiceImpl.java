package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.entity.Order;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.model.OrderModel;
import com.ecommerce.properites.MessageProperties;

public class OrderServiceImpl implements OrderService {
	private Connection con;

	public OrderServiceImpl(Connection con) {
		super();
		this.con = con;
	}

	@Override
	public int placeOrder(String token, Order order) {
		UserServiceImp userService = new UserServiceImp(con);
		ProductServiceImpl productService = new ProductServiceImpl(con);
		CartServiceIml cartService = new CartServiceIml(con);
		int orderPlaced = 0;
		PreparedStatement st = null;
		String query = "Insert into order_detail(product_qty, order_price, user_id, product_id) values(?,?,?,?)";
		try {
			ResultSet getUser = userService.findUserByEmail(token);
			ResultSet getProduct = productService.findProductById(order.getProductId());
			ResultSet getCart = cartService.findCartByProductId(order.getProductId());
			boolean isUserPresent = getUser.next();
			if (!isUserPresent) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			boolean isProductPresentInCart = getCart.next();
			if (!isProductPresentInCart) {
				throw new EcommerceException(MessageProperties.SELECT_PRODUCT.getMessage());
			}
			st = con.prepareStatement(query);
			if (order.getProductOty() < getCart.getInt(4)) {
				st.setInt(1, order.getProductOty());
				cartService.decreaseQtyInCart(token, getCart, order);
			}
			int getProductPrice = getProduct.next() ? getProduct.getInt(4) : 0;
			st.setInt(2, order.getProductOty() * getProductPrice);
			st.setInt(3, getCart.getInt(2));
			st.setInt(4, getCart.getInt(3));
			orderPlaced = st.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return orderPlaced;
	}

	@Override
	public List<OrderModel> checkuserOrderHistory(int userId, String token) {
		List<OrderModel> orderList = new ArrayList<>();
		UserServiceImp userService = new UserServiceImp(con);
		ProductServiceImpl productSevice = new ProductServiceImpl(con);
		ResultSet getUser = userService.findUserByEmail(token);
		
		OrderModel order = null;
		PreparedStatement st = null;
		boolean isUserPresent;
		try {
			isUserPresent = getUser.next();
			if (!isUserPresent) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			boolean isAdmin = getUser.getBoolean(3);
			if (!isAdmin) {
				throw new EcommerceException(MessageProperties.GET_PERMISSION.getMessage(),
						EcommerceException.ExceptionType.UNAUTHORISED_USER);
			}
			
			String query = "select * from order_detail where user_id=?";
			st = con.prepareStatement(query);
			st.setInt(1, userId);
			ResultSet getOrders = st.executeQuery();
			while (getOrders.next()) {
				order = new OrderModel();
				order.setOrderId((getOrders.getInt(1)));;
				order.setProductOty(getOrders.getInt(2));
				order.setOrderPrice((getOrders.getInt(3)));

				ResultSet getProduct = productSevice.findProductById(getOrders.getInt(5));
				String productName = getProduct.next() ? getProduct.getString(2): null;
				order.setProductName(productName);
				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}

}
