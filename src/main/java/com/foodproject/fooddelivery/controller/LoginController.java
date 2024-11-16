package com.foodproject.fooddelivery.controller;

import com.foodproject.fooddelivery.dto.UsersDTO;
import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.payload.request.SignUpRequest;
import com.foodproject.fooddelivery.service.imp.LoginServiceImp;
import com.foodproject.fooddelivery.service.imp.UserServiceImp;
import com.foodproject.fooddelivery.utils.JwtUtilHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginServiceImp loginServiceImp;

    @Autowired
    JwtUtilHelper jwtUtilHelper;

    @Autowired
    UserServiceImp userServiceImp;

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestParam String userName, @RequestParam String password) {
        return new ResponseEntity<>(loginServiceImp.checkLogin(userName, password), HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignUpRequest signUpRequest) {
        ResponseData responseData = loginServiceImp.addUser(signUpRequest);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
