package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Order;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.properites.MessageProperties;

public class CartServiceIml implements CartService {
	private Connection con;

	public CartServiceIml(Connection con) {
		this.con = con;
	}

	UserServiceImp userService = new UserServiceImp(con);
	ProductServiceImpl productService = new ProductServiceImpl(con);

	public ResultSet findCartById(int cartId) {
		ResultSet getCart = null;
		String query = "select * from cart where cart_id=?";
		try {
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, cartId);
			getCart = st.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getCart;
	}
	
	public ResultSet findCartByProductId(int productId) {
		ResultSet getCart = null;
		String query ="select * from cart where product_id=?";
		try {
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, productId);
			getCart = st.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getCart;
	}

	public ResultSet findCartByUserId(int uesrId) {
		ResultSet getCart = null;
		String query = "select * from cart where user_id=?";
		try {
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, uesrId);
			getCart = st.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getCart;
	}

	@Override
	public int increaseOtyAndPriceInCartWhenProductIsAddedInCart(String token, Cart cart, int cartId) {
		UserServiceImp userService = new UserServiceImp(con);
		ProductServiceImpl productService = new ProductServiceImpl(con);
		int updateQty = 0;
		PreparedStatement state = null;
		String query = "update cart set product_qty=?, total_price=? where cart_id=?";
//		String query1 = "UPDATE product SET product_name=?,product_description=?, product_price=? WHERE product_id = ?";
		try {
			ResultSet getUser = userService.findUserByEmail(token);
			ResultSet findCart = findCartById(cartId);
			ResultSet product = productService.findProductById(cart.getProductId());
			boolean isLoggedIn = getUser.next();
			if (!isLoggedIn) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			if (!findCart.next()) {
				throw new EcommerceException(MessageProperties.CART_NOT_FOUND.getMessage());
			}
			state = con.prepareStatement(query);
			state.setInt(1, cart.getProductQty() + findCart.getInt(4));
			state.setInt(2, findCart.getInt(5)+ cart.getProductQty()* (product.next()?product.getInt(4):1));
			state.setInt(3, cartId);
			updateQty = state.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return updateQty;
	}

	
	@Override
	public int addToCart(String token, Cart cart) {
		int addeToCart = 0;
		PreparedStatement state = null;
		String query = "insert into cart(user_id, product_id, product_qty, total_price) values(?,?,?,?)";
		try {
			UserServiceImp userService = new UserServiceImp(con);
			ProductServiceImpl productService = new ProductServiceImpl(con);
			ResultSet getUser = userService.findUserByEmail(token);
			ResultSet product = productService.findProductById(cart.getProductId());
			ResultSet cartFromDb = findCartByProductId(cart.getProductId());
			boolean isLoggedIn = getUser.next();
			if (!isLoggedIn) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			boolean isProductPresent = product.next();
			if (!isProductPresent) {
				throw new EcommerceException(MessageProperties.PRODUCT_NOT_FOUND.getMessage());
			}
			state = con.prepareStatement(query);
			
			if (cartFromDb.next() && cartFromDb.getInt(2)==getUser.getInt(1) && cart.getProductId()==cartFromDb.getInt(3)) {
					ResultSet getCartByUser = findCartByUserId(getUser.getInt(4));
					if (getCartByUser.next()) {
						addeToCart = increaseOtyAndPriceInCartWhenProductIsAddedInCart(token, cart, getCartByUser.getInt(1));
				}
			} else {
				state.setInt(1, getUser.getInt(4));
				state.setInt(2, cart.getProductId());
				state.setInt(3, cart.getProductQty());
				int totalProcutPrice = cart.getProductQty()*product.getInt(4);
				state.setInt(4, totalProcutPrice);
				addeToCart = state.executeUpdate();
			}
			state.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addeToCart;
	}

	public int decreaseQtyInCart(String token, ResultSet getCart, Order order) {
		int reduceQty =0;
		String query = "update cart set product_qty=? where cart_id=?";
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(query);
			int qty = getCart.getInt(4)-order.getProductOty();
			st.setInt(1, qty);
			st.setInt(2, getCart.getInt(1));
			reduceQty = st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reduceQty;
	}
}
