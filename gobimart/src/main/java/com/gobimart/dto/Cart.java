package com.gobimart.dto;

import com.gobimart.entity.Product;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    public void addItem(Product product, int quantity) {
        if (product == null || product.getId() == null || quantity <= 0) {
            return;
        }

        Long productId = product.getId();
        if (items.containsKey(productId)) {
            CartItem existing = items.get(productId);
            int newQty = existing.getQuantity() + quantity;
            // Cap at available stock
            if (product.getStock() != null && newQty > product.getStock()) {
                newQty = product.getStock();
            }
            existing.setQuantity(newQty);
        } else {
            int initialQty = quantity;
            if (product.getStock() != null && initialQty > product.getStock()) {
                initialQty = product.getStock();
            }
            if (initialQty > 0) {
                items.put(productId, new CartItem(product, initialQty));
            }
        }
    }

    public void updateQuantity(Long productId, int quantity) {
        if (items.containsKey(productId)) {
            if (quantity <= 0) {
                items.remove(productId);
            } else {
                CartItem item = items.get(productId);
                int stock = item.getProduct().getStock() != null ? item.getProduct().getStock() : quantity;
                item.setQuantity(Math.min(quantity, stock));
            }
        }
    }

    public void removeItem(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public int getTotalQuantity() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public BigDecimal getTotalAmount() {
        return items.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}
