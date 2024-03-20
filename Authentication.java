package edu.depaul;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class Authentication {
    static String url = "jdbc:mysql://localhost:3306/ecomm";
    static String username = "root";
    static String password = "Mysql@188";

    public static boolean Logincheck(String username, String password) {
        try (
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm","root", "Mysql@188");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
        ) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            return result.next();
        } catch (SQLException e) {
            System.out.println("Error in Logincheck: " + e.getMessage());
        }
        return false;
    }

	
	
	  public static boolean UserCheck(String username) { try ( Connection
	  connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm","root","Mysql@188"); 
	  PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?"); ) {
	  statement.setString(1, username);
	  
	  ResultSet result = statement.executeQuery();
	  
	  return result.next(); } catch (SQLException e) {
	  System.out.println("Error in UserCheck: " + e.getMessage()); } return false;
	  }
	 

    public static boolean RegisterUser(String username, String password) throws SQLException {
        try {
            if (!UserCheck(username)) {
                String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
                try (
                    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm","root", "Mysql@188");
                    PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, username);
                    statement.setString(2, password);
                    statement.executeUpdate(); // Use executeUpdate for insert statements
                    return true;
                }
            }
            else {
                return false;
            }
            
        }
         
		  catch (SQLIntegrityConstraintViolationException e) {
		  System.out.println("Username already exists: " + e.getMessage()); } catch
		  (SQLException e) { System.out.println("Error in RegisterUser: " +
		  e.getMessage()); }
		 
			 return false; 
    }
    public static int UserId(String username) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ecomm", "root", "Mysql@188");
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                throw new SQLException("User not found");
                
            }
        }
    }
    
    
}


