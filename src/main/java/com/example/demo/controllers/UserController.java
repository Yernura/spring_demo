package com.example.demo.controllers;


import com.example.demo.entities.Category;
import com.example.demo.entities.Feature;
import com.example.demo.entities.Filter;
import com.example.demo.entities.Product;
import com.example.demo.repositories.CategoryRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/*
Этот класс реализует API сервер для оброботки запроссов.
 */
@RestController
@RequestMapping("/demo")
public class UserController {
    // Для всех операции я использую таблицу "category".
    @Autowired
    private CategoryRepository categoryRepository;

    // Задания №1. Показать все категории.
    @GetMapping("/categories")
    public ResponseEntity<MappingJacksonValue> getAllCategories(){
        // Создания фильтров для скрытия ненужных данных
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("filters","products");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("categoryFilter", simpleBeanPropertyFilter);

        // Запрос в БД
        List<Category> list = categoryRepository.findAll();

        // Создания JSON ответа на запрос
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);

        // Отправка к клиенту HTTP response
        return ResponseEntity.ok()
                .header("Custom-Header", "foo")
                .body(mappingJacksonValue);

    }

    // Задания №2. Показать товары определенного категорий.
    @GetMapping("/category/{id}")
    public ResponseEntity<MappingJacksonValue> getCategoryProducts(@PathVariable Long id){
        // Создания фильтров для скрытия ненужных данных
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("category","filters");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("productFilter", simpleBeanPropertyFilter);

        // Запрос в БД
        Category category = categoryRepository.findById(id).get();
        Set<Product> list = new HashSet<>();
        list = category.getProducts();

        // Создания JSON ответа на запрос
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);

        // Отправка к клиенту HTTP response
        return ResponseEntity.ok()
                .header("Custom-Header", "foo")
                .body(mappingJacksonValue);
    }

    // Задания №3. Показать филтры определенного категорий.
    @GetMapping("/category/filters/{id}")
    public ResponseEntity<MappingJacksonValue> getCategoryFilters(@PathVariable Long id){
        // Создания фильтров для скрытия ненужных данных
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("filterFilter", simpleBeanPropertyFilter);

        // Запрос в БД
        Category category = categoryRepository.findById(id).get();
        Set<Filter> list = new HashSet<>();
        list = category.getFilters();

        // Создания JSON ответа на запрос
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);

        // Отправка к клиенту HTTP response
        return ResponseEntity.ok()
                .header("Custom-Header", "foo")
                .body(mappingJacksonValue);
    }

    // Задания №4. Показать список продуктов определенного категорий с использованием фильтров.
    @GetMapping("/category/{id}/{filter}")
    public ResponseEntity<MappingJacksonValue> getFilteredProducts(@PathVariable Long id,@PathVariable String filter){
        // Создания фильтров для скрытия ненужных данных
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("productFilter", simpleBeanPropertyFilter);
        // Запрос в БД
        Category category = categoryRepository.findById(id).get();
        Set<Product> list = new HashSet<>();
        list = category.getProducts();

        /*
        Часть кода для использования фильтров на список продуктов.
        Здесь я отфильтровал товары алгоритмическим путем. Но это версия будет нагружать сервер если база данных огромна.
        В этом кейсе наилучшии вариант - использование методов "specification,criteria" (https://medium.com/@milan.brankovic/spring-advanced-search-filtering-5ee850f9458c)
        Но эти методы требуют большие изменения в моей структуре БД. Я думая эти изменения потребует много времени. Поэтому я оставил менее оптимальный вариант фильтрования.
         */
        StringTokenizer st= new StringTokenizer(filter,"&");
        while(st.hasMoreTokens()){
            StringTokenizer st2 = new StringTokenizer(st.nextToken(),"=");
            Long filterId = Long.parseLong(st2.nextToken());
            StringTokenizer st3 = new StringTokenizer(st2.nextToken(),",");
            ArrayList<String> values = new ArrayList<>();
            while (st3.hasMoreTokens()){
                values.add(st3.nextToken());
            }
            Set<Product> temp = new HashSet<>();
            for (Product p: list) {
                for (Feature f:p.getFeatures()) {
                    if(f.getFilter().getId()==filterId && values.contains(f.getValue())){
                        temp.add(p);
                        break;
                    }
                }
            }
            list=temp;
        }

        // Создания JSON ответа на запрос
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);

        // Отправка к клиенту HTTP response
        return ResponseEntity.ok()
                .header("Custom-Header", "foo")
                .body(mappingJacksonValue);
    }

}

