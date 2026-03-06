package com.microservice.product.controller;

import com.microservice.product.db.ProductRepository;
import com.microservice.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class SlashProduct {
    @Autowired
    private ProductRepository productRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product save(@RequestBody Product product) {
        return productRepository.save(product);
    }

    @GetMapping(value = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product findAllByProductId(@PathVariable Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NullPointerException("Product with id: " + productId + " not found"));
    }
}
