package com.impinj.octanesdk.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class sqlOperation {
	public static Connection conn;
	public static Statement stmt;

	public static void connection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// String url = "114.212.84.243";
		String url = "120.24.42.68";
		String user = "javatest";
		String password = "1234";

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + url + "/hebb", user, password);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void add(String tmp) {

		try {
			stmt.executeUpdate(tmp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void select() {
		ResultSet ret;
		try {
			ret = stmt.executeQuery("select * from waterlevel");
			while (ret.next()) {
				System.out.print(ret.getString("name") + " ");
				System.out.println(ret.getString("info"));
			}
			ret.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void close() {
		try {
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
