package com.gobimart.service;

import com.gobimart.dto.Cart;
import com.gobimart.dto.CartItem;
import com.gobimart.dto.CheckoutRequest;
import com.gobimart.entity.Order;
import com.gobimart.entity.OrderItem;
import com.gobimart.entity.Product;
import com.gobimart.exception.EmptyCartException;
import com.gobimart.exception.InsufficientStockException;
import com.gobimart.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, ProductService productService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.cartService = cartService;
    }

    @Transactional
    public Order placeOrder(CheckoutRequest request, HttpSession session) {
        Cart cart = cartService.getCart(session);

        // 1. Validate cart is not empty
        if (cart.isEmpty()) {
            throw new EmptyCartException("Your shopping cart is empty. Please add items before checking out.");
        }

        // 2. Validate customer information
        if (request.getCustomerName() == null || request.getCustomerName().trim().isEmpty() ||
            request.getEmail() == null || request.getEmail().trim().isEmpty() ||
            request.getPhone() == null || request.getPhone().trim().isEmpty() ||
            request.getAddress() == null || request.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("All checkout fields are required.");
        }

        // 3. Create Order
        Order order = new Order();
        order.setCustomerName(request.getCustomerName().trim());
        order.setEmail(request.getEmail().trim());
        order.setPhone(request.getPhone().trim());
        order.setAddress(request.getAddress().trim());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 4 & 5. Check stock, create OrderItems, reduce stock
        for (CartItem cartItem : cart.getItems()) {
            Product currentProduct = productService.getProductById(cartItem.getProduct().getId());
            int requestedQuantity = cartItem.getQuantity();

            if (currentProduct.getStock() < requestedQuantity) {
                throw new InsufficientStockException("Insufficient stock for product '" + currentProduct.getName() +
                        "'. Requested: " + requestedQuantity + ", Available: " + currentProduct.getStock());
            }

            // Reduce stock
            currentProduct.setStock(currentProduct.getStock() - requestedQuantity);
            productService.save(currentProduct);

            // Create OrderItem
            OrderItem orderItem = new OrderItem(order, currentProduct, requestedQuantity, currentProduct.getPrice());
            order.addItem(orderItem);

            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }

        order.setTotalAmount(totalAmount);

        // Save order and cascade to OrderItems
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cartService.clearCart(session);

        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + id));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    public long countAllOrders() {
        return orderRepository.count();
    }
}
