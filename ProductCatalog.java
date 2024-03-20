package edu.depaul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ProductCatalog {
    private static final String url = "jdbc:mysql://localhost:3306/ecomm";
    private static final String username = "root";
    private static final String password = "Mysql@188";

    private Connection connection;

    public ProductCatalog() {
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ViewCategories(Scanner sc) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
             PreparedStatement ps = conn.prepareStatement("SELECT category_id, name FROM categories WHERE parent_id IS NOT NULL")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("+"+ rs.getString("name"));
            }
            System.out.println("Choose your category (1-6) :");
            int categoryId = sc.nextInt();
            sc.nextLine(); // consume the newline
            ViewProducts(categoryId, sc);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ViewProducts(int categoryId, Scanner sc) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
             PreparedStatement ps = conn.prepareStatement("SELECT product_id, name, description, price FROM products WHERE category_id = ?")) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            boolean hasProducts = false;
            while (rs.next()) {
                if (!hasProducts) {
                    System.out.println("Products in selected category:");
                    hasProducts = true;
                }
                System.out.println("Product ID: " + rs.getInt("product_id") + ", Name: " + rs.getString("name") + ", Description: " + rs.getString("description") + ", Price: $" + rs.getBigDecimal("price"));
            }
            if (!hasProducts) {
                System.out.println("No products found in this category.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

	