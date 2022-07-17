package com.ecommerce.service;

import java.util.List;

import com.ecommerce.entity.Cart;
import com.ecommerce.model.CartModel;

public interface CartService {

	int increaseOtyAndPriceInCartWhenProductIsAddedInCart(String token, Cart cart, int cartId);

	int addToCart(String token, Cart cart);

	List<CartModel> getCartDetails(String token);

}
