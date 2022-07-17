package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.spi.DirStateFactory.Result;

import com.ecommerce.entity.Cart;
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
	public int updateProductQtyInCart(String token, Cart cart, int cartId) {
		UserServiceImp userService = new UserServiceImp(con);
		ProductServiceImpl productService = new ProductServiceImpl(con);
		int updateQty = 0;
		PreparedStatement state = null;
		String query = "update cart set product_qty=? where cart_id=?";
//		String query1 = "UPDATE product SET product_name=?,product_description=?, product_price=? WHERE product_id = ?";
		try {
			ResultSet getUser = userService.findUserByEmail(token);
			ResultSet findCart = findCartById(cartId);
			boolean isLoggedIn = getUser.next();
			if (!isLoggedIn) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			if (!findCart.next()) {
				throw new EcommerceException(MessageProperties.CART_NOT_FOUND.getMessage());
			}
			state = con.prepareStatement(query);
			state.setInt(1, cart.getProductQty() + findCart.getInt(4));
			state.setInt(2, cartId);
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
		String query = "insert into cart(user_id, product_id, product_qty) values(?,?,?)";
		try {
			UserServiceImp userService = new UserServiceImp(con);
			ProductServiceImpl productService = new ProductServiceImpl(con);
			ResultSet getUser = userService.findUserByEmail(token);
			ResultSet product = productService.findProductById(cart.getProductId());
			boolean isLoggedIn = getUser.next();
			if (!isLoggedIn) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			boolean isProductPresent = product.next();
			if (!isProductPresent) {
				throw new EcommerceException(MessageProperties.PRODUCT_NOT_FOUND.getMessage());
			}
			boolean isProductPresentInCart = cart.getProductId() == product.getInt(1) ? true : false;
			state = con.prepareStatement(query);
			if (isProductPresentInCart) {
				ResultSet getCartByUser = findCartByUserId(getUser.getInt(4));
				if (getCartByUser.next()) {
					addeToCart = updateProductQtyInCart(token, cart, getCartByUser.getInt(1));
				}
			} else {
				state.setInt(1, getUser.getInt(4));
				state.setInt(2, cart.getProductId());
				state.setInt(3, cart.getProductQty());
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
}
