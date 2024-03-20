package edu.depaul;
import java.sql.*;

public class DatabaseConnection {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String url = "jdbc:mysql://localhost:3306/ecomm";
		String username = "root";
		String password = "Mysql@188";

		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection(url, username, password);
		System.out.println("Connected to the database");
		



	}

}
