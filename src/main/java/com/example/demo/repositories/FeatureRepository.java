package com.example.demo.repositories;

import com.example.demo.entities.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Этот интерфейс используется для манипуляции с таблицы "feature"
 */
@Repository
public interface FeatureRepository extends JpaRepository<Feature,Long> {
}
