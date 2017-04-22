<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Statement" %>

<%
	    String bed_num = "'未知'";
	    String name = "";
	    double remaining = 0.0;  
	    boolean needWarn = false;
	    int flowrate = 0;
	    int timeremaining = 0;
	    String info = "";
	    int timestamp = 0;

//	    bed_num = request.getParameter("bed_num");

	    try {
			String url = "114.212.84.243";
			String user = "javatest";
			String password = "1234";
			Connection conn = DriverManager.getConnection("jdbc:mysql://"+ url +"/hebb", user, password);
			Statement stmt = conn.createStatement();

	        ResultSet ret = stmt.executeQuery("select * from waterlevel where bednumber = " + bed_num);

	        while (ret.next()) {
	        	name = ret.getString("name");
	        	remaining = ret.getDouble("remaining");	
	        	needWarn = ret.getBoolean("needWarn");
	        	flowrate = ret.getInt("flowrate");
	        	timeremaining = ret.getInt("timeremaining");
	        	info = ret.getString("info");
	        	timestamp = ret.getInt("timestamp");
//	        	System.out.println(name);
	        	
	        }


	    } catch (SQLException e) {
	        e.printStackTrace();
	    }



    //设置输出信息的格式及字符集
    response.setContentType("text/xml; charset=UTF-8");
    response.setHeader("Cache-Control","no-cache");
    out.println("<response>");
    out.println("<name>"+ name + "</name>");
    out.println("<remaining>"+ remaining + "</remaining>");
    out.println("<needWarn>"+ needWarn + "</needWarn>");
    out.println("<flowrate>"+ flowrate + "</flowrate>");
    out.println("<timeremaining>"+ timeremaining + "</timeremaining>");
    out.println("<info>"+ info + "</info>");
    out.println("<timestamp>"+ timestamp + "</timestamp>");



    out.println("</response>");
    out.close();
%>