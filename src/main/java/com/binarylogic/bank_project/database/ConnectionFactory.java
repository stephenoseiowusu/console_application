package com.binarylogic.bank_project.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	 private Connection connection;
	 public Connection setUp() {
		 try {
		  Class.forName ("com.mysql.cj.jdbc.Driver");
	      this.connection = DriverManager.getConnection("jdbc:mysql://localhost/bank_database","root","");
	      //System.out.println("Connection is :" + !connection.isClosed());  
		 }catch(SQLException | ClassNotFoundException e) {
			 System.err.println("Connection failed");
		 }
		 return connection;
		 
	  }
	 

}
