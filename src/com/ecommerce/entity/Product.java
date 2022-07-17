package com.ecommerce.entity;

public class Product {
	private int id;
	private String productName;
	private String productDecription;
	private int productPrice;
	private int productQty;
	
	public Product() {}
	
	public Product(String name, String desc, int price, int qty) {
		this.productName = name;
		this.productDecription = desc;
		this.productPrice = price;
		this.productQty = qty;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductDecription() {
		return productDecription;
	}
	public void setProductDecription(String productDecription) {
		this.productDecription = productDecription;
	}
	public int getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}
	public int getProductQty() {
		return productQty;
	}
	public void setProductQty(int productQty) {
		this.productQty = productQty;
	}
	
	
}
