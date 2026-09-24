package com.gobimart.repository;

import com.gobimart.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should save product and retrieve by ID")
    void testSaveAndFindById() {
        Product product = new Product("Gaming Mouse", "Precision optical mouse", new BigDecimal("1500.00"), "http://img.jpg", "Accessories", 10);
        Product saved = productRepository.save(product);

        assertNotNull(saved.getId());
        assertTrue(productRepository.findById(saved.getId()).isPresent());
        assertEquals("Gaming Mouse", saved.getName());
    }

    @Test
    @DisplayName("Should find products by category ignore case")
    void testFindByCategoryIgnoreCase() {
        Product p1 = new Product("Smart TV", "4K HDR TV", new BigDecimal("45000.00"), "http://img.jpg", "Electronics", 5);
        Product p2 = new Product("Novel Book", "Fiction novel", new BigDecimal("400.00"), "http://img.jpg", "Books", 15);
        productRepository.save(p1);
        productRepository.save(p2);

        List<Product> electronics = productRepository.findByCategoryIgnoreCase("electronics");
        assertFalse(electronics.isEmpty());
        assertTrue(electronics.stream().anyMatch(p -> p.getName().equals("Smart TV")));
    }

    @Test
    @DisplayName("Should search products by name or description keyword")
    void testSearchByKeyword() {
        Product p1 = new Product("Wireless Earbuds", "Bluetooth earbuds with noise isolation", new BigDecimal("2500.00"), "http://img.jpg", "Electronics", 20);
        productRepository.save(p1);

        List<Product> resultsByName = productRepository.searchByKeyword("earbuds");
        assertFalse(resultsByName.isEmpty());

        List<Product> resultsByDesc = productRepository.searchByKeyword("isolation");
        assertFalse(resultsByDesc.isEmpty());
    }
}
