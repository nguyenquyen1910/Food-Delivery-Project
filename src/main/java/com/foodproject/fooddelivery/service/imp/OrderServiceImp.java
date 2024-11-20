package com.foodproject.fooddelivery.service.imp;

import com.foodproject.fooddelivery.dto.OrderDTO;
import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.payload.request.OrderInCartRequest;
import com.foodproject.fooddelivery.payload.request.OrderRequest;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface OrderServiceImp {
    ResponseData getAll();
    ResponseData getAllOrders(int userId);
    OrderDTO getOrderById(int orderId);
    ResponseData insertOrder(OrderRequest orderRequest);
    ResponseData checkOutInCart(OrderInCartRequest orderInCartRequest);
    boolean changStatusOrder(int orderId,int status);
}
