package com.foodproject.fooddelivery.payload.request;

import com.foodproject.fooddelivery.entity.Product;

import java.util.Date;
import java.util.List;

public class OrderRequest {
    private int userId;
    private OrderItemRequest orderItemRequest;
    private String shippingMethod;
    private String deliveryAddress;
    private String recipientName;
    private String recipientPhone;
    private String  expectedDeliveryDate;
    private String noteOrder;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public OrderItemRequest getOrderItemRequest() {
        return orderItemRequest;
    }

    public void setOrderItemRequest(OrderItemRequest orderItemRequest) {
        this.orderItemRequest = orderItemRequest;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public String getNoteOrder() {
        return noteOrder;
    }

    public void setNoteOrder(String noteOrder) {
        this.noteOrder = noteOrder;
    }
}
