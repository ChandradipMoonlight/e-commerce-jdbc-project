package com.ecommerce.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.ecommerce.connection.DB;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.EcommerceException;
import com.ecommerce.properites.MessageProperties;

public class ProductServiceImpl implements ProductService {
	private Connection con;

	public ProductServiceImpl(Connection con) {
		this.con = con;
	}

	public ResultSet findProductById(int productId) {
		PreparedStatement st =null;
		ResultSet getProduct = null;
		try {
			st = con.prepareStatement("select * from product where product_id=?");
			st.setInt(1, productId);
			getProduct = st.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return getProduct;
	}

	@Override
	public int addProducts(Product product, String token) {
		int count = 0;
		String query = "Insert into product(product_name, product_description,product_price, product_qty) values(?,?,?,?)";
		PreparedStatement pst = null;
		UserServiceImp userService = new UserServiceImp(con);
		try {
			ResultSet getUser = userService.findUserByEmail(token);
			boolean isLoggedIn = getUser.next();
			if (!isLoggedIn) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			boolean isAdmin = getUser.getBoolean(3);
			if (!isAdmin) {
				throw new EcommerceException(MessageProperties.GET_PERMISSION.getMessage(),
						EcommerceException.ExceptionType.UNAUTHORISED_USER);
			}
			pst = con.prepareStatement(query);
			pst.setString(1, product.getProductName());
			pst.setString(2, product.getProductDecription());
			pst.setInt(3, product.getProductPrice());
			pst.setInt(4, product.getProductQty());
			count = pst.executeUpdate();
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<Product> fetchAllProduct() {
		List<Product> productList = new ArrayList<>();
		PreparedStatement pst = null;
		Product product = null;
		try {
			pst = con.prepareStatement("select * from product");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				product = new Product();
				product.setId(rs.getInt(1));
				product.setProductName(rs.getString(2));
				product.setProductDecription(rs.getString(3));
				product.setProductPrice(rs.getInt(4));
				product.setProductQty(rs.getInt(5));
				productList.add(product);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return productList;
	}

	@Override
	public int updataStockOfProudct(String token, int productId, int productQty) {
		UserServiceImp userService = new UserServiceImp(con);
		ProductServiceImpl productService = new ProductServiceImpl(con);
		int updatedStock =0;
		PreparedStatement stat = null;
		ResultSet product = null;
		ResultSet user = userService.findUserByEmail(token);
		String query="update product set product_qty=? where product_id=?";
		try {
			if(!user.next()) {
				throw new EcommerceException(MessageProperties.PLEASE_LOGIN.getMessage());
			}
			boolean isAdmin = user.getBoolean(3);
			if(!isAdmin) {
				throw new EcommerceException(MessageProperties.GET_PERMISSION.getMessage());
			}
			product = productService.findProductById(productId);
			if(!product.next()) {
				throw new EcommerceException(MessageProperties.PRODUCT_NOT_FOUND.getMessage());
			}
			stat = con.prepareStatement(query);
			stat.setInt(1, productQty);
			stat.setInt(2, productId);
			updatedStock=stat.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return updatedStock;
	}
}
