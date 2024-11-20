package com.foodproject.fooddelivery.service;

import com.foodproject.fooddelivery.dto.OrderDTO;
import com.foodproject.fooddelivery.dto.OrderItemDTO;
import com.foodproject.fooddelivery.dto.ProductDTO;
import com.foodproject.fooddelivery.entity.*;
import com.foodproject.fooddelivery.entity.keys.KeyOrderItem;
import com.foodproject.fooddelivery.mapper.OrderMapper;
import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.payload.request.OrderInCartRequest;
import com.foodproject.fooddelivery.payload.request.OrderRequest;
import com.foodproject.fooddelivery.repository.*;
import com.foodproject.fooddelivery.service.imp.OrderServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements OrderServiceImp {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    private CartRepository cartRepository;

    @Override
    public ResponseData getAll() {
        ResponseData responseData = new ResponseData();
        List<Orders> ordersList = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Orders orders : ordersList) {
            OrderDTO orderDTO = orderMapper.toOrderDTO(orders);
            Users user = orders.getUsers();
            orderDTO.setUserId(orders.getUsers().getId());
            orderDTO.setUserName(user.getUserName());
            orderDTO.setUserFullName(user.getFullName());
            orderDTOs.add(orderDTO);
        }
        responseData.setSuccess(true);
        responseData.setData(orderDTOs);
        return responseData;
    }

    @Override
    public ResponseData getAllOrders(int userId) {
        ResponseData responseData = new ResponseData();
        Users user = usersRepository.findById(userId);
        if (user == null) {
            responseData.setSuccess(false);
            responseData.setDescription("User not found");
            return responseData;
        }
        List<Orders> ordersList = orderRepository.findByUsers(user);
        List<OrderDTO> orderDTOs = new ArrayList<>();

        for (Orders orders : ordersList) {
            OrderDTO orderDTO = orderMapper.toOrderDTO(orders);
            orderDTO.setUserId(orders.getUsers().getId());
            orderDTO.setUserName(user.getUserName());
            orderDTO.setUserFullName(user.getFullName());
            orderDTOs.add(orderDTO);
        }
        responseData.setSuccess(true);
        responseData.setData(orderDTOs);
        return responseData;
    }


    @Override
    public OrderDTO getOrderById(int orderId) {
        Optional<Orders> orders = orderRepository.findById(orderId);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orders.get().getId());
        orderDTO.setTotalPrice(orders.get().getPrice());
        orderDTO.setStatus(orders.get().getStatus());
        orderDTO.setCreateDate(orders.get().getCreateDate());
        orderDTO.setShippingMethod(orders.get().getShippingMethod());
        orderDTO.setDeliveryAddress(orders.get().getDeliveryAddress());
        orderDTO.setRecipientName(orders.get().getRecipientName());
        orderDTO.setRecipientPhone(orders.get().getRecipientPhone());
        orderDTO.setExpectedDeliveryDate(orders.get().getExpectedDeliveryDate());
        orderDTO.setNoteOrder(orders.get().getNoteOrder());
        orderDTO.setUserId(orders.get().getUsers().getId());
        orderDTO.setUserFullName(orders.get().getUsers().getFullName());
        orderDTO.setUserName(orders.get().getUsers().getUserName());
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        for (OrderItem orderItem : orders.get().getOrderItem()) {
            OrderItemDTO orderItemDTO = new OrderItemDTO();
            Product product = productRepository.findById(orderItem.getProduct().getId());
            KeyOrderItem keyOrderItem = new KeyOrderItem(orderItem.getOrders().getId(), product.getId());
            orderItemDTO.setKeyOrderItem(keyOrderItem);
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setTitle(product.getTitle());
            productDTO.setPrice(product.getPrice());
            productDTO.setImage(product.getImage());
            productDTO.setDescription(product.getDescription());
            productDTO.setStatus(product.getStatus());
            productDTO.setCategoryName(product.getCategory().getNameCate());
            orderItemDTO.setProductDTO(productDTO);
            orderItemDTO.setQuantity(orderItem.getQuantity());
            orderItemDTO.setPrice(orderItem.getPrice());
            orderItemDTO.setNote(orderItem.getNote());
            orderItemDTOS.add(orderItemDTO);
        }
        orderDTO.setOrderItems(orderItemDTOS);
        return orderDTO;
    }

    @Override
    @Transactional
    public ResponseData insertOrder(OrderRequest orderRequest) {
        ResponseData responseData = new ResponseData();
        Users user = usersRepository.findById(orderRequest.getUserId());
        if(user == null) {
            responseData.setSuccess(false);
            responseData.setDescription("User not found");
            return responseData;
        }

        Product product = productRepository.findById(orderRequest.getOrderItemRequest().getProductId());
        if(product == null) {
            responseData.setSuccess(false);
            responseData.setDescription("Product not found");
            return responseData;
        }

        Orders orders = new Orders();
        orders.setUsers(user);
        orders.setCreateDate(new Date());
        orders.setShippingMethod(orderRequest.getShippingMethod());
        orders.setDeliveryAddress(orderRequest.getDeliveryAddress());
        orders.setRecipientName(orderRequest.getRecipientName());
        orders.setRecipientPhone(orderRequest.getRecipientPhone());
        orders.setExpectedDeliveryDate(orderRequest.getExpectedDeliveryDate());
        orders.setNoteOrder(orderRequest.getNoteOrder());
        orders.setPrice(product.getPrice() * orderRequest.getOrderItemRequest().getQuantity());
        Orders savedOrder = orderRepository.save(orders);

        OrderItem orderItem = new OrderItem();
        KeyOrderItem keyOrderItem = new KeyOrderItem(savedOrder.getId(), product.getId());
        orderItem.setKeyOrderItem(keyOrderItem);
        orderItem.setOrders(savedOrder);
        orderItem.setProduct(product);
        orderItem.setQuantity(orderRequest.getOrderItemRequest().getQuantity());
        orderItem.setNote(orderRequest.getOrderItemRequest().getNote());
        orderItem.setCreateDate(new Date());
        orderItemRepository.save(orderItem);

        responseData.setSuccess(true);
        responseData.setDescription("Success");
        return responseData;
    }

    @Override
    @Transactional
    public ResponseData checkOutInCart(OrderInCartRequest orderInCartRequest) {
        ResponseData responseData = new ResponseData();
        Users user = usersRepository.findById(orderInCartRequest.getUserId());
        if(user==null){
            responseData.setSuccess(false);
            responseData.setDescription("User is null");
            return responseData;
        }
        Cart cart = cartRepository.findByUser(user);
        if(cart==null){
            cart = new Cart();
            cart.setStatus(1);
            cart.setUser(user);
            cart.setCreateDate(new Date());
            cart.setCartItems(new ArrayList<>());
            cart.setTotalPrice(0);
            cartRepository.save(cart);
        }
        if(cart.getCartItems().isEmpty()){
            responseData.setSuccess(false);
            responseData.setDescription("Cart is empty");
            return responseData;
        }
        Orders order = new Orders();
        order.setUsers(user);
        order.setStatus(0);
        order.setCreateDate(cart.getCreateDate());
        order.setShippingMethod(orderInCartRequest.getShippingMethod());
        order.setDeliveryAddress(orderInCartRequest.getDeliveryAddress());
        order.setRecipientName(orderInCartRequest.getRecipientName());
        order.setRecipientPhone(orderInCartRequest.getRecipientPhone());
        order.setExpectedDeliveryDate(orderInCartRequest.getExpectedDeliveryDate());
        order.setNoteOrder(orderInCartRequest.getNoteOrder());
        order.setPrice(cart.getTotalPrice());
        Orders orderSaved = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            KeyOrderItem keyOrderItem = new KeyOrderItem(orderSaved.getId(), cartItem.getProduct().getId());
            orderItem.setKeyOrderItem(keyOrderItem);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setNote(cartItem.getNote());
            orderItem.setCreateDate(new Date());
            orderItem.setOrders(orderSaved);
            orderItems.add(orderItem);
        }
        orderItemRepository.saveAll(orderItems);

        cart.getCartItems().clear();
        cart.setTotalPrice(0);
        cartRepository.save(cart);

        responseData.setSuccess(true);
        responseData.setDescription("Success");
        return responseData;
    }

    @Override
    public boolean changStatusOrder(int orderId,int status) {
        Optional<Orders> orders = orderRepository.findById(orderId);
        orders.get().setStatus(status);
        orderRepository.save(orders.get());
        return true;
    }

}
