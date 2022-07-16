package com.ecommerce;

import java.sql.Connection;

import com.connection.DB;

public class Main {
	
	public static void main(String[] args) {
		Connection con = DB.connectDb();
		System.out.println(con);
	}
}
