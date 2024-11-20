package com.foodproject.fooddelivery.service.imp;

import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.payload.request.CartRequest;


public interface CartServiceImp {
    ResponseData getAllCart(int userId);
    ResponseData insertCart(CartRequest cartRequest);
    ResponseData deleteCartItem(int cartItemId);
    ResponseData updateQuantityCartItem(int cartItemId,int quantity);
}
