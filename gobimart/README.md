# GOBIMART

**GOBIMART** is a clean, student-friendly, monolithic e-commerce Minimum Viable Product (MVP) designed and architected using Java 17+, Spring Boot, Spring MVC, Spring Data JPA, Hibernate, Thymeleaf, and an embedded file-based H2 database.

---

## 1. Description

GOBIMART is engineered for college project demonstrations and software engineering viva examinations. It provides a full shopping lifecycle—from catalog browsing, category filtering, and keyword searching, to session-based cart management, server-validated checkout with Cash on Delivery, and automated stock deduction. It also includes an administrative management portal for managing products and reviewing customer orders.

---

## 2. Features

### Customer Features
- **Home Page**: Features an eye-catching hero banner, quick category navigation chips, and a grid of featured products.
- **Product Catalog**: Complete product browsing with real-time stock status ("In Stock" count or "Out of Stock" badge).
- **Category Filtering**: Filter products instantly across five core categories: *Electronics*, *Fashion*, *Home*, *Books*, and *Accessories*.
- **Product Search**: Case-insensitive keyword search by product name and description (`/search?keyword=laptop`) with friendly "No products found" fallback.
- **Product Details**: High-resolution image preview, pricing, category, detailed specifications, stock count, and quantity selector.
- **Session-Based Cart**:
  - Add items with custom quantities
  - Increase or decrease quantity directly from cart
  - Automatic capping at available inventory stock
  - Remove individual items
  - Clear entire cart
  - Dynamic subtotal and total calculations
- **Checkout & Order Placement**:
  - Server-side validation for customer details (Name, Email, Phone, Address)
  - Demo payment method: **Cash on Delivery (Demo Order)**
  - Transactional inventory deduction preventing overselling
- **Order Confirmation**:
  - Displays unique Order ID, date, status (`PLACED`), customer shipping details, itemized breakdown, and total price.
  - "Continue Shopping" navigation.

### Admin Features (Demo/MVP)
- **Dashboard**: High-level store metrics displaying total products, total customer orders, total categories, and recent orders.
- **Product Management (CRUD)**:
  - View all products in a structured table
  - Add new products with Bean Validation (`@NotBlank`, `@DecimalMin`, `@Min`, etc.)
  - Edit existing product details, prices, and stock
  - Delete products
- **Order Management**:
  - View all customer orders sorted chronologically
  - Detailed order view showing customer shipping info, status, ordered products, and totals.

> [!NOTE]
> **Admin Security Notice**: The Admin section is a demo/MVP feature intended for academic presentation and does not include complex login authentication or Spring Security credentials.

---

## 3. Technology Stack

- **Backend Language**: Java 17+
- **Framework**: Spring Boot 3.2.4
- **Web MVC**: Spring MVC
- **Persistence & ORM**: Spring Data JPA, Hibernate
- **Database**: H2 Database (File-based storage for data persistence across restarts)
- **Template Engine**: Thymeleaf
- **Frontend**: HTML5, Responsive CSS3 (Flexbox/Grid with CSS variables & media queries), Vanilla JavaScript
- **Build & Dependency Management**: Apache Maven & Maven Wrapper

---

## 4. Requirements

- **Java Development Kit (JDK)**: Version 17 or higher
- **Maven**: Version 3.8+ (or use the included Maven Wrapper scripts)

---

## 5. How to Run

Navigate into the `gobimart` project directory:

```bash
cd gobimart
```

### Windows (Command Prompt or PowerShell)
Using Maven Wrapper:
```cmd
mvnw.cmd spring-boot:run
```

Or using an installed Maven:
```cmd
mvn spring-boot:run
```

### Linux / macOS
```bash
mvn spring-boot:run
```

---

## 6. Accessing the Application

- **Storefront Application**: [http://localhost:8080](http://localhost:8080)
- **Admin Dashboard**: [http://localhost:8080/admin](http://localhost:8080/admin)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

### Actual H2 Database Configuration
When connecting to the H2 Web Console, use the following exact settings:
- **Driver Class**: `org.h2.Driver`
- **JDBC URL**: `jdbc:h2:file:./data/gobimartdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
- **User Name**: `sa`
- **Password**: *(leave blank / empty)*

---

## 7. Render Deployment (Docker)

GOBIMART is ready for 1-click or repository-based deployment on **Render** using Docker.

### Automated Setup Features:
- **Multi-Stage Build**: Compiles with Maven & Temurin JDK 17, runs on lightweight Temurin JRE 17 Alpine (~140MB).
- **Dynamic Port Binding**: Automatically listens on `${PORT}` passed by Render.
- **Render Free Tier Optimized**: JVM memory options configured (`-XX:MaxRAMPercentage=75.0 -Xss512k`) to avoid exceeding Render's 512MB RAM limit.
- **Non-Root Execution**: Runs under a dedicated `appuser` for container security.
- **Render Blueprint (`render.yaml`)**: Preconfigured for automated Web Service provisioning.

### Deployment Steps on Render:
1. Push your repository to GitHub / GitLab.
2. Log into [Render Dashboard](https://dashboard.render.com).
3. Click **New +** -> **Web Service** (or **Blueprint** to use `render.yaml`).
4. Connect your repository.
5. In settings:
   - **Environment / Runtime**: `Docker`
   - **Root Directory**: (Leave blank or set to `gobimart` — both are supported)
   - **Plan**: `Free`
   - **Health Check Path**: `/`
6. Click **Deploy Web Service**.

---

## 8. Project Structure

```text
gobimart/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── gobimart/
│   │   │           ├── GobiMartApplication.java       # Application entry point
│   │   │           ├── config/
│   │   │           │   └── DataInitializer.java       # Seeds 14 sample products on first boot
│   │   │           ├── controller/
│   │   │           │   ├── AdminController.java       # Admin dashboard & product/order CRUD
│   │   │           │   ├── CartController.java        # Shopping cart operations
│   │   │           │   ├── CheckoutController.java    # Checkout form & order submission
│   │   │           │   ├── HomeController.java        # Home page & featured showcases
│   │   │           │   ├── OrderController.java       # Customer order confirmation
│   │   │           │   └── ProductController.java     # Catalog, search, & product details
│   │   │           ├── dto/
│   │   │           │   ├── Cart.java                  # Cart state & total calculation
│   │   │           │   ├── CartItem.java              # Individual line item in cart
│   │   │           │   ├── CheckoutRequest.java       # Validated customer checkout request
│   │   │           │   └── ProductForm.java           # Validated admin product form
│   │   │           ├── entity/
│   │   │           │   ├── Order.java                 # JPA entity for customer orders
│   │   │           │   ├── OrderItem.java             # JPA entity for line items in an order
│   │   │           │   └── Product.java               # JPA entity for store products
│   │   │           ├── exception/
│   │   │           │   ├── EmptyCartException.java
│   │   │           │   ├── GlobalExceptionHandler.java# Centralized friendly error handling
│   │   │           │   ├── InsufficientStockException.java
│   │   │           │   └── ProductNotFoundException.java
│   │   │           ├── repository/
│   │   │           │   ├── OrderItemRepository.java   # Spring Data JPA OrderItem repo
│   │   │           │   ├── OrderRepository.java       # Spring Data JPA Order repo
│   │   │           │   └── ProductRepository.java     # Spring Data JPA Product repo
│   │   │           └── service/
│   │   │               ├── CartService.java           # Session-based cart business logic
│   │   │               ├── OrderService.java          # Transactional order placement & stock deduction
│   │   │               └── ProductService.java        # Product retrieval, search, & CRUD
│   │   └── resources/
│   │       ├── application.properties                 # App configuration & H2 file settings
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css                      # Modern responsive styles
│   │       │   └── js/
│   │       │       └── main.js                        # UI interactivity & confirmations
│   │       └── templates/
│   │           ├── index.html                         # Home page
│   │           ├── fragments/
│   │           │   ├── header.html                    # Responsive navbar & search bar
│   │           │   └── footer.html                    # Store footer
│   │           ├── products/
│   │           │   ├── list.html                      # Catalog listing & search results
│   │           │   └── detail.html                    # Detailed product view
│   │           ├── cart/
│   │           │   └── view.html                      # Shopping cart page
│   │           ├── checkout/
│   │           │   ├── form.html                      # Checkout form & order summary
│   │           │   └── confirmation.html              # Order placed confirmation screen
│   │           ├── admin/
│   │           │   ├── dashboard.html                 # Admin metrics & recent orders
│   │           │   ├── products.html                  # Product inventory management table
│   │           │   ├── product-form.html              # Add / Edit product form
│   │           │   ├── orders.html                    # All customer orders list
│   │           │   └── order-detail.html              # Detailed order review
│   │           └── error/
│   │               ├── 404.html                       # Friendly not found page
│   │               └── error.html                     # Friendly general error page
│   └── test/
│       └── java/
│           └── com/
│               └── gobimart/
│                   ├── GobiMartApplicationTests.java  # Spring Boot context test
│                   ├── repository/
│                   │   └── ProductRepositoryTest.java # Repository search & filter tests
│                   └── service/
│                       ├── CartServiceTest.java       # Cart calculation & stock capping tests
│                       ├── OrderServiceTest.java      # Order placement & stock reduction tests
│                       └── ProductServiceTest.java    # Product CRUD & exception tests
├── pom.xml                                            # Maven dependencies & build setup
├── mvnw                                               # Unix Maven wrapper
├── mvnw.cmd                                           # Windows Maven wrapper
└── README.md                                          # Documentation
```

---

## 9. Database Design

```text
  +------------------+         1 : N         +--------------------+         N : 1         +------------------+
  |      ORDERS      |---------------------->|    ORDER_ITEMS     |<----------------------|     PRODUCTS     |
  +------------------+                       +--------------------+                       +------------------+
  | id (PK)          |                       | id (PK)            |                       | id (PK)          |
  | customer_name    |                       | order_id (FK)      |                       | name             |
  | email            |                       | product_id (FK)    |                       | description      |
  | phone            |                       | quantity           |                       | price            |
  | address          |                       | price              |                       | image_url        |
  | total_amount     |                       +--------------------+                       | category         |
  | order_date       |                                                                    | stock            |
  | status           |                                                                    +------------------+
  +------------------+
```

### Relationship Details
- **Order to OrderItem**: One-to-Many (`@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)`). Deleting or saving an order automatically cascades to its line items.
- **OrderItem to Product**: Many-to-One (`@ManyToOne(fetch = FetchType.EAGER)`). Each line item refers to the specific product purchased and preserves the historical unit price at checkout time.
- **Product Inventory**: When an order is placed, the product stock count is atomically decreased via `@Transactional` execution.

---

## 10. Future Enhancements

- User registration, login, and profile history (Spring Security + JWT/session).
- Integrated payment gateways (Stripe, Razorpay, or PayPal).
- Customer product ratings and reviews.
- User wishlist functionality.
- Real-time order status tracking with email/SMS notifications.
- Admin role-based access control and sales analytics charts.

---

## 11. License & Academic Disclaimer

This project is built as an educational demonstration of clean monolithic Spring Boot web architecture for college students and academic evaluations.
