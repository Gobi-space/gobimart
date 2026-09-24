package com.gobimart.controller;

import com.gobimart.service.CartService;
import com.gobimart.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CartService cartService;

    public HomeController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("featuredProducts", productService.getAllProducts());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("cart", cartService.getCart(session));
        return "index";
    }
}
