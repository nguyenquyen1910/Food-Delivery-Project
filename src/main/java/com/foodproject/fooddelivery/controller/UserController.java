package com.foodproject.fooddelivery.controller;

import com.foodproject.fooddelivery.dto.UsersDTO;
import com.foodproject.fooddelivery.entity.Users;
import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.payload.request.ChangeInfoRequest;
import com.foodproject.fooddelivery.payload.request.ChangePasswordRequest;
import com.foodproject.fooddelivery.payload.request.EditUserRequest;
import com.foodproject.fooddelivery.repository.UsersRepository;
import com.foodproject.fooddelivery.service.MailService;
import com.foodproject.fooddelivery.service.UserService;
import com.foodproject.fooddelivery.service.imp.MailServiceImp;
import com.foodproject.fooddelivery.service.imp.UserServiceImp;
import com.foodproject.fooddelivery.utils.JwtUtilHelper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@CrossOrigin("*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;
    @Autowired
    private JwtUtilHelper jwtUtilHelper;
    @Autowired

    private MailService mailService;
    @Autowired
    private MailServiceImp mailServiceImp;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("")
    public ResponseEntity<?> getAllUser() {
        return new ResponseEntity<>(userServiceImp.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        if (token != null && jwtUtilHelper.verifyToken(token)) {
            String username = jwtUtilHelper.extractUsername(token);
            UsersDTO currentUser = userServiceImp.findUserByUsername(username);
            if (currentUser != null) {
                return new ResponseEntity<>(currentUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    @PostMapping("/change")
    public ResponseEntity<?> changeUser(@RequestBody ChangeInfoRequest changeInfoRequest) {
        ResponseData responseData = new ResponseData();
        boolean isSuccess = userServiceImp.changeInfo(changeInfoRequest);
        responseData.setData(isSuccess);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        ResponseData responseData = userServiceImp.changePassword(changePasswordRequest);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PutMapping("/admin/edit")
    public ResponseEntity<?> editUser(@RequestBody EditUserRequest editUserRequest) {
        ResponseData responseData = new ResponseData();
        boolean isSuccess = userServiceImp.editUser(editUserRequest);
        responseData.setData(isSuccess);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/admin/lock/{id}")
    public ResponseEntity<?> lockUser(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        boolean isLock = userServiceImp.lockUser(id);
        responseData.setData(isLock);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }

    @PostMapping("/admin/unlock/{id}")
    public ResponseEntity<?> unlockUser(@PathVariable int id) {
        ResponseData responseData = new ResponseData();
        boolean isUnlock = userServiceImp.unlockUser(id);
        responseData.setData(isUnlock);
        return new ResponseEntity<>(responseData,HttpStatus.OK);
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        ResponseData responseData=new ResponseData();
        boolean isSuccess = userServiceImp.deleteUser(id);
        responseData.setData(isSuccess);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/forgot/send")
    public ResponseEntity<?> forgotPassword(@RequestParam String email){
        ResponseData responseData=new ResponseData();
        if(!usersRepository.existsByEmail(email)){
            responseData.setSuccess(false);
            responseData.setDescription("User not found");
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
        Users user = usersRepository.findByEmail(email);
        Timestamp createCodeAt = user.getCreateCodeAt();

        if(createCodeAt != null) {
            long minutesSinceLastSent = ChronoUnit.SECONDS.between(createCodeAt.toLocalDateTime(), LocalDateTime.now());
            if (minutesSinceLastSent < 60) {
                responseData.setSuccess(false);
                responseData.setDescription("Wait 60s!");
                return new ResponseEntity<>(responseData, HttpStatus.OK);
            }
        }

        String resetCode = mailService.generateResetCode(email);
        String fullname = user.getFullName();
        responseData = mailServiceImp.sentForgotPasswordMail(fullname, email, resetCode);

        if (responseData.isSuccess()) {
            user.setCreateCodeAt(Timestamp.valueOf(LocalDateTime.now()));
            user.setVerifyCode(resetCode);
            usersRepository.save(user);
        }
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("/forgot/verify")
    public ResponseEntity<?> verifyForgotPassword(@RequestParam String email, @RequestParam String code){
        ResponseData responseData = mailServiceImp.validateCode(email, code);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PostMapping("forgot/reset/password")
    public ResponseEntity<?> resetForgotPassword(@RequestParam String email,
                                                 @RequestParam String code,
                                                 @RequestParam String newPassword){
        ResponseData responseData = userServiceImp.resetPassword(email, code, newPassword);
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
