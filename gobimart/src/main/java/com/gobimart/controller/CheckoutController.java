package com.gobimart.controller;

import com.gobimart.dto.Cart;
import com.gobimart.dto.CheckoutRequest;
import com.gobimart.entity.Order;
import com.gobimart.exception.EmptyCartException;
import com.gobimart.exception.InsufficientStockException;
import com.gobimart.service.CartService;
import com.gobimart.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping
    public String showCheckoutForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Cart cart = cartService.getCart(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty. Please add items before checking out.");
            return "redirect:/cart";
        }

        if (!model.containsAttribute("checkoutRequest")) {
            model.addAttribute("checkoutRequest", new CheckoutRequest());
        }
        model.addAttribute("cart", cart);
        return "checkout/form";
    }

    @PostMapping("/place-order")
    public String placeOrder(@Valid @ModelAttribute("checkoutRequest") CheckoutRequest checkoutRequest,
                             BindingResult bindingResult,
                             Model model,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        Cart cart = cartService.getCart(session);
        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty.");
            return "redirect:/cart";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("cart", cart);
            return "checkout/form";
        }

        try {
            Order order = orderService.placeOrder(checkoutRequest, session);
            return "redirect:/orders/confirmation/" + order.getId();
        } catch (InsufficientStockException | EmptyCartException ex) {
            model.addAttribute("cart", cart);
            model.addAttribute("errorMessage", ex.getMessage());
            return "checkout/form";
        } catch (Exception ex) {
            model.addAttribute("cart", cart);
            model.addAttribute("errorMessage", "Failed to place order: " + ex.getMessage());
            return "checkout/form";
        }
    }
}
