package com.foodproject.fooddelivery.repository;

import com.foodproject.fooddelivery.entity.Cart;
import com.foodproject.fooddelivery.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findById(int id);
    Cart findByUser(Users users);
}
