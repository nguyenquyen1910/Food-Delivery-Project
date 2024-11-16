package com.foodproject.fooddelivery.service.imp;

import com.foodproject.fooddelivery.payload.ResponseData;

public interface MailServiceImp {
    ResponseData sentForgotPasswordMail(String fullname, String toAddress, String verifyCode);
    ResponseData validateCode(String email, String resetCode);
}
