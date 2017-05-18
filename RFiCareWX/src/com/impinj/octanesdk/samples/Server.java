package com.impinj.octanesdk.samples;

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server implements Runnable {

	private Socket client;
	private DataInputStream dis;

	public Server() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getMsg() {
		byte[] b = new byte[1024];
		String message = "";
		try {

			dis = new DataInputStream(client.getInputStream());
			dis.read(b, 0, b.length);
			message = new String(b, "UTF-8"); // 关键！接收的是字节流，要转换为字符串才显示
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message.trim(); // 去掉空格
	}

	public void test() throws IOException {
		ServerSocket server = new ServerSocket(8895);
		while (true) {
			client = server.accept();
			System.out.println("connect successfully");
			String str = getMsg();
			if (!str.equals("")) {
				// System.out.println(str.trim());
				String[] tmp = str.trim().split(";");
				HashMap<String, Integer> map = Reader.getPatient_label();
				StringBuilder sb = new StringBuilder();
				for(int i = 0 ; i  < tmp[0].length() ; i+=4){
					sb.append(tmp[0].substring(i, i+4));
					sb.append(' ');
				}
				
				map.put(sb.toString().trim(), Reader.patient_curTagNum++);

				Test.ht.put(Test.ht.size(), tmp[0]);
			}
			client.close();
		}
	}
}
