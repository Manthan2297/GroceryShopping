package edu.depaul;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class PaymentProcess{ 



public void proceedToPayment(String username, Scanner sc) throws SQLException {
	// TODO Auto-generated method stub
	System.out.println("Your Payment Methods:");
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
    String query = "SELECT pm.id, pm.card_number, pm.card_type, pm.expiry_date FROM payment_methods pm JOIN users u ON pm.user_id = u.id WHERE u.username = ?";
    try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + ", Card Number: " + rs.getString("card_number") + ", Type: " + rs.getString("card_type") + ", Expiry Date: " + rs.getDate("expiry_date"));
        }
    }
    
    System.out.println("\nDo you want to add a new card? (yes/no)");
    if ("yes".equalsIgnoreCase(sc.nextLine())) {
        addNewPaymentMethod(username, sc);
    } else {
        // Proceed to select one of the existing cards for payment
        System.out.println("Select a card ID for payment:");
        int cardId = Integer.parseInt(sc.nextLine());
        String cardTypeQuery = "SELECT card_type FROM payment_methods WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(cardTypeQuery)) {
            ps.setInt(1, cardId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String cardType = rs.getString("card_type");
                System.out.println("The selected card is a " + cardType + " card.");
            }
        
        proceedToPayment(cardId, username, sc); // Implement this method to handle payment
    }
	
    }	
}

public static void addNewPaymentMethod(String username, Scanner sc) throws SQLException {
	System.out.println("Enter card number: (card number must be 12 digits)");
	String cardNumber;
	while (true) {
			cardNumber = sc.nextLine();
			if (cardNumber.length() != 12) {
					System.out.println("Invalid input. Please enter a valid 12 digit card number:");
			} else if (cardNumberExists(cardNumber)) {
					System.out.println("This card number already exists. Please enter a different card number:");
			} else {
					break; 
			}
	}

	System.out.println("Enter expiry date (YYYY-MM-DD):");
	String expiryDate;
	while (true) {
			expiryDate = sc.nextLine();
			if (expiryDate.length() != 10) {
					System.out.println("Invalid input. Please enter a valid expiry date in the format YYYY-MM-DD:");
			} else {
					break;
			}
	}

	System.out.println("Enter CVV:");
	String cvv = sc.nextLine(); // Changed to String to check length
	while (cvv.length() != 3) {
			System.out.println("Invalid input. CVV must be 3 digits. Please enter a valid CVV:");
			cvv = sc.nextLine();
	}

	System.out.println("Enter card type (DEBIT/CREDIT):");
	String cardType;
	while (true) {
			cardType = sc.nextLine().toUpperCase();
			if (!cardType.equals("DEBIT") && !cardType.equals("CREDIT")) {
					System.out.println("Invalid input. Please enter either DEBIT or CREDIT:");
			} else {
					break; // Valid card type
			}
	}

	

	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
			PreparedStatement ps = conn.prepareStatement("INSERT INTO payment_methods (user_id, card_number, expiry_date, cvv, card_type) VALUES " +
											"((SELECT id FROM users WHERE username = ?), ?, ?, ?, ?)")) {
			ps.setString(1, username);
			ps.setString(2, cardNumber);
			ps.setString(3, expiryDate);
			ps.setString(4, cvv);
			ps.setString(5, cardType);
			

			int rowsAffected = ps.executeUpdate();
			if (rowsAffected > 0) {
					System.out.println("New payment method added successfully.");
			} else {
					System.out.println("Failed to add the new payment method.");
			}
	

		

	}
    
}


public void proceedToPayment(int cardId, String username, Scanner sc) throws SQLException {
	// TODO Auto-generated method stub
	
	// Retrieve the total cart value
    BigDecimal cartTotal = OrderProcessing.Total(username);
    

    System.out.println("Enter the amount to pay:");
    BigDecimal amount = new BigDecimal(sc.nextLine());

    // Check if the entered amount matches the cart total
    if (amount.compareTo(cartTotal) != 0) {
        System.out.println("Payment rejected. The entered amount does not match the total cart value.");
        return;
    }

    // If the amount matches, proceed with payment processing
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM payment_methods WHERE id = ? AND user_id = (SELECT id FROM users WHERE username = ?)")) {
        ps.setInt(1, cardId);
        ps.setString(2, username);
        

        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            System.out.println("Payment of $" + amount + " processed successfully with card ID: " + cardId);
            
            // Here, integrate with an actual payment processing system or gateway
        } else {
            System.out.println("Payment failed. Card details not found or do not match.");
        }
        
        boolean paymentSuccess = true; // This should be replaced with actual payment success status

        if (paymentSuccess) {
            // Retrieve the userId for the username
            int userId = Authentication.UserId(username);

            // Clear the user's cart
            OrderProcessing.clearCart(userId);

            // Inform the user
            System.out.println("Payment processed successfully. Your cart has been cleared.");
            
           
        } else {
            // Handle payment failure
            System.out.println("Payment failed. Please try again.");
        }
    }
	
}

public static boolean cardNumberExists(String cardNumber) throws SQLException {
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
         PreparedStatement ps = conn.prepareStatement("SELECT card_number FROM payment_methods WHERE card_number = ?")) {
        ps.setString(1, cardNumber);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next(); // True if card number exists
        }
    }
}
}

