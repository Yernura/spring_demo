package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "category")
@JsonFilter("categoryFilter")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name",nullable = false)
    private String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST,mappedBy = "category")
    private Set<Filter> filters;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST,mappedBy = "parentCategory")
    private Set<Category> childCategories;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST,mappedBy = "category")
    private Set<Product> products;

    public Category() {
    }

    public Category(String name, Category parentCategory, Set<Filter> filters) {
        this.name = name;
        this.parentCategory = parentCategory;
        this.filters = filters;
    }

    public Category(String name, Category parentCategory, Set<Filter> filters, Set<Category> childCategories, Set<Product> products) {
        this.name = name;
        this.parentCategory = parentCategory;
        this.filters = filters;
        this.childCategories = childCategories;
        this.products = products;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Set<Filter> getFilters() {
        return filters;
    }

    public void setFilters(Set<Filter> filters) {
        this.filters = filters;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(Set<Category> childCategories) {
        this.childCategories = childCategories;
    }


}
