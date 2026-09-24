package com.gobimart.service;

import com.gobimart.dto.ProductForm;
import com.gobimart.entity.Product;
import com.gobimart.exception.ProductNotFoundException;
import com.gobimart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product(1L, "Smartphone", "5G Phone", new BigDecimal("30000.00"), "img.jpg", "Electronics", 15);
    }

    @Test
    @DisplayName("Should find product by ID when product exists")
    void testGetProductByIdFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));

        Product result = productService.getProductById(1L);
        assertNotNull(result);
        assertEquals("Smartphone", result.getName());
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product does not exist")
    void testGetProductByIdNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    @DisplayName("Should create product and return saved entity")
    void testCreateProduct() {
        ProductForm form = new ProductForm(null, "Backpack", "Travel bag", new BigDecimal("2000.00"), "Accessories", 20, "backpack.jpg");
        when(productRepository.save(any(Product.class))).thenReturn(new Product(2L, form.getName(), form.getDescription(), form.getPrice(), form.getImageUrl(), form.getCategory(), form.getStock()));

        Product created = productService.createProduct(form);
        assertNotNull(created);
        assertEquals("Backpack", created.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should delete product when ID exists")
    void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}
