package com.ecommerce.service;

import com.ecommerce.entity.Cart;

public interface CartService {

	int updateProductQtyInCart(String token, Cart cart, int cartId);

	int addToCart(String token, Cart cart);

}
