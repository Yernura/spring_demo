package com.example.demo.controllers;


import com.example.demo.entities.Category;
import com.example.demo.entities.Feature;
import com.example.demo.entities.Filter;
import com.example.demo.entities.Product;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.FeatureRepository;
import com.example.demo.repositories.FilterRepository;
import com.example.demo.repositories.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/demo")
public class UserController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FeatureRepository featureRepository;
    @Autowired
    private FilterRepository filterRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public MappingJacksonValue getAllCategories(){
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("filters","products");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("categoryFilter", simpleBeanPropertyFilter);
        List<Category> list = categoryRepository.findAll();
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/category/{id}")
    public MappingJacksonValue getCategoryProducts(@PathVariable Long id){
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("category","filters");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("productFilter", simpleBeanPropertyFilter);
        Category category = categoryRepository.findById(id).get();
        Set<Product> list = new HashSet<>();
        list = category.getProducts();

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/category/filters/{id}")
    public MappingJacksonValue getCategoryFilters(@PathVariable Long id) throws JsonProcessingException {
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("filterFilter", simpleBeanPropertyFilter);
        Category category = categoryRepository.findById(id).get();
        Set<Filter> list = new HashSet<>();
        list = category.getFilters();

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

    @GetMapping("/category/{id}/{filter}")
    public MappingJacksonValue getFilteredProducts(@PathVariable Long id,@PathVariable String filter){
        SimpleBeanPropertyFilter simpleBeanPropertyFilter = SimpleBeanPropertyFilter.serializeAllExcept("category");
        FilterProvider filterProvider = new SimpleFilterProvider().addFilter("productFilter", simpleBeanPropertyFilter);
        Category category = categoryRepository.findById(id).get();
        Set<Product> list = new HashSet<>();
        list = category.getProducts();
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
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setFilters(filterProvider);
        return mappingJacksonValue;
    }

}

