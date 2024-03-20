package edu.depaul;

import java.sql.SQLException;
import java.util.Scanner;

public interface PaymentGateway {
    
	boolean CardAuthenticate(String expiryDate, String cvv);
    
}
