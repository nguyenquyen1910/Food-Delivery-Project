package com.foodproject.fooddelivery.security;

import com.foodproject.fooddelivery.entity.Users;
import com.foodproject.fooddelivery.repository.UsersRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UsersRepository usersRepository;
    public CustomUserDetailService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Users user;
        if(usernameOrEmail.contains("@")) {
            user=usersRepository.findByEmail(usernameOrEmail);
        }
        else{
            user=usersRepository.findByUserName(usernameOrEmail);
        }
        if(user==null){
            throw new UsernameNotFoundException("User not found");
        }

        return new User(user.getUserName(),user.getPassword(),new ArrayList<>());
    }
}
