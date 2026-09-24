package com.gobimart.service;

import com.gobimart.dto.Cart;
import com.gobimart.entity.Product;
import com.gobimart.exception.InsufficientStockException;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    public static final String SESSION_CART_KEY = "gobimart_cart";

    private final ProductService productService;

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(SESSION_CART_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(SESSION_CART_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Long productId, int quantity) {
        if (quantity <= 0) {
            return;
        }

        Product product = productService.getProductById(productId);
        if (product.isOutOfStock()) {
            throw new InsufficientStockException("Product '" + product.getName() + "' is out of stock.");
        }

        Cart cart = getCart(session);
        cart.addItem(product, quantity);
        session.setAttribute(SESSION_CART_KEY, cart);
    }

    public void updateQuantity(HttpSession session, Long productId, int quantity) {
        Cart cart = getCart(session);
        if (quantity <= 0) {
            cart.removeItem(productId);
        } else {
            Product product = productService.getProductById(productId);
            if (product.getStock() != null && quantity > product.getStock()) {
                cart.updateQuantity(productId, product.getStock());
                throw new InsufficientStockException("Only " + product.getStock() + " item(s) available in stock for " + product.getName());
            } else {
                cart.updateQuantity(productId, quantity);
            }
        }
        session.setAttribute(SESSION_CART_KEY, cart);
    }

    public void removeFromCart(HttpSession session, Long productId) {
        Cart cart = getCart(session);
        cart.removeItem(productId);
        session.setAttribute(SESSION_CART_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        Cart cart = getCart(session);
        cart.clear();
        session.setAttribute(SESSION_CART_KEY, cart);
    }
}
