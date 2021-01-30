package com.example.demo.repositories;

import com.example.demo.entities.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Этот интерфейс используется для манипуляции с таблицы "filter"
 */
@Repository
public interface FilterRepository extends JpaRepository<Filter,Long> {
}
