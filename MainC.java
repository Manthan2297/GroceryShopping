package edu.depaul;

import java.sql.SQLException;
import java.util.Scanner;

public class MainC {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            String username = null;
            while (true) {
                if (username == null) {
                    System.out.println("\n Welcome to Manthan's Grocery Center");
                    
                    System.out.println("1. Signin");
                    System.out.print("New User? Press 2 for New registration\n");
                    System.out.println("2. Signup");
                    System.out.println("3. Exit");
                    System.out.print("Your choice: ");
                    String choice = sc.nextLine();

                    switch (choice) {
                        case "1":
                            username = login(sc); 
                            break;
                        case "2":
                            register(sc);
                            break;
                        case "3":
                            System.out.println("Exiting...");
                            return;
                        default:
                            System.out.println("Invalid option. Please try again.");
                    }
				} 
                
            }
        } catch (Exception e) {
			
			e.printStackTrace();
		}
    }

    private static String login(Scanner sc) {
        System.out.println("\nLogin");
        System.out.println("Enter your Email id : ");
        String username = sc.nextLine();

        System.out.println("Enter your password: ");
        String password = sc.nextLine();

        boolean isAuthenticated = Authentication.Logincheck(username, password);

        if (isAuthenticated) {
            System.out.println("Welcome Back!" +" "+ username);
            
            ProductCatalog.ViewCategories(sc);
            OrderProcessing.Yourcart(username, sc);
           
        } else {
            System.out.println("Login Failed. Please check your username and password.");
            return null; 
        }
		return null;
    }

    private static void register(Scanner sc) throws SQLException {
        System.out.println("\nSign Up");
        System.out.println("Enter your Email id :");
        String username = sc.nextLine();

        System.out.println("Choose a password: ");
        String password = sc.nextLine();

        boolean isRegistered = Authentication.RegisterUser(username, password);

        if (isRegistered) {
            System.out.println("Welcome to Manthan's Grocery Center");
            ProductCatalog.ViewCategories(sc);
            OrderProcessing.Yourcart(username, sc);
            
        } else {
            System.out.println("Registration failed or email id already exists. Please try again.");
            
        }
    }

}
