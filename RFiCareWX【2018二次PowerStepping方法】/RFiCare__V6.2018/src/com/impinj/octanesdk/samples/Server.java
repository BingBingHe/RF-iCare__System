package com.impinj.octanesdk.samples;

import java.io.*;
import java.net.*;

// 此类，是独立的一个线程，用来接收手持阅读器传过来的数据，并更新Hand类中的infusion_bed的集合。
//
//public class Server implements Runnable {

//	private Socket client;
//	private DataInputStream dis;
//	private ServerSocket server;
////
//	public Server() {
//		// TODO Auto-generated constructor stub
//	}
//
//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		try {
//			test();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public String getMsg() {
//		byte[] b = new byte[1024];
//		String message = "";
//		try {
//
//			dis = new DataInputStream(client.getInputStream());
//			dis.read(b, 0, b.length);
//			message = new String(b, "UTF-8"); // 关键！接收的是字节流，要转换为字符串才显示
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return message.trim(); // 去掉空格
//	}
//
//	public void test() throws IOException {
//		server = new ServerSocket(8895);
//		while (true) {
//			client = server.accept();
//			System.out.println("connect successfully");
//			String str = getMsg();
//
//			Hand.getInfusion_bed().add(str);
//			client.close();
//		}
//	}
//}
