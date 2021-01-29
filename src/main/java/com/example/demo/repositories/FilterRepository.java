package com.example.demo.repositories;

import com.example.demo.entities.Filter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilterRepository extends JpaRepository<Filter,Long> {
}
