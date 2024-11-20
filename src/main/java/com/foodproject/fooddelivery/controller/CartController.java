package com.foodproject.fooddelivery.controller;

import com.foodproject.fooddelivery.dto.CartDTO;
import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.payload.request.CartRequest;
import com.foodproject.fooddelivery.service.imp.CartServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartServiceImp cartServiceImp;

    @GetMapping("/get-cart/{userId}")
    public ResponseEntity<ResponseData> getCart(@PathVariable int userId) {
        ResponseData responseData = cartServiceImp.getAllCart(userId);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/insert")
    public ResponseEntity<?> insertCart(@RequestBody CartRequest cartRequest) {
        ResponseData responseData = cartServiceImp.insertCart(cartRequest);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable int cartItemId){
        ResponseData responseData = cartServiceImp.deleteCartItem(cartItemId);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }

    @PostMapping("update/{cartItemId}/{quantity}")
    public ResponseEntity<?> updateCartItem(@PathVariable int cartItemId, @PathVariable int quantity){
        ResponseData responseData = cartServiceImp.updateQuantityCartItem(cartItemId, quantity);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
