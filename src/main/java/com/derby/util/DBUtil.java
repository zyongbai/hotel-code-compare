package com.derby.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class DBUtil {
	private static String url = SystemMessage.getString("url");
	private static String driver = SystemMessage.getString("driver");
	private static String username = SystemMessage.getString("username");
	private static String password = SystemMessage.getString("password");
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return conn;
	}
}
