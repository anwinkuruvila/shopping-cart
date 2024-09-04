package com.shoppingcart.repository;

import com.shoppingcart.model.Category;
import com.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    Category findByName(String name);

    boolean existsByName(String name);
}