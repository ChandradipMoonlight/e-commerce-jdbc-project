package com.ecommerce.model;


public class OrderModel {
	private int orderId;
	private int userId;
	private String productName;
	private int productOty;
	private int orderPrice;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getProductOty() {
		return productOty;
	}
	public void setProductOty(int productOty) {
		this.productOty = productOty;
	}
	public int getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(int orderPrice) {
		this.orderPrice = orderPrice;
	}

}
