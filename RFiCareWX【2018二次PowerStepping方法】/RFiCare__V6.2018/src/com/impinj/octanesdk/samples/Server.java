package com.impinj.octanesdk.samples;

import java.io.*;
import java.net.*;

// ���࣬�Ƕ�����һ���̣߳����������ֳ��Ķ��������������ݣ�������Hand���е�infusion_bed�ļ��ϡ�
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
//			message = new String(b, "UTF-8"); // �ؼ������յ����ֽ�����Ҫת��Ϊ�ַ�������ʾ
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return message.trim(); // ȥ���ո�
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
