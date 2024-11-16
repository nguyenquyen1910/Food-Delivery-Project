package com.foodproject.fooddelivery.service;

import com.foodproject.fooddelivery.dto.UsersDTO;
import com.foodproject.fooddelivery.entity.Roles;
import com.foodproject.fooddelivery.entity.Users;
import com.foodproject.fooddelivery.mapper.UserMapper;
import com.foodproject.fooddelivery.payload.ResponseData;
import com.foodproject.fooddelivery.payload.request.ChangeInfoRequest;
import com.foodproject.fooddelivery.payload.request.ChangePasswordRequest;
import com.foodproject.fooddelivery.payload.request.EditUserRequest;
import com.foodproject.fooddelivery.repository.UsersRepository;
import com.foodproject.fooddelivery.service.imp.MailServiceImp;
import com.foodproject.fooddelivery.service.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserServiceImp {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailServiceImp mailServiceImp;

    @Override
    public List<UsersDTO> getAllUsers() {
        List<Users> listUser=usersRepository.findAll();
        return UserMapper.toDTOList(listUser);
    }

    @Override
    public UsersDTO findUserById(int id) {
        Users users=usersRepository.findById(id);
        return UserMapper.toUsersDTO(users);
    }

    @Override
    public UsersDTO findUserByUsername(String username) {
        Users users=usersRepository.findByUserName(username);
        return UserMapper.toUsersDTO(users);
    }

    @Override
    public boolean changeInfo(ChangeInfoRequest changeInfoRequest) {
        Users user=usersRepository.findById(changeInfoRequest.getUserId());
        user.setFullName(changeInfoRequest.getFullname());
        user.setEmail(changeInfoRequest.getEmail());
        user.setAddress(changeInfoRequest.getNewAddress());
        usersRepository.save(user);
        return true;
    }

    @Override
    public ResponseData changePassword(ChangePasswordRequest changePasswordRequest) {
        ResponseData responseData = new ResponseData();
        Users user = usersRepository.findById(changePasswordRequest.getUserId());
        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())){
            responseData.setSuccess(false);
            responseData.setDescription("Wrong old password");
            return responseData;
        }
        if(passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())){
            responseData.setSuccess(false);
            responseData.setExist(true);
            return responseData;
        }
        String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
        user.setPassword(encodedPassword);
        usersRepository.save(user);
        responseData.setSuccess(true);
        return responseData;
    }

    @Override
    public ResponseData resetPassword(String email, String code, String newPassword) {
        ResponseData responseDataRes = new ResponseData();
        ResponseData responseData = mailServiceImp.validateCode(email, code);
        if(responseData.isSuccess()) {
            Users user = usersRepository.findByEmail(email);
            if(passwordEncoder.matches(newPassword, user.getPassword())){
                responseDataRes.setSuccess(false);
                responseDataRes.setExist(true);
                return responseDataRes;
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            usersRepository.save(user);
            responseDataRes.setSuccess(true);
        }
        return responseDataRes;
    }

    @Override
    public boolean editUser(EditUserRequest editUserRequest) {
        if(usersRepository.existsById(editUserRequest.getUserId())) {
            Users user = usersRepository.findById(editUserRequest.getUserId());
            Roles roles=new Roles();
            roles.setId(editUserRequest.getRoleId());
            user.setRoles(roles);
            usersRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean lockUser(int id) {
        if(usersRepository.existsById(id)){
            Users user = usersRepository.findById(id);
            user.setStatus(0);
            usersRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlockUser(int id) {
        if(usersRepository.existsById(id)){
            Users user = usersRepository.findById(id);
            user.setStatus(1);
            usersRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(int id) {
        if(usersRepository.existsById(id)){
            usersRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
