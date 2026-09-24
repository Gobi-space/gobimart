package com.gobimart.service;

import com.gobimart.dto.ProductForm;
import com.gobimart.entity.Product;
import com.gobimart.exception.ProductNotFoundException;
import com.gobimart.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ProductService {

    public static final List<String> DEFAULT_CATEGORIES = Arrays.asList(
            "Electronics",
            "Fashion",
            "Home",
            "Books",
            "Accessories"
    );

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryIgnoreCase(category);
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllProducts();
        }
        return productRepository.searchByKeyword(keyword.trim());
    }

    @Transactional
    public Product createProduct(ProductForm form) {
        Product product = new Product();
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setCategory(form.getCategory());
        product.setStock(form.getStock());
        product.setImageUrl(form.getImageUrl());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, ProductForm form) {
        Product product = getProductById(id);
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setCategory(form.getCategory());
        product.setStock(form.getStock());
        product.setImageUrl(form.getImageUrl());
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Cannot delete. Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    public long countAllProducts() {
        return productRepository.count();
    }

    public List<String> getAllCategories() {
        return DEFAULT_CATEGORIES;
    }

    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }
}
