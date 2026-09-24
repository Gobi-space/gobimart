package com.gobimart.service;

import com.gobimart.dto.Cart;
import com.gobimart.dto.CheckoutRequest;
import com.gobimart.entity.Order;
import com.gobimart.entity.Product;
import com.gobimart.exception.EmptyCartException;
import com.gobimart.exception.InsufficientStockException;
import com.gobimart.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CartService cartService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private OrderService orderService;

    private CheckoutRequest validCheckout;

    @BeforeEach
    void setUp() {
        validCheckout = new CheckoutRequest("Gobinath S", "gobinath@example.com", "9876543210", "123 College Road, Tamil Nadu");
    }

    @Test
    @DisplayName("Should throw EmptyCartException when attempting to place order with empty cart")
    void testPlaceOrderEmptyCart() {
        Cart emptyCart = new Cart();
        when(cartService.getCart(session)).thenReturn(emptyCart);

        assertThrows(EmptyCartException.class, () -> orderService.placeOrder(validCheckout, session));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Should successfully place order, reduce stock, and clear cart")
    void testPlaceOrderSuccess() {
        Cart cart = new Cart();
        Product product = new Product(1L, "Laptop", "Fast Laptop", new BigDecimal("60000.00"), "img.jpg", "Electronics", 10);
        cart.addItem(product, 2);

        when(cartService.getCart(session)).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(100L);
            return o;
        });

        Order placedOrder = orderService.placeOrder(validCheckout, session);

        assertNotNull(placedOrder);
        assertEquals(100L, placedOrder.getId());
        assertEquals("PLACED", placedOrder.getStatus());
        assertEquals(new BigDecimal("120000.00"), placedOrder.getTotalAmount());
        assertEquals(8, product.getStock(), "Product stock should be reduced from 10 to 8");

        verify(productService, times(1)).save(product);
        verify(cartService, times(1)).clearCart(session);
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when item quantity exceeds available stock")
    void testPlaceOrderInsufficientStock() {
        Cart cart = new Cart();
        Product productInCart = new Product(1L, "Limited Book", "Special book", new BigDecimal("500.00"), "img.jpg", "Books", 5);
        cart.addItem(productInCart, 3);

        // Simulate stock changed in DB before checkout
        Product productInDb = new Product(1L, "Limited Book", "Special book", new BigDecimal("500.00"), "img.jpg", "Books", 1);

        when(cartService.getCart(session)).thenReturn(cart);
        when(productService.getProductById(1L)).thenReturn(productInDb);

        assertThrows(InsufficientStockException.class, () -> orderService.placeOrder(validCheckout, session));
        verify(orderRepository, never()).save(any(Order.class));
    }
}
