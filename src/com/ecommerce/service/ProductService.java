package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Product;

public interface ProductService {

	public int addProducts(Product product, String token);

	public List<Product> fetchAllProduct();

	public int updataStockOfProudct(String token, int productId, int productQty);
}
