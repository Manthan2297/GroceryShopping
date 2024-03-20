package edu.depaul;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class OrderProcessing {
	
	private static OrderProcessing instance;
    
    private OrderProcessing() { }

    public static synchronized OrderProcessing getInstance() {
        if (instance == null) {
            instance = new OrderProcessing();
        }
        return instance;
    }
    public static void Yourcart(String username, Scanner sc) {
        // Existing code to display cart items
        String query = "SELECT c.cart_id, c.product_id, p.name, c.quantity FROM carts c JOIN users u ON c.user_id = u.id JOIN products p ON c.product_id = p.product_id WHERE u.username = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            System.out.println("Your Cart:");
            while (rs.next()) {
                int cartId = rs.getInt("cart_id"); // Get cart ID from the result set
                int productId = rs.getInt("product_id");
                String productName = rs.getString("name");
                int quantity = rs.getInt("quantity");
                System.out.println("Cart ID: " + cartId + ", Product ID: " + productId + ", Product Name: " + productName + ", Quantity: " + quantity);
            }
            Fprocess(username, sc);  // Call to display next options after cart
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void Fprocess(String username, Scanner sc) throws SQLException {
        System.out.println("\nWhat would you like to do next?");
        
        
        System.out.println("1. Add Product's to Cart");
        System.out.println("2. Remove Product's from Cart");
        System.out.println("3. View your Total");
        System.out.println("4. Proceed to Payment");
        System.out.println("5. Back to Main Menu");
        System.out.println("6. Logout");
        System.out.print("Your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Consume the newline

        switch (choice) {
            
            
            case 1:
                System.out.print("Enter Product ID: ");
                int productIdToAdd = sc.nextInt();
                System.out.print("Enter Quantity: ");
                int quantityToAdd = sc.nextInt();
                AddtoCart(Authentication.UserId(username), productIdToAdd, quantityToAdd);
                sc.nextLine(); // Consume the newline
                break;
            case 2:
                System.out.print("Enter Product ID to remove: ");
                int ProductIDToRemove = sc.nextInt();
                
                RemoveFromCart(Authentication.UserId(username), ProductIDToRemove);
                sc.nextLine(); // Consume the newline
               break;
            case 3:
                Total(username);
                break;
            case 4:
            	PaymentProcess paymentProcessor = new PaymentProcess(); 
                paymentProcessor.proceedToPayment(username, sc); 
                break;
            case 5:
            	BackToMenu(username, sc);
                break;
            case 6:
            	System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                Fprocess(username, sc);
                break;
        }
        
    }
    public static void BackToMenu(String username, Scanner sc) throws SQLException {
        System.out.println("Continuing shopping...");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
            PreparedStatement ps = conn.prepareStatement("SELECT category_id, name FROM categories WHERE parent_id IS NOT NULL")) {
            ResultSet rs = ps.executeQuery();
            // Ensure we have at least one category to show
            if (rs.next()) {
                // Now that we have the first category, let's show products in this category
                ProductCatalog.ViewCategories(sc);
                //ProductCatalog.searchProducts(sc);
            } else {
                System.out.println("No categories found.");
            }
            return;
        }
    }
    public static void AddtoCart(int userId, int productId, int quantity) throws SQLException {
        String query = "INSERT INTO carts (user_id, product_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
            System.out.println("Product added to cart successfully.");
            
        }
    }
    public static void RemoveFromCart(int userId, int ProductID) throws SQLException {
        String query = "DELETE FROM carts WHERE product_id = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
            PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, ProductID);
            ps.setInt(2, userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Product removed from cart successfully.");
            } else {
                System.out.println("No product found with the specified ID in your cart.");
            }
        }
    }
    public static BigDecimal Total(String username) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        String query = "SELECT SUM(p.price * c.quantity) AS total FROM carts c JOIN products p ON c.product_id = p.product_id JOIN users u ON c.user_id = u.id WHERE u.username = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getBigDecimal("total");
                System.out.println("Total Cart Value: $" + total);
            }
        }
        return total;
    }
    public static void clearCart(int userId) throws SQLException {
        String query = "DELETE FROM carts WHERE user_id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Cart cleared successfully.");
            } else {
                System.out.println("Failed to clear the cart or cart was already empty.");
            }
        }
    }
    
}