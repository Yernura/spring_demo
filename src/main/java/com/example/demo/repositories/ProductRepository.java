package com.example.demo.repositories;

import com.example.demo.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Этот интерфейс используется для манипуляции с таблицы "product"
 */
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
}
