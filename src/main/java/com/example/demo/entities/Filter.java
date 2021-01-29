package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "filter")
@JsonFilter("filterFilter")
public class Filter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name",nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, mappedBy = "filter")
    private Set<Feature> features;

    public Filter() {
    }

    public Filter(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public Filter(String name, Category category, Set<Feature> features) {
        this.name = name;
        this.category = category;
        this.features = features;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }
}
