package com.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	private static Connection connection;
	private static final String ulr="jdbc:mysql://localhost:3306/ecommerceDb";
	private static final String user="root";
	private static final String password ="nextdefault";
	public static Connection connectDb() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(ulr, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
}
