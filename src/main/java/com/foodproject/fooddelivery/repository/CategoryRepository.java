package com.foodproject.fooddelivery.repository;

import com.foodproject.fooddelivery.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findById(int id);
}
