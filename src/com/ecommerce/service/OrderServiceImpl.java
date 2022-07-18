package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
		ResultSet getProduct = null;
		ResultSet cartByUserIdAndProdutId = null;
		int orderPlaced = 0;
		PreparedStatement st = null;
		String query = "Insert into order_detail(product_qty, order_price, user_id, product_id) values(?,?,?,?)";
		try {
			ResultSet getUser = userService.findUserByEmail(token);
			getProduct = productService.findProductById(order.getProductId());

			boolean isUserPresent = getUser.next();
			if (!isUserPresent) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			cartByUserIdAndProdutId = cartService.findCartByUserIdAndProductId(getUser.getInt(4), order.getProductId());
			boolean isProductPresentInCart = cartByUserIdAndProdutId.next();
			if (!isProductPresentInCart) {
				throw new EcommerceException(MessageProperties.SELECT_PRODUCT.getMessage());
			}
			st = con.prepareStatement(query);
			if (order.getProductOty() < cartByUserIdAndProdutId.getInt(4)) {
				st.setInt(1, order.getProductOty());
			}
			int getProductPrice = getProduct.next() ? getProduct.getInt(4) : 0;
			st.setInt(2, order.getProductOty() * getProductPrice);
			st.setInt(3, cartByUserIdAndProdutId.getInt(2));
			st.setInt(4, cartByUserIdAndProdutId.getInt(3));
			orderPlaced = st.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		if (orderPlaced == 1) {
			cartService.decreaseQtyInCart(token, cartByUserIdAndProdutId, order);
			System.out.println(MessageProperties.ORDER_PLACED.getMessage());
			System.out.println("Your order details are as follows !");
			try {
				PreparedStatement stat = con
						.prepareStatement("select * from order_detail order by order_id desc limit 1");
				ResultSet set = stat.executeQuery();
				System.out.println(
						"___________________________________________________________________________________________________________________________________");
				System.out.println("OrderId: ProductQty: OrderPrice: Product name");
				System.out.println(
						"----------------------------------------------------------------------------------------------------------------------");
				while (set.next()) {
					System.out.println(set.getInt(1) + "     : " + set.getInt(2) + "     :  " + set.getInt(3) + "  : "
							+ getProduct.getString(2));
				}
				System.out.println(
						"-----------------------------------------------------------------------------------------------------------------------------");
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
				order.setOrderId((getOrders.getInt(1)));
				;
				order.setProductOty(getOrders.getInt(2));
				order.setOrderPrice((getOrders.getInt(3)));

				ResultSet getProduct = productSevice.findProductById(getOrders.getInt(5));
				String productName = getProduct.next() ? getProduct.getString(2) : null;
				order.setProductName(productName);
				orderList.add(order);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderList;
	}

}
