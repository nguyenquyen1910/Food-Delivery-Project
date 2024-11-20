package com.foodproject.fooddelivery.payload.request;

public class CartRequest {
    private int userId;
    CartItemRequest cartItemRequest;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public CartItemRequest getCartItemRequest() {
        return cartItemRequest;
    }

    public void setCartItemRequest(CartItemRequest cartItemRequest) {
        this.cartItemRequest = cartItemRequest;
    }
}
