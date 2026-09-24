package com.gobimart.config;

import com.gobimart.entity.Product;
import com.gobimart.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            List<Product> sampleProducts = Arrays.asList(
                    new Product(
                            "Pro Laptop 15-inch",
                            "High performance laptop featuring Intel Core i7 processor, 16GB RAM, and 512GB NVMe SSD. Perfect for programming, design, and daily work.",
                            new BigDecimal("84999.00"),
                            "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&auto=format&fit=crop&q=80",
                            "Electronics",
                            25
                    ),
                    new Product(
                            "Ultra Smartphone 5G",
                            "Sleek smartphone with 6.7-inch AMOLED 120Hz display, 108MP triple camera system, and 5000mAh all-day battery.",
                            new BigDecimal("34999.00"),
                            "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=600&auto=format&fit=crop&q=80",
                            "Electronics",
                            40
                    ),
                    new Product(
                            "Wireless Noise-Cancelling Headphones",
                            "Over-ear premium Bluetooth headphones with active noise cancellation, deep bass, and 30-hour battery life.",
                            new BigDecimal("7999.00"),
                            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80",
                            "Electronics",
                            50
                    ),
                    new Product(
                            "Smart Watch Fitness Tracker",
                            "Water-resistant smartwatch with heart rate monitor, SpO2 sensor, sleep tracking, and customizable watch faces.",
                            new BigDecimal("4499.00"),
                            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80",
                            "Electronics",
                            30
                    ),
                    new Product(
                            "10.5-inch Digital Tablet",
                            "Versatile tablet with crystal clear Retina-grade display, stereo speakers, and stylus support for study and entertainment.",
                            new BigDecimal("28999.00"),
                            "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=600&auto=format&fit=crop&q=80",
                            "Electronics",
                            20
                    ),
                    new Product(
                            "Mechanical Gaming Keyboard",
                            "RGB backlit mechanical keyboard with tactile blue switches, durable aluminum top plate, and anti-ghosting keys.",
                            new BigDecimal("3299.00"),
                            "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=600&auto=format&fit=crop&q=80",
                            "Accessories",
                            35
                    ),
                    new Product(
                            "Ergonomic Wireless Mouse",
                            "Precision optical wireless mouse with ergonomic thumb rest, silent clicks, and adjustable DPI up to 3200.",
                            new BigDecimal("1299.00"),
                            "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=600&auto=format&fit=crop&q=80",
                            "Accessories",
                            60
                    ),
                    new Product(
                            "Urban Travel Laptop Backpack",
                            "Waterproof multi-compartment travel backpack with dedicated 15.6-inch laptop sleeve and USB charging port.",
                            new BigDecimal("2199.00"),
                            "https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80",
                            "Accessories",
                            45
                    ),
                    new Product(
                            "Lightweight Running Shoes",
                            "Breathable mesh running sneakers with responsive cushioning sole, ideal for daily fitness and running.",
                            new BigDecimal("2799.00"),
                            "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop&q=80",
                            "Fashion",
                            28
                    ),
                    new Product(
                            "Classic Cotton Crewneck T-Shirt",
                            "100% premium combed organic cotton t-shirt with modern regular fit, soft texture, and durable stitching.",
                            new BigDecimal("699.00"),
                            "https://images.unsplash.com/photo-1521572267360-ee0c2909d518?w=600&auto=format&fit=crop&q=80",
                            "Fashion",
                            80
                    ),
                    new Product(
                            "Clean Architecture & Java Guide",
                            "Comprehensive software engineering book covering modern Spring Boot patterns, microservices, and clean code principles.",
                            new BigDecimal("1499.00"),
                            "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80",
                            "Books",
                            15
                    ),
                    new Product(
                            "Modern Minimalist Desk Lamp",
                            "Dimmable LED desk lamp with 5 color modes, touch sensitive controls, and integrated wireless charging base.",
                            new BigDecimal("1899.00"),
                            "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600&auto=format&fit=crop&q=80",
                            "Home",
                            22
                    ),
                    new Product(
                            "Insulated Stainless Steel Water Bottle",
                            "Double-wall vacuum insulated 750ml flask that keeps drinks ice cold for 24 hours or piping hot for 12 hours.",
                            new BigDecimal("899.00"),
                            "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=600&auto=format&fit=crop&q=80",
                            "Home",
                            70
                    ),
                    new Product(
                            "Vintage Leather Bi-Fold Wallet",
                            "Handcrafted genuine leather wallet with RFID blocking layer, 8 card slots, and currency compartment.",
                            new BigDecimal("1199.00"),
                            "https://images.unsplash.com/photo-1627123424574-724758594e93?w=600&auto=format&fit=crop&q=80",
                            "Accessories",
                            32
                    )
            );

            productRepository.saveAll(sampleProducts);
        }
    }
}
