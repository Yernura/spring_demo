package com.example.demo.repositories;

import com.example.demo.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Этот интерфейс используется для манипуляции с таблицы "category"
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
}
