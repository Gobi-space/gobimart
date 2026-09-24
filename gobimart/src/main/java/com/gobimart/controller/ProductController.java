package com.gobimart.controller;

import com.gobimart.entity.Product;
import com.gobimart.service.CartService;
import com.gobimart.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    public ProductController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @GetMapping("/products")
    public String listProducts(Model model, HttpSession session) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("currentCategory", "All");
        model.addAttribute("pageTitle", "All Products");
        model.addAttribute("cart", cartService.getCart(session));
        return "products/list";
    }

    @GetMapping("/categories/{category}")
    public String listByCategory(@PathVariable String category, Model model, HttpSession session) {
        List<Product> products = productService.getProductsByCategory(category);
        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("currentCategory", category);
        model.addAttribute("pageTitle", category + " Products");
        model.addAttribute("cart", cartService.getCart(session));
        return "products/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword,
                         Model model,
                         HttpSession session) {
        List<Product> results = productService.searchProducts(keyword);
        model.addAttribute("products", results);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("keyword", keyword);
        model.addAttribute("pageTitle", "Search Results");
        model.addAttribute("cart", cartService.getCart(session));
        return "products/list";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model, HttpSession session) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("cart", cartService.getCart(session));
        return "products/detail";
    }
}
