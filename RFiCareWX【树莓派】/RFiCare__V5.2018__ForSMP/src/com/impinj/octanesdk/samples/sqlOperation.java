package com.impinj.octanesdk.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

// MySql的JDBC操作
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

		String url = "192.168.100.82";
		String user = "javatest";
		String password = "1234";

		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + url + "/rfiddislab", user, password);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void add(Container cont) {

		StringBuilder sql = new StringBuilder();
		int count = selectBednumber(cont);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String time = "" + df.format(new Date());
		if (count == 0) {

			sql.append("insert into waterlevel values ("); // bednumber
			sql.append("'" + cont.getBedNum() + "'");
			sql.append(",");
			sql.append("'" + cont.getTag1() + "'"); // tag1 epc
			sql.append(",");
			sql.append("'" + cont.getTag2() + "'"); // tag2 epc
			sql.append(",");
			sql.append("" + cont.waterLevel); // water level
			sql.append(",");
			sql.append("" + cont.isHasWarn()); // 有无警报
			sql.append(",");
			sql.append("" + cont.isPresent()); // 是否存在
			sql.append(",");
			sql.append("'" + time + "'"); // time
			sql.append(");");

		} else {
			sql.append("update waterlevel set ");
			sql.append("waterlevel = " + cont.waterLevel); //
			sql.append(",");
			sql.append("hasWarn = " + cont.isHasWarn()); // n
			sql.append(",");
			sql.append("isPresent = " + cont.isHasWarn()); // n
			sql.append(",");
			sql.append("time = ");
			sql.append("'" + time + "'"); // time
			sql.append(" where bedNumber = '" + cont.getBedNum() + "';");

			// sql.append("update waterlevel set ");
			// sql.append("waterLevel = " + cont.waterLevel); // name
			// sql.append(",");
			// sql.append("time=");
			// sql.append("'" + time + "'"); // time
			// sql.append(" where bedNumber = '" + cont.getBedNum() + "';");
		}

		try {
			stmt.executeUpdate(sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// public static void select() {
	// ResultSet ret;
	// try {
	// ret = stmt.executeQuery("select * from waterlevel");
	// while (ret.next()) {
	// System.out.print("【 ");
	// System.out.print(ret.getString("bednumber") + " ");
	// System.out.print(ret.getString("name") + " ");
	// System.out.print(ret.getString("remaining") + " ");
	// System.out.print(ret.getString("needwarn") + " ");
	// System.out.print(ret.getString("info") + " ");
	// System.out.println(ret.getString("time") + "】");
	// }
	// ret.close();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static int selectBednumber(Container cont) {
		ResultSet ret;
		int count = 0;
		try {
			ret = stmt.executeQuery("select * from waterlevel where bedNumber = '" + cont.getBedNum() + "'");

			while (ret.next()) {
				count++;
				// System.out.print("【 ");
				// System.out.print(ret.getString("bednumber") + " ");
				// System.out.print(ret.getString("name") + " ");
				// System.out.print(ret.getString("remaining") + " ");
				// System.out.print(ret.getString("needwarn") + " ");
				// System.out.print(ret.getString("info") + " ");
				// System.out.println(ret.getString("time") + "】");
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
