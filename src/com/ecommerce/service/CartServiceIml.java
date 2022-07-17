package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.Order;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.model.CartModel;
import com.ecommerce.model.OrderModel;
import com.ecommerce.properites.MessageProperties;

public class CartServiceIml implements CartService {
	private Connection con;

	public CartServiceIml(Connection con) {
		this.con = con;
	}

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
//			ResultSet cartFromDb = findCartByProductId(cart.getProductId());
			
			
			boolean isLoggedIn = getUser.next();
			if (!isLoggedIn) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			boolean isProductPresent = product.next();
			if (!isProductPresent) {
				throw new EcommerceException(MessageProperties.PRODUCT_NOT_FOUND.getMessage());
			}
			state = con.prepareStatement(query);
			ResultSet cartdb = findCartByUserId(getUser.getInt(4));
			boolean check = cartdb.next() && cart.getProductId()==cartdb.getInt(3)  && cartdb.getInt(2)==getUser.getInt(4);
			if (check) {
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return addeToCart;
	}

	public int decreaseQtyInCart(String token, ResultSet getCart, Order order) {
		int reduceQty =0;
		ProductServiceImpl productService = new ProductServiceImpl(con);
		String query = "update cart set product_qty=?, total_price=? where cart_id=?";
		PreparedStatement st = null;
		try {
			ResultSet getProduct = productService.findProductById(getCart.getInt(5));
			st = con.prepareStatement(query);
			int qty = getCart.getInt(4)-order.getProductOty();
			int price = getCart.getInt(5)-order.getProductOty()*(getProduct.next()? getProduct.getInt(4):1);
			st.setInt(1, qty);
			st.setInt(2, getCart.getInt(1));
			st.setInt(3, price);
			reduceQty = st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reduceQty;
	}

	@Override
	public List<CartModel> getCartDetails(String token) {
		List<CartModel> cartList = new ArrayList<>();
		UserServiceImp userService = new UserServiceImp(con);
		ResultSet getUser = userService.findUserByEmail(token);
		ProductServiceImpl productService = new ProductServiceImpl(con);
		PreparedStatement st = null;
		CartModel cartModel = null;
		String query="select * from cart where user_id=?";
		try {
			boolean isLoggedIn = getUser.next();
			if (!isLoggedIn) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			st = con.prepareStatement(query);
			st.setInt(1, getUser.getInt(4));
			ResultSet cart = st.executeQuery();
			while(cart.next()) {
				cartModel = new CartModel();
				cartModel.setProductId(cart.getInt(3));
				ResultSet product = productService.findProductById(cart.getInt(3));
				if(product.next()) {
					cartModel.setProductName(product.getString(2));
					cartModel.setProductPrice(product.getInt(4));
				}
				cartModel.setProductQty(cart.getInt(4));
				cartModel.setTotalPrice(cart.getInt(5));
				cartList.add(cartModel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cartList;
	}
}
