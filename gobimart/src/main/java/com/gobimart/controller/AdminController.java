package com.gobimart.controller;

import com.gobimart.dto.ProductForm;
import com.gobimart.entity.Order;
import com.gobimart.entity.Product;
import com.gobimart.service.OrderService;
import com.gobimart.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("totalProducts", productService.countAllProducts());
        model.addAttribute("totalOrders", orderService.countAllOrders());
        model.addAttribute("totalCategories", productService.getAllCategories().size());

        List<Order> allOrders = orderService.getAllOrders();
        // Limit to 5 recent orders for dashboard view
        List<Order> recentOrders = allOrders.size() > 5 ? allOrders.subList(0, 5) : allOrders;
        model.addAttribute("recentOrders", recentOrders);

        return "admin/dashboard";
    }

    // -------------------------------------------------------------
    // Product Management
    // -------------------------------------------------------------

    @GetMapping("/products")
    public String viewProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/products";
    }

    @GetMapping("/products/new")
    public String showAddProductForm(Model model) {
        if (!model.containsAttribute("productForm")) {
            model.addAttribute("productForm", new ProductForm());
        }
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("isEdit", false);
        return "admin/product-form";
    }

    @PostMapping("/products/new")
    public String createProduct(@Valid @ModelAttribute("productForm") ProductForm productForm,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("isEdit", false);
            return "admin/product-form";
        }

        productService.createProduct(productForm);
        redirectAttributes.addFlashAttribute("successMessage", "Product '" + productForm.getName() + "' created successfully!");
        return "redirect:/admin/products";
    }

    @GetMapping("/products/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        ProductForm form = new ProductForm(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getStock(),
                product.getImageUrl()
        );

        model.addAttribute("productForm", form);
        model.addAttribute("categories", productService.getAllCategories());
        model.addAttribute("isEdit", true);
        return "admin/product-form";
    }

    @PostMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable Long id,
                                @Valid @ModelAttribute("productForm") ProductForm productForm,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            productForm.setId(id);
            model.addAttribute("categories", productService.getAllCategories());
            model.addAttribute("isEdit", true);
            return "admin/product-form";
        }

        productService.updateProduct(id, productForm);
        redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
        return "redirect:/admin/products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to delete product: " + ex.getMessage());
        }
        return "redirect:/admin/products";
    }

    // -------------------------------------------------------------
    // Order Management
    // -------------------------------------------------------------

    @GetMapping("/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin/orders";
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order-detail";
    }
}
