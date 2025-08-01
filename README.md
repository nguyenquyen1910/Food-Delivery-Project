# 🍽️ Food Delivery Management System

A comprehensive, full-stack food delivery platform built with **Spring Boot 3.3.3** and **Java 21**, featuring a modern web interface, robust backend architecture, and enterprise-grade security.

## 🚀 Features

### **Customer Portal**

- **Product Catalog**: Browse and search through extensive food menu with advanced filtering
- **Shopping Cart**: Add/remove items with real-time quantity management
- **Order Management**: Place orders, track status, and view order history
- **User Authentication**: Secure login/registration with JWT token-based authentication
- **Email Verification**: Account verification via email with SMTP integration
- **Responsive Design**: Mobile-first approach with modern UI/UX

### **Admin Dashboard**

- **Product Management**: CRUD operations for products with image upload via Cloudinary
- **Category Management**: Organize products by categories
- **Order Processing**: Real-time order tracking and status updates
- **Customer Management**: User account administration and analytics
- **Sales Analytics**: Comprehensive reporting and statistics
- **Inventory Control**: Stock management and product status tracking

### **Technical Features**

- **RESTful API**: Complete REST API with proper HTTP status codes
- **Database Integration**: MySQL with JPA/Hibernate ORM
- **File Management**: Cloudinary integration for image storage and optimization
- **Security**: Spring Security with JWT authentication and role-based access control
- **Email Services**: Automated email notifications and verification
- **Pagination**: Efficient data pagination for large datasets
- **Search & Filtering**: Advanced search with multiple criteria

## 🛠️ Technology Stack

### **Backend**

- **Java 21** - Latest LTS version with modern features
- **Spring Boot 3.3.3** - Rapid application development framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data persistence layer
- **Hibernate** - Object-relational mapping
- **MySQL 8.0** - Relational database
- **JWT** - JSON Web Token for stateless authentication
- **MapStruct** - Type-safe object mapping
- **Thymeleaf** - Server-side templating engine

### **Frontend**

- **HTML5/CSS3** - Modern web standards
- **JavaScript (ES6+)** - Dynamic client-side functionality
- **Font Awesome** - Professional icon library
- **Responsive Design** - Mobile-first approach

### **External Services**

- **Cloudinary** - Cloud image management and optimization
- **Gmail SMTP** - Email delivery service
- **Maven** - Dependency management and build tool

### **Development Tools**

- **Spring Boot DevTools** - Hot reload and development utilities
- **Lombok** - Reduces boilerplate code
- **Maven Wrapper** - Consistent build environment

## 🏗️ Architecture

### **Layered Architecture**

```
┌─────────────────────────────────────┐
│           Presentation Layer        │
│  (Controllers + Thymeleaf Views)    │
├─────────────────────────────────────┤
│           Business Layer            │
│        (Services + DTOs)            │
├─────────────────────────────────────┤
│           Data Access Layer         │
│      (Repositories + Entities)      │
├─────────────────────────────────────┤
│           Database Layer            │
│            (MySQL 8.0)              │
└─────────────────────────────────────┘
```

### **Core Components**

- **Controllers**: REST API endpoints and view controllers
- **Services**: Business logic implementation
- **Repositories**: Data access layer with Spring Data JPA
- **Entities**: JPA entities with proper relationships
- **DTOs**: Data transfer objects for API responses
- **Security**: Custom JWT filter and authentication
- **Configuration**: Modular configuration files

## 📁 Project Structure

```
src/main/java/com/foodproject/fooddelivery/
├── config/                 # Configuration classes
├── controller/             # REST controllers and view controllers
├── dto/                   # Data Transfer Objects
├── entity/                # JPA entities
├── mapper/                # MapStruct mappers
├── payload/               # Request/Response payloads
├── repository/            # Data access repositories
├── security/              # Security configuration and filters
├── service/               # Business logic services
│   └── imp/              # Service implementations
└── utils/                 # Utility classes
```

## 🚀 Getting Started

### **Prerequisites**

- Java 21 or higher
- MySQL 8.0
- Maven 3.6+
- Cloudinary account
- Gmail account for SMTP

### **Installation**

1. **Clone the repository**

   ```bash
   git clone https://github.com/nguyenquyen1910/Food-Delivery-Project.git
   cd food-delivery-system
   ```

2. **Database Setup**

   ```sql
   CREATE DATABASE food_delivery;
   ```

3. **Configuration**

   - Copy configuration example files:

     - `application-database-example.yml` → `application-database.yml`
     - `application-mail-example.yml` → `application-mail.yml`
     - `application-cloudinary-example.yml` → `application-cloudinary.yml`
     - `application-jwt-example.yml` → `application-jwt.yml`

   - Update configuration with your credentials:

     - Database connection details
     - Cloudinary API credentials
     - Gmail SMTP settings
     - JWT secret key

4. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**

   - Customer Portal: `http://localhost:8080`
   - Admin Dashboard: `http://localhost:8080/admin`

## 🔧 Configuration

### **Modular Configuration**

The application uses a modular configuration approach with separate YAML files:

- `application.yml` - Main configuration with profile includes
- `application-database.yml` - Database configuration
- `application-mail.yml` - Email service configuration
- `application-cloudinary.yml` - Cloudinary integration
- `application-jwt.yml` - JWT security configuration

### **Environment Variables**

For production deployment, use environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/food_delivery
export SPRING_DATASOURCE_USERNAME=your_username
export SPRING_DATASOURCE_PASSWORD=your_password
export CLOUDINARY_CLOUD_NAME=your_cloud_name
export CLOUDINARY_API_KEY=your_api_key
export CLOUDINARY_API_SECRET=your_api_secret
```

## 🔐 Security Features

- **JWT Authentication**: Stateless token-based authentication
- **Role-based Access Control**: Admin and customer role separation
- **Password Encryption**: BCrypt password hashing
- **CORS Configuration**: Cross-origin resource sharing setup
- **Input Validation**: Request parameter validation
- **SQL Injection Prevention**: Parameterized queries via JPA

## 📊 Database Schema

### **Core Entities**

- **Users**: Customer and admin accounts
- **Products**: Food items with categories
- **Categories**: Product classification
- **Orders**: Customer orders with status tracking
- **OrderItems**: Individual items in orders
- **Cart/CartItems**: Shopping cart functionality
- **Roles**: User role management

### **Key Relationships**

- One-to-Many: User → Orders, Category → Products
- Many-to-Many: Orders ↔ Products (via OrderItems)
- One-to-One: User ↔ Cart

## 🧪 API Endpoints

### **Product Management**

- `GET /products/all` - Get all products
- `GET /products/homepage?page={page}` - Paginated products
- `GET /products/find` - Advanced search with filters
- `POST /products/admin/create` - Create new product
- `PUT /products/admin/edit` - Update product
- `DELETE /products/admin/delete/{id}` - Delete product

### **User Management**

- `POST /user/signup` - User registration
- `POST /user/login` - User authentication
- `GET /user/profile` - Get user profile
- `PUT /user/change-info` - Update user information

### **Order Management**

- `POST /order/create` - Create new order
- `GET /order/history` - Get order history
- `GET /order/admin/all` - Admin order management

### **Cart Operations**

- `POST /cart/add` - Add item to cart
- `PUT /cart/update` - Update cart item
- `DELETE /cart/delete/{id}` - Remove item from cart

## 🎨 UI/UX Features

- **Modern Design**: Clean, professional interface
- **Responsive Layout**: Works seamlessly on all devices
- **Interactive Elements**: Dynamic search, filtering, and cart management
- **Toast Notifications**: User-friendly feedback messages
- **Loading States**: Smooth user experience during operations
- **Accessibility**: WCAG compliant design elements

## 📈 Performance Optimizations

- **Database Indexing**: Optimized queries with proper indexes
- **Pagination**: Efficient data loading for large datasets
- **Image Optimization**: Cloudinary integration for fast image delivery
- **Caching**: Strategic caching for frequently accessed data
- **Lazy Loading**: On-demand data loading

## 🔄 Development Workflow

1. **Feature Development**: Implement new features in feature branches
2. **Code Review**: Peer review process for quality assurance
3. **Testing**: Unit and integration testing
4. **Deployment**: Automated deployment pipeline
5. **Monitoring**: Application performance monitoring

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Your Name**

- LinkedIn: /nguyenquyen1910
- GitHub: /nguyenquyen1910
- Email: jrnguyen14@gmail.com

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Cloudinary for image management services
- Font Awesome for the icon library
- MySQL team for the reliable database

---

⭐ **Star this repository if you find it helpful!**
