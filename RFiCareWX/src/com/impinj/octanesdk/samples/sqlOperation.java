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

	public static void add(Container cont) {

		StringBuilder sql = new StringBuilder();
		int count = selectBednumber(cont);

		if (count == 0) {
			sql.append("insert into waterlevel values (");
			sql.append("'jack'");
			sql.append(",");
			// sql.append("'"+cont.getBedNum()+"'");
			sql.append("'" + 301 + "'");
			sql.append(",");
			sql.append("" + cont.getWaterLevel() * 12.5);
			sql.append(",");
			if (cont.isHasWarn()) {
				sql.append("1");
			} else {
				sql.append("0");
			}
			sql.append(",");
			sql.append("40");
			sql.append(",");
			sql.append("120");
			sql.append(",");
			sql.append("'NaCl'");
			sql.append(",");
			sql.append("04192008");
			sql.append(");");

		} else {
			sql.append("insert into waterlevel values (");
			sql.append("'jack'");
			sql.append(",");
			// sql.append("'"+cont.getBedNum()+"'");
			sql.append("'" + 301 + "'");
			sql.append(",");
			sql.append("" + cont.getWaterLevel() * 12.5);
			sql.append(",");
			if (cont.isHasWarn()) {
				sql.append("1");
			} else {
				sql.append("0");
			}
			sql.append(",");
			sql.append("40");
			sql.append(",");
			sql.append("120");
			sql.append(",");
			sql.append("'NaCl'");
			sql.append(",");
			sql.append("04192008");
			sql.append(");");
		}

		try {
			stmt.executeUpdate(sql.toString());
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

	public static int selectBednumber(Container cont) {
		ResultSet ret;
		int count = 0;
		try {
			ret = stmt.executeQuery("select * from waterlevel where bednumber = '" + cont.bedNum + "'");

			while (ret.next()) {
				count++;
			}
			ret.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return count;
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
