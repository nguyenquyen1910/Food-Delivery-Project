package com.foodproject.fooddelivery.service;

import com.foodproject.fooddelivery.dto.CartDTO;
import com.foodproject.fooddelivery.dto.CartItemDTO;
import com.foodproject.fooddelivery.dto.ProductDTO;
import com.foodproject.fooddelivery.entity.Cart;
import com.foodproject.fooddelivery.entity.CartItem;
import com.foodproject.fooddelivery.entity.Product;
import com.foodproject.fooddelivery.entity.Users;
import com.foodproject.fooddelivery.mapper.ProductMapper;
import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.payload.request.CartItemRequest;
import com.foodproject.fooddelivery.payload.request.CartRequest;
import com.foodproject.fooddelivery.repository.CartItemRepository;
import com.foodproject.fooddelivery.repository.CartRepository;
import com.foodproject.fooddelivery.repository.ProductRepository;
import com.foodproject.fooddelivery.repository.UsersRepository;
import com.foodproject.fooddelivery.service.imp.CartServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartService implements CartServiceImp {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartItemRepository cartItemRepository;


    @Override
    public ResponseData getAllCart(int userId) {
        ResponseData responseData = new ResponseData();
        Users user = usersRepository.findById(userId);
        if(user==null){
            responseData.setSuccess(false);
            responseData.setDescription("User not found");
            return responseData;
        }
        Cart cart=cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setStatus(1);
            cart.setUser(user);
            cart.setCreateDate(new Date());
            cart.setCartItems(new ArrayList<>());
            cartRepository.save(cart);
        }
        CartDTO cartDTO = new CartDTO();
        cartDTO.setCartId(cart.getId());
        cartDTO.setStatus(cart.getStatus());
        cartDTO.setCreateDate(cart.getCreateDate());
        int totalPrice=0;
        List<CartItemDTO> cartItemDTOS=new ArrayList<>();
        if (cart.getCartItems() != null) {
            for (CartItem cartItem : cart.getCartItems()) {
                CartItemDTO cartItemDTO = new CartItemDTO();
                cartItemDTO.setId(cartItem.getId());
                ProductDTO productDTO = ProductMapper.toProductDTO(cartItem.getProduct());
                cartItemDTO.setProduct(productDTO);
                cartItemDTO.setQuantity(cartItem.getQuantity());
                cartItemDTO.setPrice(cartItem.getPrice() * cartItem.getQuantity());
                cartItemDTO.setNote(cartItem.getNote());
                totalPrice += productDTO.getPrice() * cartItem.getQuantity();
                cartItemDTOS.add(cartItemDTO);
            }
        }
        cartDTO.setTotalPrice(totalPrice);
        cartDTO.setCartItems(cartItemDTOS);
        responseData.setSuccess(true);
        responseData.setData(cartDTO);
        return responseData;
    }

    @Override
    public ResponseData insertCart(CartRequest cartRequest) {
        ResponseData responseData = new ResponseData();
        Users user=usersRepository.findById(cartRequest.getUserId());
        if(user == null) {
            responseData.setSuccess(false);
            responseData.setDescription("User not found");
            return responseData;
        }
        Cart cart = cartRepository.findByUser(user);
        if(cart == null) {
            cart = new Cart();
            cart.setStatus(1);
            cart.setUser(user);
            cart.setCreateDate(new Date());
            cart.setCartItems(new ArrayList<>());
            cart.setTotalPrice(0);
            cartRepository.save(cart);
        }
        CartItem cartItem = new CartItem();
        CartItemRequest cartItemRequest = cartRequest.getCartItemRequest();
        Product product = productRepository.findById(cartItemRequest.getProductId());
        if(product == null) {
            responseData.setSuccess(false);
            responseData.setDescription("Product not found");
            return responseData;
        }
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItem.setPrice(product.getPrice());
        cartItem.setCreateDate(new Date());
        cartItem.setNote(cartItemRequest.getNote());
        cart.setTotalPrice(cart.getTotalPrice() + product.getPrice());
        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);
        responseData.setSuccess(true);
        responseData.setDescription("Success");
        return responseData;
    }

    @Override
    public ResponseData deleteCartItem(int cartItemId) {
        ResponseData responseData = new ResponseData();
        CartItem cartItem = cartItemRepository.findById(cartItemId);
        if (cartItem == null) {
            responseData.setSuccess(false);
            responseData.setDescription("Item not found");
            return responseData;
        }
        Cart cart = cartItem.getCart();
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        int totalPrice = cart.getCartItems()
                .stream()
                .mapToInt(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
        responseData.setSuccess(true);
        responseData.setDescription("Success");
        return responseData;
    }


    @Override
    public ResponseData updateQuantityCartItem(int cartItemId, int quantity) {
        ResponseData responseData = new ResponseData();
        CartItem cartItem = cartItemRepository.findById(cartItemId);
        if(cartItem == null) {
            responseData.setSuccess(false);
            responseData.setDescription("Item not found");
            return responseData;
        }
        Cart cart = cartItem.getCart();
        cartItem.setQuantity(quantity);
        int totalPrice = cart.getCartItems()
                .stream()
                .mapToInt(item -> item.getQuantity() * item.getProduct().getPrice())
                .sum();
        cart.setTotalPrice(totalPrice);
        cartRepository.save(cart);
        responseData.setSuccess(true);
        responseData.setDescription("Success");
        return responseData;
    }
}
