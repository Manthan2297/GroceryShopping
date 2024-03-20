package edu.depaul;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Credit implements PaymentGateway {
	
	String CVV;
	String Expiry;
	
    public Credit() throws SQLException {
        // Constructor logic if needed
    	// Use your actual password
    	try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ShoppingDB", "root", "root");
	            PreparedStatement ps = conn.prepareStatement("SELECT pm.expiry_date, pm.cvv FROM payment_methods pm JOIN users u ON pm.user_id = u.id WHERE u.username = ?")) {
	            ResultSet rs = ps.executeQuery();
	            
	            if (rs.next()) {
	                // Now that we have the first category, let's show products in this category
	            	String cvv = rs.getString("cvv");
	            	String exp = rs.getString("expiry_date");
	            	CardAuthenticate(cvv,exp);
	            } else {
	                System.out.println("No categories found.");
	            }
    	}
                    
    	
    	
    
    }


	@Override
	public boolean CardAuthenticate(String cvv, String expirydate) {
		// TODO Auto-generated method stub
		if(CVV == cvv && Expiry == expirydate ) {
			return true;
			
		}
		return false;
	}
    
    
}
