package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnector {
	
	/**
	 * Database Name
	 * */
	 static String dbName = "health_management_system";
	
	 /**
	  * Database User
	  * */
	 static String userName = "root";
	 
	 /**
	  * Database Password
	  * */
	 static String password = "root@localhost";
	 
	 /**
	  * Driver
	  * */
	 static String driver="com.mysql.cj.jdbc.Driver";
	 
	 /**
	  * Server URL
	  * */
	 static String serverURL="jdbc:mysql://localhost:3306/";
	 
	 /**
	  *Returns the connection of database 
	  * */
	
	 public static Connection getConnection() {
	
		 try {
		
			Class.forName(driver);
			Connection	connection = DriverManager.getConnection(serverURL+ dbName, userName, password);
			System.out.println("=============>Connection Established Successfully to Database");
			return connection;
		
		 }
		 
		 catch (ClassNotFoundException e) {
		
			 System.out.println("================>MYSQLConnector:Class Not Fount Exception");
		
		 } 
		 catch (SQLException e) {
		
			 System.out.println("================>MYSQLConnector:"+e.getMessage());
		
		 }
		
		 return null;

	}
}
