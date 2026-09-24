package com.gobimart.controller;

import com.gobimart.entity.Order;
import com.gobimart.service.CartService;
import com.gobimart.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id, Model model, HttpSession session) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        model.addAttribute("cart", cartService.getCart(session));
        return "checkout/confirmation";
    }
}
