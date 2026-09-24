package com.gobimart.service;

import com.gobimart.dto.Cart;
import com.gobimart.dto.CartItem;
import com.gobimart.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceTest {

    @Test
    @DisplayName("Should add product to cart and calculate correct subtotal and total")
    void testCartAddAndCalculateTotal() {
        Cart cart = new Cart();
        Product product1 = new Product(1L, "Laptop", "Fast laptop", new BigDecimal("50000.00"), "img1.jpg", "Electronics", 10);
        Product product2 = new Product(2L, "Mouse", "Wireless mouse", new BigDecimal("1000.00"), "img2.jpg", "Accessories", 20);

        cart.addItem(product1, 1);
        cart.addItem(product2, 2);

        assertEquals(2, cart.getItems().size());
        assertEquals(3, cart.getTotalQuantity());
        // 50000*1 + 1000*2 = 52000.00
        assertEquals(new BigDecimal("52000.00"), cart.getTotalAmount());
    }

    @Test
    @DisplayName("Should not allow quantity to exceed available stock")
    void testCartStockCapping() {
        Cart cart = new Cart();
        Product product = new Product(1L, "Desk Lamp", "Lamp", new BigDecimal("1500.00"), "img.jpg", "Home", 3);

        cart.addItem(product, 5); // Requested 5, stock is 3

        CartItem item = cart.getItems().get(0);
        assertEquals(3, item.getQuantity(), "Quantity should be capped at available stock");
    }

    @Test
    @DisplayName("Should remove and clear items from cart")
    void testCartRemoveAndClear() {
        Cart cart = new Cart();
        Product p = new Product(1L, "Novel", "Book", new BigDecimal("500.00"), "img.jpg", "Books", 10);
        cart.addItem(p, 2);

        assertFalse(cart.isEmpty());

        cart.removeItem(1L);
        assertTrue(cart.isEmpty());
        assertEquals(BigDecimal.ZERO, cart.getTotalAmount());
    }
}
