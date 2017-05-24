package com.impinj.octanesdk.samples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

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
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String time = "" + df.format(new Date());
		if (count == 0) {

			sql.append("insert into waterlevel values ("); // bednumber
			sql.append("'" + cont.getBedNum() + "'");
			sql.append(",");
			sql.append("'张三'"); // name
			sql.append(",");
			sql.append("" + cont.getWaterLevel() * 12.5); // remaining
			sql.append(",");
			if (cont.isHasWarn()) { // needWarn
				sql.append("'yes'");
			} else {
				sql.append("'no'");
			}
			sql.append(",");
			sql.append("'0.9%NaCl'"); // info
			sql.append(",");
			sql.append("'" + time + "'"); // time
			sql.append(");");

		} else {

			sql.append("update waterlevel set ");
			sql.append("name = '张三'"); // name
			sql.append(",");
			sql.append("remaining=" + cont.getWaterLevel() * 12.5); // remaining
			sql.append(",");
			sql.append("needwarn=");
			if (cont.isHasWarn()) { // needwarn
				sql.append("'yes'");
			} else {
				sql.append("'no'");
			}
			sql.append(",");
			sql.append("info=");
			sql.append("'0.9% NaCl溶液'"); // info
			sql.append(",");
			sql.append("time=");
			sql.append("'" + time + "'"); // time
			sql.append(" where bednumber = '" + cont.getBedNum() + "';");
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
				System.out.print("【 ");
				System.out.print(ret.getString("bednumber") + " ");
				System.out.print(ret.getString("name") + " ");
				System.out.print(ret.getString("remaining") + " ");
				System.out.print(ret.getString("needwarn") + " ");
				System.out.print(ret.getString("info") + " ");
				System.out.println(ret.getString("time") + "】");
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
				System.out.print("【 ");
				System.out.print(ret.getString("bednumber") + " ");
				System.out.print(ret.getString("name") + " ");
				System.out.print(ret.getString("remaining") + " ");
				System.out.print(ret.getString("needwarn") + " ");
				System.out.print(ret.getString("info") + " ");
				System.out.println(ret.getString("time") + "】");
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
